package ru.bchstudio.ponk;

import android.app.LauncherActivity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

public class Notification {

    private Context context;
    private String idChannel;


    public Notification(String idChannel, Context context) {
         this.context = context;
         this.idChannel = idChannel;

    }

    public void send(String text, Bitmap icon){



        Intent mainIntent;

        mainIntent = new Intent(context, LauncherActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mainIntent, 0);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel mChannel;
        // The id of the channel.


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, null);
        builder.setContentTitle(context.getString(R.string.app_name))
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentIntent(pendingIntent)
                .setContentText("TEST ALARM!");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            mChannel = new NotificationChannel(idChannel, context.getString(R.string.app_name), importance);
            // Configure the notification channel.
            mChannel.setDescription("CHANNEL TEST ALARM");
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);
        } else {
            builder.setContentTitle(context.getString(R.string.app_name))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                    .setVibrate(new long[]{100, 250})
                    .setLights(Color.YELLOW, 500, 5000)
                    .setAutoCancel(true);
        }
        builder.setChannelId(idChannel);
        mNotificationManager.notify(1, builder.build());

    }
}
