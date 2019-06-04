package ru.bchstudio.ponk.DAO.entities;


import android.content.Context;

import ru.bchstudio.ponk.R;

public class WeatherElement {

    private int id;
    private String main;
    private String description;
    private String icon_day;
    private String icon__night;




    public int getId() {
        return id;
    }

    public String getMain() {
        return main;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon_day() {
        return icon_day;
    }

    public String getIcon__night() {
        return icon__night;
    }

    public int getIcon_day(Context context) throws NoSuchFieldException, IllegalAccessException {
        return getIcon(context, icon_day);
    }

    public int getIcon__night(Context context) throws NoSuchFieldException, IllegalAccessException {
        return getIcon(context, icon__night);
    }




    public void setId(int id) {
        this.id = id;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIcon_day(String icon_day) {
        this.icon_day = icon_day;
    }

    public void setIcon__night(String icon__night) {
        this.icon__night = icon__night;
    }




    public WeatherElement() {
    }

    private int getIcon(Context context, String iconName) throws NoSuchFieldException, IllegalAccessException {
        String nameResource = "ic_"+ iconName;
        return R.drawable.class.getField(nameResource).getInt(context.getResources());
    }

    @Override
    public String toString() {
        return "WeatherElement{" +
                "id=" + id +
                ", main='" + main + '\'' +
                ", description='" + description + '\'' +
                ", icon_day='" + icon_day + '\'' +
                ", icon__night='" + icon__night + '\'' +
                '}';
    }
}
