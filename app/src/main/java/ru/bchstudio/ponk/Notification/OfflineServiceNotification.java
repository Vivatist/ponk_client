package ru.bchstudio.ponk.Notification;

import android.annotation.SuppressLint;
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
import ru.bchstudio.ponk.R;


@SuppressLint("Registered")
public class OfflineServiceNotification implements ServiceNotification {

    private static final String CHANNEL_ID = "Channel ID"; //TODO придумать более удачное название
    private static final String CHANNEL_NAME = "Channel name"; //TODO придумать более удачное название
    private static final int NOTIFICATION_ID = 1010;
    private Context context;



    private int icon;
    private String contentTitle = null;
    private String contentText = null;
    private Date upd_time;


    public OfflineServiceNotification(Context context) {
        this.context = context;
        this.upd_time = Calendar.getInstance().getTime();
        this.contentTitle = "Ошибка связи";
        this.contentText = "";
        this.icon = R.drawable.ic_stat_cloud_off;
    }

    @Override
    public OfflineServiceNotification setTemperature(int temperature) {
        this.icon = getIcon(temperature);
        return this;
    }

    @Override
    public OfflineServiceNotification setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
        return this;
    }

    @Override
    public OfflineServiceNotification setContentText(String contentText) {
        this.contentText = contentText;
        return this;
    }

    @Override
    public OfflineServiceNotification setUpd_time(Date upd_time) {
        this.upd_time = upd_time;
        return this;
    }

    @Override
    public OfflineServiceNotification setIcon(int icon){
        this.icon = icon;
        return this;
    }

    @Override
    public void show() {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(getId(), getNotification());
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


    @Override
    public int getId() {
        return NOTIFICATION_ID;
    }

    private int getIcon(int value){

        String nameResource;
        int result;
        try {
            if (value >= 0){
                nameResource = "ic_degrees_"+ value;
                return R.drawable.class.getField(nameResource).getInt(context.getResources());
            } else {
                nameResource = "ic_degrees_minus"+ Math.abs(value);
                result = R.drawable.class.getField(nameResource).getInt(context.getResources());
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
            result =  R.drawable.ic_stat_error_outline;
        } catch (NoSuchFieldException e) {
            result =  R.drawable.ic_stat_error_outline;
        }

        return result;
    }

}
