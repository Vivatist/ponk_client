package ru.bchstudio.ponk.service;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import java.util.Calendar;


import ru.bchstudio.ponk.Constants;

import ru.bchstudio.ponk.notification.OfflineServiceNotification;
import ru.bchstudio.ponk.notification.BaseNotification;
import ru.bchstudio.ponk.notification.WeatherNotificationInterface;
import ru.bchstudio.ponk.web.events.ResponseCurrentWeatherEvent;
import ru.bchstudio.ponk.web.WebAsyncTask;

public class BackgroundService extends Service {


    private Thread mainThread;
    public static Intent serviceIntent = null;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {




        serviceIntent = intent;
        if (serviceIntent == null) {
            stopForeground(true);
            stopSelf();
            return START_NOT_STICKY;
        }


        WeatherNotificationInterface notification = new OfflineServiceNotification(this);
        try {
            startForeground(notification.getId(), notification.getNotification());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if (Constants.ENABLED_DEBUG_TWIST) showToast(getApplication(), "Start Foreground Service");


        //Создаем отдельный поток
        mainThread = new Thread(new Runnable() {
            @Override
            public void run() {

                //Бесконечный цикл
                boolean run = true;
                while (run) {
                    try {


                        new WebAsyncTask(Constants.WEATHER_URL, Constants.HTTP_REQUEST_TIMEOUT, new ResponseCurrentWeatherEvent()).execute();
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



}
