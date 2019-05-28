package ru.bchstudio.ponk.DAO;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.List;

import ru.bchstudio.ponk.DAO.entities.User;
import ru.bchstudio.ponk.DAO.entities.Weather;


public class WeatherDao {
    private static final String TAG = "UserDao";
    private Dao<Weather,Integer> weatherDao;


    public WeatherDao(Context context){
        DatabaseHelper helper = DatabaseHelper.getInstance(context);

        try {
            weatherDao = helper.getDao(Weather.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addWeather(Weather weather){
        try{
            weatherDao.create(weather);
            Log.d(TAG, "В базу добавлен:  " + weather.toString());
        }catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "addWeather: "+e.getMessage());
        }
    }


}
