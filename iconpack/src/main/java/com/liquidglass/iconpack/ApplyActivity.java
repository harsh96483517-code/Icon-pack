package com.liquidglass.iconpack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/** Invisible entry point so launchers can open the pack. */
public class ApplyActivity extends Activity {
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        try {
            startActivity(new Intent(this, MainActivity.class));
        } catch (Exception ignored) { }
        finish();
    }
}
