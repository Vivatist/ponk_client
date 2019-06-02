package ru.bchstudio.ponk;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.TextView;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import ru.bchstudio.ponk.DAO.WeatherCollection;
import ru.bchstudio.ponk.DAO.WeatherDao;
import ru.bchstudio.ponk.DAO.entities.User;
import ru.bchstudio.ponk.DAO.UserDao;
import ru.bchstudio.ponk.DAO.entities.Weather;
import ru.bchstudio.ponk.notification.CustomServiceNotification;
import ru.bchstudio.ponk.service.BackgroundService;
import ru.bchstudio.ponk.web.events.ResponseTestEvent;
import ru.bchstudio.ponk.web.WebAsyncTask;


public class MainActivity extends AppCompatActivity {


    private static final String TAG = WebAsyncTask.class.getName();
    private Intent serviceIntent;


    TextView tvRez;
    TextView tvCounter;
    int queryCounter = 0;
    EditText editText3;
    private View view;


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



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResponseEvent(ResponseTestEvent event) throws IOException, XmlPullParserException {

        String result = event.message;
        if (result != null){
            WeatherDao weatherDao = new WeatherDao(this);
            Weather weather = new Weather(event.message);

            weatherDao.addWeather(weather);


        }
    }



    public void onMyButtonClick(View view) {
        new WebAsyncTask(Constants.WEATHER_URL, Constants.HTTP_REQUEST_TIMEOUT, new ResponseTestEvent()).execute();


        UserDao userDao = new UserDao(this);
        User user = new User();
        user.setName("Юзер 1");
        user.setDesc("й1");

        userDao.addUser(user);
        userDao.addUser(new User("Юзер 2","й2"));
        userDao.addUser(new User("Юзер 3","й3"));




        List <User> users = userDao.getUsersByName("Юзер 1");
        userDao.deleteMulUser(users);

        List<User> list = userDao.listAll();
        Log.e(TAG, "Все юзеры: "+list.toString() );
    }

    public void onMyButton2Click(View view) {

        WeatherDao weatherDao = new WeatherDao(this);
        List<Weather> weatherList = weatherDao.listAll();
        for (Weather w: weatherList){
            Log.e(TAG,"!!! - : " + w.toString());
        }
    }



    public void CustomNotification() {
        // Using RemoteViews to bind custom layouts into Notification
        RemoteViews remoteViews = new RemoteViews(getPackageName(),
                R.layout.customnotification);

        // Set Notification Title
        String strtitle = getString(R.string.customnotificationtitle);
        // Set Notification Text
        String strtext = getString(R.string.customnotificationtext);

        // Open NotificationView Class on Notification Click
        Intent intent = new Intent(this, MainActivity.class);
        // Send data to NotificationView Class
        intent.putExtra("title", strtitle);
        intent.putExtra("text", strtext);
        // Open NotificationView.java Activity
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                // Set Icon
                .setSmallIcon(R.drawable.ic_stat_cloud_done)
                // Set Ticker Message
                .setTicker(getString(R.string.customnotificationticker))
                // Dismiss Notification
                .setAutoCancel(true)
                // Set PendingIntent into Notification
                .setContentIntent(pIntent)
                // Set RemoteViews into Notification
                .setContent(remoteViews);

        // Locate and set the Image into customnotificationtext.xml ImageViews
        remoteViews.setImageViewResource(R.id.imagenotileft,R.drawable.ic_launcher_background);
        remoteViews.setImageViewResource(R.id.imagenotiright,R.drawable.ic_launcher_background);

        // Locate and set the Text into customnotificationtext.xml TextViews
        remoteViews.setTextViewText(R.id.title,getString(R.string.customnotificationtitle));
        remoteViews.setTextViewText(R.id.text,getString(R.string.customnotificationtext));

        // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Build Notification with Notification Manager
        notificationmanager.notify(0, builder.build());

    }


    public void onMyButton3Click(View view) {

        WeatherCollection weatherCollection = new WeatherCollection(getApplicationContext(), R.xml.weather_codes);

        Log.e(TAG, weatherCollection.getElementById(200).toString());

       // CustomNotification();

        CustomServiceNotification notification = new CustomServiceNotification(getApplicationContext());
        notification.setTemperature(-33)
                .setContentTitle("Ветер " + "1000" + "м/с")
                .setContentText("Влажность " + "99" + "%")
                .setUpd_time(new Date())
                .show();



//        WeatherDao weatherDao = new WeatherDao(this);
//
//
//        List<Weather> list = getWeatherById(weather.getId());
//        deleteMultiUser(list);
//
//
//        List<Weather> weatherCollection = weatherDao.getWeatherById(4200);
//        for (Weather w: weatherCollection){
//            Log.e(TAG,"&&& - : " + w.toString());
//        }
    }





}
