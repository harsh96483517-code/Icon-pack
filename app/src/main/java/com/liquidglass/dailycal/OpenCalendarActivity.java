package com.liquidglass.dailycal;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;

/** Invisible activity: refreshes the icon, then opens the phone's calendar app. */
public class OpenCalendarActivity extends Activity {
    private static final String[] CALENDAR_PACKAGES = {
            "com.samsung.android.calendar",
            "com.google.android.calendar",
            "com.android.calendar",
            "com.samsung.android.app.calendar"
    };

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        IconUpdater.update(this);

        Intent i = null;
        for (String pkg : CALENDAR_PACKAGES) {
            i = getPackageManager().getLaunchIntentForPackage(pkg);
            if (i != null) break;
        }
        if (i == null) {
            Uri.Builder u = CalendarContract.CONTENT_URI.buildUpon().appendPath("time");
            ContentUris.appendId(u, System.currentTimeMillis());
            i = new Intent(Intent.ACTION_VIEW).setData(u.build());
        }
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(i);
        } catch (Exception ignored) { }
        finish();
    }
}
