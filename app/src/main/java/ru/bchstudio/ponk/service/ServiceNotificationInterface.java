package ru.bchstudio.ponk.service;

import android.app.Notification;
import android.content.Context;

import java.util.Date;

public interface ServiceNotificationInterface {
    Notification getNotification() ;
    int getId();
    void setContentTitle(String contentTitle);
    void setTemperature(int temp);
    void setContentText(String contentText);
    void setUpd_time(Date upd_time);
    void setIcon(int icon);
}
