package ru.bchstudio.ponk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyBroadReceiv extends BroadcastReceiver {

    final String TAG = "MyBroadReceiv";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive " + intent.getAction());
        context.startService(new Intent(context, MainService.class));
    }
}
