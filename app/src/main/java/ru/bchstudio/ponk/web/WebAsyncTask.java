package ru.bchstudio.ponk.web;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
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
    private OnWebAsyncTaskCompleted listener;
    private String myURL;
    private Context context;
    private int httpRequestTimeout;

    public WebAsyncTask(String myURL, int httpRequestTimeout,OnWebAsyncTaskCompleted listener, Context context){
        this.listener=listener;
        this.myURL = myURL;
        this.httpRequestTimeout = httpRequestTimeout;
        this.context = context;
    }


    @org.jetbrains.annotations.NotNull
    private  String doGet(String url, int httpRequestTimeout) throws Exception {

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

        return response.toString();
    }

    @org.jetbrains.annotations.NotNull
    private String doGetHTTPS(String url_string, int httpRequestTimeout)  throws Exception {
// Load CAs from an InputStream
// (could be from a resource or ByteArrayInputStream or ...)
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
// From https://www.washington.edu/itconnect/security/ca/load-der.crt
        InputStream cert = this.context.getResources().openRawResource(R.raw.cert);
        Certificate ca = cf.generateCertificate(cert);
        cert.close();




// Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

// Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

// Create an SSLContext that uses our TrustManager
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);

// Tell the URLConnection to use a SocketFactory from our SSLContext
        URL url = new URL(url_string);
        HttpsURLConnection connection =
                (HttpsURLConnection)url.openConnection();
        connection.setSSLSocketFactory(context.getSocketFactory());
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
        //    s = doGet(myURL, httpRequestTimeout);
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