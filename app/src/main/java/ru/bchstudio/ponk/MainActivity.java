package ru.bchstudio.ponk;

import android.app.LauncherActivity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;




public class MainActivity extends AppCompatActivity implements OnWebAsyncTaskCompleted {


    private static final String myURL = "http://89.169.90.244:5000/moscow";
    private static final int NOTIFY_ID = 10 ;
    TextView tvRez;
    TextView tvCounter;
    int queryCounter = 0;
    ImageView iv;
    View view;

    public Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.5f); // round
        int height = (int) (baseline + paint.descent() + 0.5f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvRez = findViewById(R.id.textView2);
        tvCounter = findViewById(R.id.textView);
        iv = findViewById(R.id.imageView);


    }


    @Override
    public void onWebAsyncTaskCompleted(String result) {
        tvRez.setText(result);
        queryCounter += 1;
        tvCounter.setText(String.valueOf(queryCounter));
        // выводим сообщение
        //Toast.makeText(this, "Connect", Toast.LENGTH_SHORT).show();
    }





    public void onMyButtonClick(View view)
    {
        new WebAsyncTask(myURL,this).execute();


        Bitmap img =  textAsBitmap("22", 60, Color.RED);
        iv.setImageBitmap(img);



        Context context = getApplicationContext();
        String idChannel = "my_channel_01";
        Intent mainIntent;

        mainIntent = new Intent(context, LauncherActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mainIntent, 0);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel mChannel = null;
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
