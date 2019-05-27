package ru.bchstudio.ponk;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.Nullable;

import ru.bchstudio.ponk.service.BackgroundService;
import ru.bchstudio.ponk.web.events.ResponseCurrentWeatherEvent;
import ru.bchstudio.ponk.web.events.ResponseTestEvent;
import ru.bchstudio.ponk.web.WebAsyncTask;


public class MainActivity extends AppCompatActivity {


    private static final String TAG = WebAsyncTask.class.getName();
    private Intent serviceIntent;


    TextView tvRez;
    TextView tvCounter;
    int queryCounter = 0;
    EditText editText3;



    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (BackgroundService.serviceIntent == null) {
            serviceIntent = new Intent(this, BackgroundService.class);
            startService(serviceIntent);
        } else {
            serviceIntent = BackgroundService.serviceIntent;
        }

        tvRez = findViewById(R.id.textView2);
        tvCounter = findViewById(R.id.textView);
        editText3 = findViewById(R.id.editText3);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceIntent != null) {
            stopService(serviceIntent);
            serviceIntent = null;
        }
    }




    private Notification prepareNotification(int icon, String contentTitle, String contentText) {
        final String TEST_CHANNEL_ID = "Test Channel ID"; //TODO придумать более удачное название
        final String TEST_CHANNEL_NAME = "Test Channel name"; //TODO придумать более удачное название

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResponseEvent(ResponseCurrentWeatherEvent event) {
       Toast.makeText(getApplicationContext(), event.message, Toast.LENGTH_SHORT).show();
    }



    @Nullable
    private Integer getIcon(int value){

        String nameResource;

        try {
            if (value >= 0){
                nameResource = "ic_degrees_"+ value;
                return R.drawable.class.getField(nameResource).getInt(getResources());
            } else {
                nameResource = "ic_degrees_minus"+ Math.abs(value);
                return R.drawable.class.getField(nameResource).getInt(getResources());
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            return null;
        }

        return null;
    }

    public void onMyButtonClick(View view) {
        new WebAsyncTask(Constants.TEST_URL, Constants.HTTP_REQUEST_TIMEOUT, new ResponseTestEvent()).execute();

    }





}
