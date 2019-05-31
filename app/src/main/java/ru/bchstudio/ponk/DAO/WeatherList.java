package ru.bchstudio.ponk.DAO;

import android.content.Context;

import org.jetbrains.annotations.NotNull;
import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;

import ru.bchstudio.ponk.DAO.entities.WeatherElement;


public class WeatherList {

    private List<WeatherElement> elements;


    public List<WeatherElement> getElements() {
        return elements;
    }



    private List<WeatherElement> parse(Context context, int XMLfile) {

        final String ITEM = "item";
        final String ID = "id";
        final String MAIN = "main";
        final String DESCRIPTION = "description";
        final String ICON_DAY = "icon_d";
        final String ICON_NIGHT = "icon_n";


        List<WeatherElement> wListElements;
        XmlPullParser parser = context.getResources().getXml(XMLfile);
        try {
            int eventType = parser.getEventType();
            WeatherElement currentElement = null;
            boolean done = false;
            wListElements = new ArrayList<>();
            while (eventType != XmlPullParser.END_DOCUMENT && !done) {

                String name;
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase(ITEM)) {
                            currentElement = new WeatherElement();
                        } else if (currentElement != null) {
                            if (name.equalsIgnoreCase(ID)) {
                                currentElement.setId(Integer.valueOf(parser.nextText()));
                            } else if (name.equalsIgnoreCase(DESCRIPTION)) {
                                currentElement.setDescription(parser.nextText());
                            } else if (name.equalsIgnoreCase(MAIN)) {
                                currentElement.setMain(parser.nextText());
                            } else if (name.equalsIgnoreCase(ICON_DAY)) {
                                currentElement.setIcon_day(parser.nextText());
                            }else if (name.equalsIgnoreCase(ICON_NIGHT)) {
                                currentElement.setIcon__night(parser.nextText());
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase(ITEM) &&
                                currentElement != null) {
                            wListElements.add(currentElement);
                        } else if (name.equalsIgnoreCase("resource")) {
                            done = true;
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return wListElements;
    }

    public WeatherList(Context context, int XMLfile) {


        this.elements = parse(context, XMLfile);

    }


    @NotNull
    public String toString(){
        return elements.toString();
    }


    public WeatherElement getElementById(int id){

        return
    }


}
