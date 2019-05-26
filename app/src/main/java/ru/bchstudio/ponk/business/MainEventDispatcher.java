package ru.bchstudio.ponk.business;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import ru.bchstudio.ponk.Notification.OfflineServiceNotification;
import ru.bchstudio.ponk.Notification.ServiceNotification;
import ru.bchstudio.ponk.Notification.StandartServiceNotification;
import ru.bchstudio.ponk.WeatherPOJO;
import ru.bchstudio.ponk.web.ResponseCurrentWeatherEvent;
import ru.bchstudio.ponk.web.WebAsyncTask;

public class MainEventDispatcher {

    private static final String TAG = WebAsyncTask.class.getName();
    private Context context = null;




    public MainEventDispatcher(Context context) {
        this.context = context;
        EventBus.getDefault().register(this);
    }

    //  EventBus.getDefault().unregister(this); TODO разобраться с вызовом деструктора и отпиской



    @Subscribe
    public void onHttpResponseCurrentWeatherEvent(ResponseCurrentWeatherEvent event) {
        Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show();

        String result = event.message;

        Log.d(TAG, "Ответ от сервера " + result);


        if (result != null){

            WeatherPOJO weather = new WeatherPOJO(result);

            StandartServiceNotification notification = new StandartServiceNotification(context);
            notification.setTemperature(weather.getTemp())
                    .setContentTitle("Ветер " + weather.getWind_spd() + "м/с")
                    .setContentText("Влажность " + weather.getHumidity() + "%")
                    .setUpd_time(weather.getUpd_time())
                    .show();

        } else {
            ServiceNotification notification = new OfflineServiceNotification(context);
            notification.show();


        }
    }
}
