package ru.bchstudio.ponk.DAO;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.List;

import ru.bchstudio.ponk.DAO.entities.Weather;


public class WeatherDao {
    private static final String TAG = "UserDao";
    private Dao<Weather,Integer> dao;


    public WeatherDao(Context context){
        DatabaseHelper helper = DatabaseHelper.getInstance(context);

        try {
            dao = helper.getDao(Weather.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void updateWeather(Weather weather) {
        try {
            dao.update(weather);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Добавляет элемент в базу. Если элемент с таким ID уже есть, то старый элемент удаляем, новый записываем
    public void addWeather(Weather weather){
        try{

            List<Weather> weatherList = getWeatherById(weather.getId());
            if (weatherList.size() != 0){
                Log.e(TAG,"Удаляем запись с ID "+ weather.getId() +" - : " + weather.toString());
                deleteMultiUser(weatherList);
            }
            Log.e(TAG,"Добавляем запись с ID "+ weather.getId() +" - : " + weather.toString());
            dao.create(weather);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    //возвращает Х последних элементов из базы
    public List<Weather> getXLastRecord(long x)  {

        QueryBuilder<Weather, Integer> builder = dao.queryBuilder();
        builder.limit(x);
        builder.orderBy("id", false);  // true for ascending, false for descending
        List<Weather> query = null;
        try {
            query = dao.query(builder.prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return query;
    }


    //удаляет список элементов из базы
    public void deleteMultiUser(List<Weather> weathers){
        try{
            dao.delete(weathers);
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    //возвращает все записи
    public List<Weather> listAll(){
        try {
            return dao.queryForAll();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //возвращает все элементы с заданным ID
    public List<Weather> getWeatherById(Integer id){
        List<Weather> list = null;
        QueryBuilder<Weather, Integer> queryBuilder = dao.queryBuilder();
        Where<Weather,Integer> where = queryBuilder.where();
        try {
            where.eq("id",id);
            where.prepare();
            list = queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }



}
