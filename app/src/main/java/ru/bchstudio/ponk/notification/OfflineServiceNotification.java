package ru.bchstudio.ponk.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import java.util.Calendar;


import ru.bchstudio.ponk.DAO.entities.Weather;
import ru.bchstudio.ponk.MainActivity;
import ru.bchstudio.ponk.R;



public class OfflineServiceNotification extends BaseNotification implements  WeatherNotificationInterface{



    private Weather weather;


    public OfflineServiceNotification(Context context) {
        super(context);
    }

    @Override
    public Notification getNotification() {
        return prepareNotification( context);
    }



    private Notification prepareNotification(Context context) {


        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_stat_cloud_off)
                .setContentTitle("Ошибка связи")
                .setContentText("")
                .setContentIntent(pendingIntent)
                .setWhen(Calendar.getInstance().getTime().getTime())
                .setSound(null);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
            notificationChannel.setSound(null, null);
            notificationChannel.setShowBadge(false);
            manager.createNotificationChannel(notificationChannel);
        }

        return builder.build();

    }


    @Override
    public void show() {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(getId(), getNotification());
    }

    @Override
    public void setWeather(Weather weather) {
        this.weather = weather;
    }
}
