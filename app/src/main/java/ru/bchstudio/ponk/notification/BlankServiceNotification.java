package ru.bchstudio.ponk.notification;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import ru.bchstudio.ponk.DAO.WeatherCollection;
import ru.bchstudio.ponk.DAO.entities.Weather;
import ru.bchstudio.ponk.DAO.entities.WeatherElement;
import ru.bchstudio.ponk.MainActivity;
import ru.bchstudio.ponk.R;


public class BlankServiceNotification extends BaseNotification implements WeatherNotificationInterface {


    Weather weather;

    //КОНСТРУКТОР
    public BlankServiceNotification(Context context) {
        super(context);
    }



    @Override
    public Notification getNotification() throws NoSuchFieldException, IllegalAccessException {
        return prepareNotification( context,  weather);
    }

    //TODO API 23 - ошибка, 24 - ошибка, 25 - пустая панель, 26 - норм, 27 - норм, 28 - норм
    private Notification prepareNotification(Context context, Weather weather) throws NoSuchFieldException, IllegalAccessException {



        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_weather_layout_api26plus);


        WeatherCollection weatherCollection = new WeatherCollection(context, R.xml.weather_codes_ru);
        WeatherElement weatherElement = weatherCollection.getElementById(weather.getWeather_id());

        int icon_night = weatherElement.getIcon__night(context);
        int icon_day = weatherElement.getIcon_day(context);

        remoteViews.setImageViewResource(R.id.nwl26p_imageview_temperature, icon_day );

        String prefix = "";
        if (weather.getTemp() > 0) prefix = "+";

        remoteViews.setTextViewText(R.id.nwl26p_text_view_temp, prefix + String.valueOf(weather.getTemp())+ "\u00B0");
        remoteViews.setTextViewText(R.id.nwl26p_text_view_humidity, "Влажность " + String.valueOf(weather.getHumidity()) + "%");
        remoteViews.setTextViewText(R.id.nwl26p_text_view_wind_spd, "Ветер " + String.valueOf(weather.getWind_spd()) + " м/с");
        remoteViews.setTextViewText(R.id.nwl26p_text_view_pressure, "Давление " + String.valueOf(weather.getPressure()) + " мм.р.с.");

        remoteViews.setTextViewText(R.id.nwl26p_text_view_description, weatherElement.getDescription());
        remoteViews.setOnClickPendingIntent(R.id.nwl26p_root, pendingIntent);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "3");


        builder
                .setSmallIcon(getIcon(weather.getTemp()))
                .setCustomContentView(remoteViews)
               // .setContent(remoteViews)
              //  .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setContentIntent(pendingIntent)
              //  .setColor(context.getColor(R.color.colorPrimaryDark))
                .setSound(null);
//
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel("3", "CHANNEL_NAME", NotificationManager.IMPORTANCE_LOW);
            notificationChannel.setSound(null, null);
            notificationChannel.setShowBadge(false);
            manager.createNotificationChannel(notificationChannel);
        }

        return builder.build();

    }




    public void show() throws NoSuchFieldException, IllegalAccessException {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(getId(), getNotification());
    }

    @Override
    public void setWeather(Weather weather) {
        this.weather = weather;
    }


}
