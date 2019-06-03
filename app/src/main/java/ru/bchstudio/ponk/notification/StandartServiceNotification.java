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
import java.util.Date;

import ru.bchstudio.ponk.DAO.entities.Weather;
import ru.bchstudio.ponk.MainActivity;



public class StandartServiceNotification extends BaseNotification  implements WeatherNotificationInterface{

    private Weather weather;

    //КОНСТРУКТОР
    public StandartServiceNotification(Context context) {
        super(context);
    }



    private Notification prepareNotification(Context context, Weather weather) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        builder.setSmallIcon(getIcon(weather.getTemp()))
                .setContentTitle("Ветер " + String.valueOf(weather.getWind_spd()) +  "м/с")
                .setContentText("Влажность " + String.valueOf(weather.getHumidity()) + "%")
                .setWhen(weather.getUpd_time().getTime())
                .setContentIntent(pendingIntent)
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
    public Notification getNotification() {
        return prepareNotification( context, weather);
    }



    @Override
    public void setWeather(Weather weather) {
        this.weather = weather;
    }



    @Override
    public void show() {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(getId(), getNotification());
    }


}
