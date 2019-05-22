package ru.bchstudio.ponk;

import android.annotation.SuppressLint;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import ru.bchstudio.ponk.web.WebAsyncTask;


//POJO класс для хранения данных о погоде
public class WeatherPOJO {

    private int id; //Номер пакета на сервере
    private String city; //Название города
    private String country; //Идентификатор страны (RU,EN...)
    private boolean online; //Метка источника пакета с сервера (онлайн, либо офлайн)
    private Date upd_time; //Время получения пакета с сервера бесплатного сервера

    private int temp; //Температура
    private int temp_min; //Температура минимальная
    private int temp_max; //Температура максимальная
    private int humidity; //Влажность
    private int pressure; //Давление
    private int wind_spd; //Скорость ветра
    private int wind_deg; //Направление ветра

    private String instruction; //JSON с дополнительным объектом

    private static final String TAG = WebAsyncTask.class.getName();

    public int getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public boolean isOnline() {
        return online;
    }

    public Date getUpd_time() {
        return upd_time;
    }

    public int getTemp() {
        return temp;
    }

    public int getTemp_min() {
        return temp_min;
    }

    public int getTemp_max() {
        return temp_max;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getPressure() {
        return pressure;
    }

    public int getWind_spd() {
        return wind_spd;
    }

    public int getWind_deg() {
        return wind_deg;
    }

    public String getInstruction() {
        return instruction;
    }


    @NotNull
    @Override
    public String toString() {
        return "WeatherPOJO{" +
                "id=" + id +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", online=" + online +
                ", upd_time=" + upd_time +
                ", temp=" + temp +
                ", temp_min=" + temp_min +
                ", temp_max=" + temp_max +
                ", humidity=" + humidity +
                ", pressure=" + pressure +
                ", wind_spd=" + wind_spd +
                ", wind_deg=" + wind_deg +
                ", instruction='" + instruction + '\'' +
                '}';
    }

    private void JsonToFields (String JSON)  {
        try {
            JSONObject myResponse = new JSONObject(JSON);

            this.id = (int) Math.round(myResponse.getDouble("id"));
            this.city = myResponse.getString("city");
            this.country = myResponse.getString("country");
            this.online =  (myResponse.getInt("online") != 0);

            String upd_time = myResponse.getString("upd_time");
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat ft = new SimpleDateFormat ("dd.MM.yyyy HH:mm:ss" );
            this.upd_time = ft.parse(upd_time);

            this.temp = (int) Math.round(myResponse.getDouble("temp"));
            this.temp_min = (int) Math.round(myResponse.getDouble("temp_min"));
            this.temp_max = (int) Math.round(myResponse.getDouble("temp_max"));

            this.humidity = myResponse.getInt("humidity");
            this.pressure = myResponse.getInt("pressure");

            this.wind_spd = (int) Math.round(myResponse.getDouble("wind_spd"));
            this.wind_deg = (int) Math.round(myResponse.getDouble("wind_deg"));

            this.instruction = myResponse.getString("instruction");

            Log.d(TAG, "Успешно создан POJO-объект: " + this.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public WeatherPOJO(String JSON) {

        JsonToFields(JSON);
    }

}