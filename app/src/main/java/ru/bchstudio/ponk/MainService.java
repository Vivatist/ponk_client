package ru.bchstudio.ponk;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import ru.bchstudio.ponk.notifi.CreatorNotification;

public class MainService extends Service {

    CreatorNotification notification;

    final String TAG = "MainService";


    public void onCreate() {
        super.onCreate();
        notification = new CreatorNotification("myChnl", getApplicationContext());
        Log.d(TAG, "onCreate");
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        someTask();
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return null;
    }

    void someTask() {
        new Thread(new Runnable() {
            public void run() {
                for (int i = 1; i<=500; i++) {
                    Log.d(TAG, "i = " + i);

                    Bitmap img = null;
                    notification.send("i = " + i, img);

                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                stopSelf();
            }
        }).start();
    }
}
