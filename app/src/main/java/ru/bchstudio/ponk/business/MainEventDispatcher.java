package ru.bchstudio.ponk.business;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import ru.bchstudio.ponk.notification.OfflineServiceNotification;
import ru.bchstudio.ponk.notification.ServiceNotification;
import ru.bchstudio.ponk.notification.StandartServiceNotification;
import ru.bchstudio.ponk.DAO.entities.Weather;
import ru.bchstudio.ponk.web.events.ResponseCurrentWeatherEvent;
import ru.bchstudio.ponk.web.events.ResponseTestEvent;
import ru.bchstudio.ponk.web.WebAsyncTask;

public class MainEventDispatcher {

    private static final String TAG = WebAsyncTask.class.getName();
    private Context context;




    public MainEventDispatcher(Context context) {
        this.context = context;
        EventBus.getDefault().register(this);
    }

    //  EventBus.getDefault().unregister(this); TODO разобраться с вызовом деструктора и отпиской



    @Subscribe
    public void onResponseCurrentWeatherEvent(ResponseCurrentWeatherEvent event) {
        Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show();

        String result = event.message;

        Log.d(TAG, "Ответ от сервера " + result);


        if (result != null){

            Weather weather = new Weather(result);

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


    @Subscribe
    public void onResponseTestEvent(ResponseTestEvent event) {
        Toast.makeText(context, "Тестовый запрос. Ответ: " + event.message, Toast.LENGTH_SHORT).show();
    }
}
