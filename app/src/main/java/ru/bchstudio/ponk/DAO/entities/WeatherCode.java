package ru.bchstudio.ponk.DAO.entities;

import android.content.Context;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import ru.bchstudio.ponk.R;

public class WeatherCode {

    private int id;
    private String main;
    private String description;
    private String icon;

    public int getId() {
        return id;
    }

    public String getMain() {
        return main;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

    public WeatherCode(Context context, int id) throws XmlPullParserException, IOException {
        this.id = id;

        final String  TAG = "id200";

        XmlPullParser parser = context.getResources().getXml(R.xml.weather_codes);
        while(parser.getEventType() != XmlPullParser.END_DOCUMENT)
        {
            if(parser.getEventType() == XmlPullParser.START_TAG&& parser.getName() == TAG)
            {
                parser.next();




            } else if(parser.getEventType() == XmlPullParser.END_TAG && parser.getName() == TAG){
                languages.add(lang);
            }
            parser.next();
        }
    }

    @Override
    public String toString() {
        return "WeatherCode{" +
                "id=" + id +
                ", main='" + main + '\'' +
                ", description='" + description + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}
