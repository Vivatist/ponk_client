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

    private Context context;
    private String idChannel;

    OldApiNotification(String idChannel, Context context) {
        this.context = context;
        this.idChannel = idChannel;
    }

    @Override
    public void send(String text, Bitmap icon) {
        Log.e(this.getClass().getSimpleName(),"Выводим сообщение в старом API");

        Intent mainIntent;

        mainIntent = new Intent(context, LauncherActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mainIntent, 0);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, idChannel);
        builder.setContentTitle(context.getString(R.string.app_name))
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentIntent(pendingIntent)
                .setContentText("TEST ALARM!");


        builder.setContentTitle(context.getString(R.string.app_name))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                .setVibrate(new long[]{100, 250})
                .setLights(Color.YELLOW, 500, 5000)
                .setAutoCancel(true);


        builder.setChannelId(idChannel);


        mNotificationManager.notify(1, builder.build());
    }
}
