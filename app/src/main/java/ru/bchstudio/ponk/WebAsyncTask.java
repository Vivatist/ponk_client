package ru.bchstudio.ponk;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebAsyncTask extends AsyncTask<Void, String, String> { //change Object to required type

    private static final String TAG = WebAsyncTask.class.getName();
    private OnWebAsyncTaskCompleted listener;
    private String myURL;
    private int httpRequestTimeout;

    public WebAsyncTask(String myURL, int httpRequestTimeout,OnWebAsyncTaskCompleted listener){
        this.listener=listener;
        this.myURL = myURL;
        this.httpRequestTimeout = httpRequestTimeout;
    }


    @org.jetbrains.annotations.NotNull
    private static String doGet(String url, int httpRequestTimeout)
            throws Exception {

        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

        //add reuqest header
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0" );
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        connection.setRequestProperty("Content-Type", "application/json");

        connection.setConnectTimeout(httpRequestTimeout);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = bufferedReader.readLine()) != null) {
            response.append(inputLine);
        }
        bufferedReader.close();

        Log.d(TAG,"Response string: " + response.toString());
        return response.toString();
    }


    //Выполняется автоматом в отдельном потоке
    @Override
    protected String doInBackground(Void... voids) {
        String s = null;
        try {
            s = doGet(myURL, httpRequestTimeout);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG,"Response string: " + e.toString());
        }
        return s;
    }


    //Выполняется после завершения потока
    @Override
    protected void onPostExecute(final String result) {
        listener.onWebAsyncTaskCompleted(result);
    }


}