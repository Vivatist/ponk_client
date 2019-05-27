package ru.bchstudio.ponk.web;

import android.os.AsyncTask;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import ru.bchstudio.ponk.web.events.HttpResponse;
import ru.bchstudio.ponk.web.protocol.HTTP;
import ru.bchstudio.ponk.web.protocol.WebProtocol;

public class WebAsyncTask extends AsyncTask<Void, String, String> { //change Object to required type

    private static final String TAG = WebAsyncTask.class.getName();
    private String myURL;
    private int httpRequestTimeout;
    private HttpResponse listener;



    //КОНСТРУКТОР
    public WebAsyncTask(String myURL, int httpRequestTimeout, HttpResponse listener){
        this.myURL = myURL;
        this.httpRequestTimeout = httpRequestTimeout;
        this.listener = listener;
    }


    //Выполняется автоматом в отдельном потоке
    @Override
    protected String doInBackground(Void... voids) {
        String s = null;
        try {
            WebProtocol p = new HTTP();
            s = p.doGet(myURL, httpRequestTimeout);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG,"Response string: " + e.toString());
        }
        return s;
    }


    //Выполняется после завершения потока
    @Override
    protected void onPostExecute(final String result) {
      //  EventBus.getDefault().post(new ResponseCurrentWeatherEvent(result));

        listener.setResult(result);
        EventBus.getDefault().post(listener);
    }


}