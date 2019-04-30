package ru.bchstudio.ponk.notifi;

import android.graphics.Bitmap;

public interface ApiNotification {
    void send(String text, Bitmap icon);
}
