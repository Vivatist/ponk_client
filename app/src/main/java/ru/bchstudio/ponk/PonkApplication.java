package ru.bchstudio.ponk;

import android.app.Application;

import ru.bchstudio.ponk.business.MainEventDispatcher;

public class PonkApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        //Инициализация Диспетчера
        MainEventDispatcher mainEventDispatcher = new MainEventDispatcher(getApplicationContext());

        //Инициализация базы данных

    }
}
