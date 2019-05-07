package ru.bchstudio.ponk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MyBroadReceiv extends BroadcastReceiver {

    final String TAG = "MyBroadReceiv";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive " + intent.getAction());

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            MainService.enqueueWork(context, new Intent());
        }

//        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
//            //Toast toast = Toast.makeText(context.getApplicationContext(),
//            //        context.getResources().getString(R.string.recieve_start), Toast.LENGTH_LONG);
//            //toast.show();
//
//            context.startService(new Intent(context, MainService.class));
//        }

    }
}