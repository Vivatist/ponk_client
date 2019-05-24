package ru.bchstudio.ponk.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class RebootRecever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Intent in = new Intent(context, BackgroundService.class);
                context.startForegroundService(in);
            } else {
                Intent in = new Intent(context, BackgroundService.class);
                context.startService(in);
            }
        }

    }
}
