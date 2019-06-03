package ru.bchstudio.ponk.notification;

import android.app.Notification;

import ru.bchstudio.ponk.DAO.entities.Weather;

public interface WeatherNotificationInterface {

    public void show();
    public void setWeather(Weather weather);
    public Notification getNotification();
    public int getId();


}
