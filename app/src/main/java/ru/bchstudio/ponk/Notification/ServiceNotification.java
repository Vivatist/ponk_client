package ru.bchstudio.ponk.Notification;

import android.app.Notification;
import android.content.Context;

import java.util.Date;

public interface ServiceNotification {
    Notification getNotification() ;
    int getId();

    ServiceNotification setContentTitle(String contentTitle);
    ServiceNotification setTemperature(int temp);
    ServiceNotification setContentText(String contentText);
    ServiceNotification setUpd_time(Date upd_time);
    ServiceNotification setIcon(int icon);

    void show();
}
