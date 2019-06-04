package ru.bchstudio.ponk.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import ru.bchstudio.ponk.R;

public abstract class BaseNotification {

    static final String CHANNEL_ID = "Channel ID"; //TODO придумать более удачное название
    static final String CHANNEL_NAME = "Channel name"; //TODO придумать более удачное название
    static final int NOTIFICATION_ID = 1010;
    Context context;



    BaseNotification(Context context){
       this.context = context;
    }



    public int getId() {
        return NOTIFICATION_ID;
    }


    public abstract Notification getNotification() throws NoSuchFieldException, IllegalAccessException;


    int getIcon(int value){

        String nameResource;
        int result;
        try {
            if (value >= 0){
                nameResource = "ic_degrees_"+ value;
                return R.drawable.class.getField(nameResource).getInt(context.getResources());
            } else {
                nameResource = "ic_degrees_minus"+ Math.abs(value);
                result = R.drawable.class.getField(nameResource).getInt(context.getResources());
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
            result =  R.drawable.ic_stat_error_outline;
        } catch (NoSuchFieldException e) {
            result =  R.drawable.ic_stat_error_outline;
        }

        return result;
    }



}
