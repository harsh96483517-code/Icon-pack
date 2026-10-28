package com.liquidglass.dailycal;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ShortcutManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private Button aliasBtn;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        int pad = (int) (24 * getResources().getDisplayMetrics().density);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER_HORIZONTAL);
        root.setPadding(pad, pad * 2, pad, pad);

        ImageView iv = new ImageView(this);
        iv.setImageBitmap(IconUpdater.bitmap(this, IconUpdater.today()));
        root.addView(iv, new LinearLayout.LayoutParams(pad * 5, pad * 5));

        TextView t = new TextView(this);
        t.setText("Daily Calendar icon\nToday: " + IconUpdater.today()
                + "\n\nThe icon changes to the new date every midnight.");
        t.setGravity(Gravity.CENTER);
        t.setTextSize(16);
        t.setPadding(0, pad, 0, pad);
        root.addView(t);

        aliasBtn = new Button(this);
        aliasBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                IconUpdater.setAliasMode(MainActivity.this, !IconUpdater.aliasMode(MainActivity.this));
                refresh();
            }
        });
        root.addView(aliasBtn);

        Button pin = new Button(this);
        pin.setText("Add changing shortcut to Home screen");
        pin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ShortcutManager sm = getSystemService(ShortcutManager.class);
                if (sm != null && sm.isRequestPinShortcutSupported()) {
                    // make sure the dynamic shortcut exists before asking to pin it
                    IconUpdater.update(MainActivity.this);
                    sm.requestPinShortcut(
                            IconUpdater.shortcut(MainActivity.this, IconUpdater.today()), null);
                } else {
                    Toast.makeText(MainActivity.this, "Home screen does not support this",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        root.addView(pin);

        Button open = new Button(this);
        open.setText("Open calendar");
        open.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, OpenCalendarActivity.class));
            }
        });
        root.addView(open);

        setContentView(root);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IconUpdater.update(this);
        refresh();
    }

    private void refresh() {
        aliasBtn.setText(IconUpdater.aliasMode(this)
                ? "Daily app icon: ON (tap to turn off)"
                : "Turn on daily app icon");
    }
}
