package ru.bchstudio.ponk.service;

import android.app.AlarmManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import ru.bchstudio.ponk.Constants;
import ru.bchstudio.ponk.MainActivity;
import ru.bchstudio.ponk.WeatherPOJO;
import ru.bchstudio.ponk.web.OnWebAsyncTaskCompleted;
import ru.bchstudio.ponk.R;
import ru.bchstudio.ponk.web.WebAsyncTask;

public class BackgroundService extends Service implements OnWebAsyncTaskCompleted {

    private static final String TAG = WebAsyncTask.class.getName();
    private Thread mainThread;
    public static Intent serviceIntent = null;
    private static final String CHANNEL_ID = "Channel ID"; //TODO придумать более удачное название
    private static final String CHANNEL_NAME = "Channel name"; //TODO придумать более удачное название
    private static final int NOTIFICATION_ID = 1010;


    public BackgroundService() {
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
    public int onStartCommand(Intent intent, int flags, int startId) {


        serviceIntent = intent;


        if (intent == null) {
            stopForeground(true);
            stopSelf();
            return START_NOT_STICKY;
        }

        startForeground(NOTIFICATION_ID, prepareNotification(R.drawable.ic_stat_cloud_done, "MyTitleDone", "MyTextDone", Calendar.getInstance().getTime()));

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


    private Notification prepareNotification(int icon, String contentTitle, String contentText, Date upd_time) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        builder.setSmallIcon(icon)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setContentIntent(pendingIntent)
                .setWhen(upd_time.getTime())
                .setSound(null);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
            notificationChannel.setSound(null, null);
            notificationChannel.setShowBadge(false);
            manager.createNotificationChannel(notificationChannel);
        }

        return builder.build();

    }



    private int getIcon(int value){

        String nameResource;
        int result;
        try {
            if (value >= 0){
                nameResource = "ic_degrees_"+ value;
                return R.drawable.class.getField(nameResource).getInt(getResources());
            } else {
                nameResource = "ic_degrees_minus"+ Math.abs(value);
                result = R.drawable.class.getField(nameResource).getInt(getResources());
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
            result =  R.drawable.ic_stat_error_outline;
        } catch (NoSuchFieldException e) {
            result =  R.drawable.ic_stat_error_outline;
        }

        return result;
    }




// Происходит, когда от сервера получен ответ
    @Override
    public void onWebAsyncTaskCompleted(String result) {

        Log.d(TAG, "Ответ от сервера " + result);
        Notification notification;

        if (result != null){
            if (Constants.ENABLED_DEBUG_TWIST) showToast(getApplication(), result);

            WeatherPOJO weather = new WeatherPOJO(result);
            notification = prepareNotification(getIcon(weather.getTemp()), "Ветер " + weather.getWind_spd() + "м/с", "Влажность " + weather.getHumidity() + "%", weather.getUpd_time());

        } else {
            if (Constants.ENABLED_DEBUG_TWIST) showToast(getApplication(), "Ошибка соединения");
            notification = prepareNotification(R.drawable.ic_stat_cloud_off, "Нет связи", "", Calendar.getInstance().getTime());

        }

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFICATION_ID, notification);



    }
}
