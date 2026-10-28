package com.liquidglass.dailycal;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Sets today's date icon (app icon aliases and/or the pinned home screen shortcut)
 * and schedules the next midnight update.
 */
public final class IconUpdater {
    static final String SHORTCUT_ID = "daily_calendar";
    static final String ACTION_TICK = "com.liquidglass.dailycal.TICK";
    private static final String PREFS = "dailycal";
    private static final String KEY_ALIAS = "alias_mode";

    private IconUpdater() {}

    static SharedPreferences prefs(Context c) {
        return c.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    static boolean aliasMode(Context c) { return prefs(c).getBoolean(KEY_ALIAS, false); }

    static void setAliasMode(Context c, boolean on) {
        prefs(c).edit().putBoolean(KEY_ALIAS, on).apply();
        update(c);
    }

    static int today() { return Calendar.getInstance().get(Calendar.DAY_OF_MONTH); }

    static Bitmap bitmap(Context c, int day) {
        int id = c.getResources().getIdentifier("cal_" + day, "drawable", c.getPackageName());
        if (id == 0) id = c.getResources().getIdentifier("ic_app", "drawable", c.getPackageName());
        if (id == 0) return null;
        return BitmapFactory.decodeResource(c.getResources(), id);
    }

    static ShortcutInfo shortcut(Context c, int day) {
        Intent open = new Intent(c, OpenCalendarActivity.class).setAction(Intent.ACTION_MAIN);
        ShortcutInfo.Builder b = new ShortcutInfo.Builder(c, SHORTCUT_ID)
                .setShortLabel("Calendar")
                .setIntent(open);
        Bitmap bm = bitmap(c, day);
        if (bm != null) b.setIcon(Icon.createWithBitmap(bm));
        return b.build();
    }

    public static void update(Context c) {
        int day = today();
        boolean alias = aliasMode(c);

        PackageManager pm = c.getPackageManager();
        try {
            for (int d = 1; d <= 31; d++) {
                ComponentName cn = new ComponentName(c, c.getPackageName() + ".Day" + d);
                int want = (alias && d == day)
                        ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                        : PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
                int cur = pm.getComponentEnabledSetting(cn);
                boolean same = cur == want
                        || (want == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                            && cur == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
                if (!same) pm.setComponentEnabledSetting(cn, want, PackageManager.DONT_KILL_APP);
            }
        } catch (Exception ignored) { }

        try {
            ShortcutManager sm = c.getSystemService(ShortcutManager.class);
            if (sm != null) {
                List<ShortcutInfo> list = new ArrayList<>();
                list.add(shortcut(c, day));
                // The shortcut must exist as a dynamic shortcut before it can be pinned,
                // and updateShortcuts() only refreshes dynamic/pinned shortcuts.
                sm.setDynamicShortcuts(list);
                boolean pinned = false;
                for (ShortcutInfo s : sm.getPinnedShortcuts()) {
                    if (SHORTCUT_ID.equals(s.getId())) pinned = true;
                }
                if (pinned) sm.updateShortcuts(list);
            }
        } catch (Exception ignored) { }

        schedule(c);
    }

    static void schedule(Context c) {
        AlarmManager am = c.getSystemService(AlarmManager.class);
        if (am == null) return;
        Calendar next = Calendar.getInstance();
        next.add(Calendar.DAY_OF_YEAR, 1);
        next.set(Calendar.HOUR_OF_DAY, 0);
        next.set(Calendar.MINUTE, 0);
        next.set(Calendar.SECOND, 5);
        next.set(Calendar.MILLISECOND, 0);
        Intent tick = new Intent(c, DateReceiver.class).setAction(ACTION_TICK);
        PendingIntent pi = PendingIntent.getBroadcast(c, 1, tick,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, next.getTimeInMillis(), pi);
    }
}
