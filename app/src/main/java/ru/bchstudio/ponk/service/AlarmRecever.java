package ru.bchstudio.ponk.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class AlarmRecever extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent in = new Intent(context, RestartService.class);
            context.startForegroundService(in);
        } else {
            Intent in = new Intent(context, RealService.class);
            context.startService(in);
        }


        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            //TODO чтобы включить запуск сервиса - раскомментировать
            // MainService.enqueueWork(context, new Intent());
        }
    }

}