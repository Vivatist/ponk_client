package ru.bchstudio.ponk.notifi;
import android.annotation.TargetApi;
import android.app.LauncherActivity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import android.graphics.Bitmap;

import ru.bchstudio.ponk.R;

@TargetApi(26)
public class NewApiNotification implements ApiNotification {
    private NotificationCompat.Builder builder;
    private NotificationManager mNotificationManager;

    NewApiNotification(String idChannel, Context context) {

        Intent mainIntent = new Intent(context, LauncherActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mainIntent, 0);

        this.mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = new NotificationChannel(idChannel, context.getString(R.string.app_name), importance);
        // Configure the notification channel.
      //  mChannel.setDescription("CHANNEL TEST ALARM");
       // mChannel.enableLights(true);
       // mChannel.setLightColor(Color.RED);
       // mChannel.enableVibration(true);
       // mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        mNotificationManager.createNotificationChannel(mChannel);

        this.builder = new NotificationCompat.Builder(context, idChannel);
        builder.setContentTitle(context.getString(R.string.app_name))
                .setChannelId(idChannel)
                .setContentIntent(pendingIntent);
    }


    @Override
    public void send(String text, Bitmap icon) {

        Log.e(this.getClass().getSimpleName(),"Выводим сообщение в новом API");

        builder.setSmallIcon(R.drawable.ic_stat_name)
                .setContentText(text);

        mNotificationManager.notify(1, builder.build());

    }


}
