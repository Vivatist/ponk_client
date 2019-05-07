package ru.bchstudio.ponk;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import ru.bchstudio.ponk.notifi.CreatorNotification;

public class MainService extends JobIntentService {


    final String TAG = "MainService";

    public static final int JOB_ID = 0x01;

    //метод для запуска сервиса вручную
    public static void start(Context context) {
        Intent starter = new Intent(context, MainService.class);
        MainService.enqueueWork(context, starter);
    }

    //метод для автозапуска
    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, MainService.class, JOB_ID, work);
    }


    @Override
    protected void onHandleWork(@NonNull Intent intent) {


        new Thread(new Runnable() {
            public void run() {


                for (int i = 1; i<=500; i++) {
                    Log.d(TAG, "i = " + i);

                    //Пример работы с интерфейсом из потока
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    getApplicationContext().getResources().getString(R.string.app_name), Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });


                    Bitmap img = null;
                    CreatorNotification notification = new CreatorNotification("myChnl", getApplicationContext());
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
