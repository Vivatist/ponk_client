package ru.bchstudio.ponk.notifi;

import android.app.LauncherActivity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import ru.bchstudio.ponk.R;

public class OldApiNotification implements ApiNotification {


    private NotificationCompat.Builder builder;
    private NotificationManager mNotificationManager;

    OldApiNotification(String idChannel, Context context) {

        Intent mainIntent = new Intent(context, LauncherActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mainIntent, 0);

        mNotificationManager  = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        builder = new NotificationCompat.Builder(context, idChannel);

        builder.setContentTitle(context.getString(R.string.app_name))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                .setVibrate(new long[]{100, 250})
                .setLights(Color.YELLOW, 500, 5000)
                .setAutoCancel(true).setChannelId(idChannel)
                .setContentIntent(pendingIntent);

    }

    @Override
    public void send(String text, Bitmap icon) {
        Log.e(this.getClass().getSimpleName(),"Выводим сообщение в старом API");

        builder.setSmallIcon(R.drawable.ic_stat_name)
                .setContentText(text);

        mNotificationManager.notify(1, builder.build());
    }
}
