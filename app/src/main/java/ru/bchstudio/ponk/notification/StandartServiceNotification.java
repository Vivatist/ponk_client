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

import ru.bchstudio.ponk.MainActivity;



public class StandartServiceNotification extends ServiceNotification {


    private int icon;
    private String contentTitle = null;
    private String contentText = null;
    private Date upd_time;
    private int weather_id = 0;

    //КОНСТРУКТОР
    public StandartServiceNotification(Context context) {
        super(context);
        this.upd_time = Calendar.getInstance().getTime();
    }


    public StandartServiceNotification setTemperature(int temperature) {
        this.icon = super.getIcon(temperature);
        return this;
    }


    public StandartServiceNotification setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
        return this;
    }


    public StandartServiceNotification setContentText(String contentText) {
        this.contentText = contentText;
        return this;
    }

    public StandartServiceNotification setUpd_time(Date upd_time) {
        this.upd_time = upd_time;
        return this;
    }

    public StandartServiceNotification setWeather_id(int weather_id) {
        this.weather_id = weather_id;
        return this;
    }



    @Override
    public Notification getNotification() {
        return prepareNotification( context,  icon,  contentTitle,  contentText,  upd_time);
    }


    private Notification prepareNotification(Context context, int icon, String contentTitle, String contentText, Date upd_time) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        builder.setSmallIcon(icon)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setContentIntent(pendingIntent)
                .setWhen(upd_time.getTime())
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






}
