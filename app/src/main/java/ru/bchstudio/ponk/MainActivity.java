package ru.bchstudio.ponk;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import ru.bchstudio.ponk.notifi.CreatorNotification;
import ru.bchstudio.ponk.MainService;
import ru.bchstudio.ponk.service.RealService;


public class MainActivity extends AppCompatActivity implements OnWebAsyncTaskCompleted {

    private static final String TEST_CHANNEL_ID = "Test Channel ID"; //TODO придумать более удачное название
    private static final String TEST_CHANNEL_NAME = "Test Channel name"; //TODO придумать более удачное название

    private Intent serviceIntent;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (RealService.serviceIntent == null) {
            serviceIntent = new Intent(this, RealService.class);
            startService(serviceIntent);
        } else {
            serviceIntent = RealService.serviceIntent;//getInstance().getApplication();
            Toast.makeText(getApplicationContext(), "already", Toast.LENGTH_LONG).show();
        }

        tvRez = findViewById(R.id.textView2);
        tvCounter = findViewById(R.id.textView);
        iv = findViewById(R.id.imageView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceIntent != null) {
            stopService(serviceIntent);
            serviceIntent = null;
        }
    }


    TextView tvRez;
    TextView tvCounter;
    int queryCounter = 0;
    ImageView iv;


    private Notification prepareNotification(int icon, String contentTitle, String contentText) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, TEST_CHANNEL_ID);
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        builder.setSmallIcon(icon)
                .setContentTitle(contentTitle)
                .setContentText(contentTitle)
                .setContentIntent(pendingIntent)
                .setSound(null);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel(TEST_CHANNEL_ID, TEST_CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
            notificationChannel.setSound(null, null);
            notificationChannel.setShowBadge(false);
            manager.createNotificationChannel(notificationChannel);
        }

        return builder.build();

    }

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
    public void onWebAsyncTaskCompleted(String result) {
        tvRez.setText(result);
        queryCounter += 1;
        tvCounter.setText(String.valueOf(queryCounter));

        Notification nitifi = prepareNotification((R.drawable.ic_degrees_minus17), "TestTitle", "TestText");
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, nitifi);
    }


    public void onMyButtonClick(View view) {
        new WebAsyncTask(Constants.TEST_URL, Constants.HTTP_REQUEST_TIMEOUT, this, getApplicationContext()  ).execute();


        //ПРИМЕР ОТПРАВКИ СООБЩЕНИЯ
        // Bitmap img =  textAsBitmap("22", 60, Color.RED);
        // iv.setImageBitmap(img);
        // CreatorNotification notification = new CreatorNotification("myChnl", getApplicationContext());
        // notification.send("Test", img);

    }


    public void onStartClick(View view) {
        MainService.start(getApplicationContext());
    }

    public void onStopClick(View view) {


    }


}
