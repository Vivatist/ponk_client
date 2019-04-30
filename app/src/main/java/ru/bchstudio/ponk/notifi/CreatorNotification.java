package ru.bchstudio.ponk.notifi;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;

public class CreatorNotification {

    private ApiNotification myNotify;


    public CreatorNotification(String idChannel, Context context) {

         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
             myNotify = new NewApiNotification(idChannel, context);
         } else {
            myNotify = new OldApiNotification(idChannel, context);
         }

    }

    public void send(String text, Bitmap icon){


        myNotify.send(text, icon);




    }
}
