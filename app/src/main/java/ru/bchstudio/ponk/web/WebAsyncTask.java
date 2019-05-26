package ru.bchstudio.ponk.web;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import ru.bchstudio.ponk.R;

public class WebAsyncTask extends AsyncTask<Void, String, String> { //change Object to required type

    private static final String TAG = WebAsyncTask.class.getName();
    private String myURL;
    private Context context;
    private int httpRequestTimeout;


    public WebAsyncTask(String myURL, int httpRequestTimeout, Context context){
        this.myURL = myURL;
        this.httpRequestTimeout = httpRequestTimeout;
        this.context = context;
    }




    //Выполняется автоматом в отдельном потоке
    @Override
    protected String doInBackground(Void... voids) {
        String s = null;
        try {
            WebProtocol p = new HTTP();
            s = p.doGet(this.context, myURL, httpRequestTimeout);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG,"Response string: " + e.toString());
        }
        return s;
    }


    //Выполняется после завершения потока
    @Override
    protected void onPostExecute(final String result) {
        EventBus.getDefault().post(new ResponseCurrentWeatherEvent(result));
    }


}