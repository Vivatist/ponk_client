package ru.bchstudio.ponk.notification;


import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import java.util.Calendar;
import java.util.Date;

import ru.bchstudio.ponk.MainActivity;
import ru.bchstudio.ponk.PonkApplication;
import ru.bchstudio.ponk.R;


public class CustomServiceNotification extends ServiceNotification {


    private int icon;
    private String contentTitle = null;
    private String contentText = null;
    private Date upd_time;
    private int weather_id = 0;

    //КОНСТРУКТОР
    public CustomServiceNotification(Context context) {
        super(context);
        this.upd_time = Calendar.getInstance().getTime();
    }


    public CustomServiceNotification setTemperature(int temperature) {
        this.icon = super.getIcon(temperature);
        return this;
    }


    public CustomServiceNotification setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
        return this;
    }


    public CustomServiceNotification setContentText(String contentText) {
        this.contentText = contentText;
        return this;
    }

    public CustomServiceNotification setUpd_time(Date upd_time) {
        this.upd_time = upd_time;
        return this;
    }

    public CustomServiceNotification setWeather_id(int weather_id) {
        this.weather_id = weather_id;
        return this;
    }



    @Override
    public Notification getNotification() {
        return prepareNotification( context,  icon,  contentTitle,  contentText,  upd_time);
    }


    private Notification prepareNotification(Context context, int icon, String contentTitle, String contentText, Date upd_time) {

//        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
//                R.layout.notification);
//
//        // Set Notification Title
//        String strtitle = context.getString(R.string.customnotificationtitle);
//        // Set Notification Text
//        String strtext = context.getString(R.string.customnotificationtext);
//
//        // Open NotificationView Class on Notification Click
//        Intent intent = new Intent(context, MainActivity.class);
//        // Send data to NotificationView Class
//        intent.putExtra("title", strtitle);
//        intent.putExtra("text", strtext);
//        // Open NotificationView.java Activity
//        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
//                // Set Icon
//                .setSmallIcon(R.drawable.ic_stat_cloud_done)
//                // Set Ticker Message
//                .setTicker(context.getString(R.string.customnotificationticker))
//                // Dismiss Notification
//                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
//           //     .setAutoCancel(true)
//                // Set PendingIntent into Notification
//                .setContentIntent(pIntent)
//                // Set RemoteViews into Notification
//                .setContent(remoteViews);
//
//        // Locate and set the Image into customnotificationtext.xml ImageViews
//        remoteViews.setImageViewResource(R.id.imagenotileft,R.drawable.ic_launcher_background);
//        remoteViews.setImageViewResource(R.id.imagenotiright,R.drawable.ic_launcher_background);
//
//        // Locate and set the Text into customnotificationtext.xml TextViews
//        remoteViews.setTextViewText(R.id.title,context.getString(R.string.customnotificationtitle));
//        remoteViews.setTextViewText(R.id.text,context.getString(R.string.customnotificationtext));

        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
        remoteViews.setTextViewText(R.id.textView3, "-40");
        remoteViews.setTextViewText(R.id.textView4, "Влажность " + contentText);
        remoteViews.setTextViewText(R.id.textView5, "Ветер " + contentTitle);
        remoteViews.setOnClickPendingIntent(R.id.root_layout, pendingIntent);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "3");


        builder
                .setSmallIcon(icon)
                .setContent(remoteViews)
              //  .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
             //   .setContentIntent(pendingIntent)
              //  .setColor(context.getColor(R.color.colorPrimaryDark))
                .setSound(null);
//
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel("3", "CHANNEL_NAME", NotificationManager.IMPORTANCE_LOW);
            notificationChannel.setSound(null, null);
            notificationChannel.setShowBadge(false);
            manager.createNotificationChannel(notificationChannel);
        }

        return builder.build();

    }






}
