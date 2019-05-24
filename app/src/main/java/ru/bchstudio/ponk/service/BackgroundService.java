package ru.bchstudio.ponk.service;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;


import ru.bchstudio.ponk.Constants;

import ru.bchstudio.ponk.Notification.OfflineServiceNotification;
import ru.bchstudio.ponk.Notification.ServiceNotification;
import ru.bchstudio.ponk.Notification.StandartServiceNotification;
import ru.bchstudio.ponk.WeatherPOJO;
import ru.bchstudio.ponk.web.OnWebAsyncTaskCompleted;
import ru.bchstudio.ponk.R;
import ru.bchstudio.ponk.web.WebAsyncTask;

public class BackgroundService extends Service implements OnWebAsyncTaskCompleted {

    private static final String TAG = WebAsyncTask.class.getName();
    private Thread mainThread;
    public static Intent serviceIntent = null;
    //private ServiceNotification serviceNotification;

    public BackgroundService() {
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {



        serviceIntent = intent;


        if (intent == null) {
            stopForeground(true);
            stopSelf();
            return START_NOT_STICKY;
        }


        ServiceNotification notification = new StandartServiceNotification(this);
        startForeground(notification.getId(), notification.getNotification());

        if (Constants.ENABLED_DEBUG_TWIST) showToast(getApplication(), "Start Foreground Service");

        final OnWebAsyncTaskCompleted lstnr = this;

        //Создаем отдельный поток
        mainThread = new Thread(new Runnable() {
            @Override
            public void run() {

                //Бесконечный цикл
                boolean run = true;
                while (run) {
                    try {


                        new WebAsyncTask(Constants.WEATHER_URL, Constants.HTTP_REQUEST_TIMEOUT, lstnr, getApplicationContext()).execute();
                        Thread.sleep(Constants.WEATHER_REQUEST_INTERVAL);


                    } catch (InterruptedException e) {
                        run = false;
                        e.printStackTrace();
                    }
                }
            }
        });
        mainThread.start(); //Запускаем поток

        return START_NOT_STICKY;
    }



    //Запускаем WatchDog
    protected void setAlarmTimer() {
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.add(Calendar.SECOND, 1);
        Intent intent = new Intent(this, AlarmRecever.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);

        AlarmManager mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mAlarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), sender);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        serviceIntent = null;
        setAlarmTimer();
        Thread.currentThread().interrupt();

        if (mainThread != null) {
            mainThread.interrupt();
            mainThread = null;
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }



    //Пример работы с интерфейсом из потока
    //выводит всплывающее сообщение
    public void showToast(final Application application, final String msg) {
        Handler h = new Handler(application.getMainLooper());
        h.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(application, msg, Toast.LENGTH_LONG).show();
            }
        });
    }





// Происходит, когда от сервера получен ответ
    @Override
    public void onWebAsyncTaskCompleted(String result) {

        Log.d(TAG, "Ответ от сервера " + result);


        if (result != null){

            WeatherPOJO weather = new WeatherPOJO(result);

            ServiceNotification notification = new StandartServiceNotification(this);
            notification.setTemperature(weather.getTemp())
                    .setContentTitle("Ветер " + weather.getWind_spd() + "м/с")
                    .setContentText("Влажность " + weather.getHumidity() + "%")
                    .setUpd_time(weather.getUpd_time())
                    .show();

        } else {
            ServiceNotification notification = new OfflineServiceNotification(this);
            notification.show();


        }


    }

}
