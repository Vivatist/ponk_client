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
import ru.bchstudio.ponk.R;



public class OfflineServiceNotification extends ServiceNotification {



    private int icon;
    private String contentTitle;
    private String contentText;
    private Date upd_time;


    public OfflineServiceNotification(Context context) {
        super(context);
        this.context = context;
        this.upd_time = Calendar.getInstance().getTime();
        this.contentTitle = "Ошибка связи";
        this.contentText = "";
        this.icon = R.drawable.ic_stat_cloud_off;
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


}
