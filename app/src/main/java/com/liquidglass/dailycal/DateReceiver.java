package com.liquidglass.dailycal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        IconUpdater.update(context);
    }
}
