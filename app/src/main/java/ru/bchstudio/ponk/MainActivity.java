package ru.bchstudio.ponk;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import ru.bchstudio.ponk.notifi.CreatorNotification;
import ru.bchstudio.ponk.MainService;
import ru.bchstudio.ponk.service.RealService;


public class MainActivity extends AppCompatActivity implements OnWebAsyncTaskCompleted {


    private Intent serviceIntent;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (RealService.serviceIntent==null) {
            serviceIntent = new Intent(this, RealService.class);
            startService(serviceIntent);
        } else {
            serviceIntent = RealService.serviceIntent;//getInstance().getApplication();
            Toast.makeText(getApplicationContext(), "already", Toast.LENGTH_LONG).show();
        }

        tvRez = findViewById(R.id.textView2);
        tvCounter = findViewById(R.id.textView);
        iv = findViewById(R.id.imageView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceIntent!=null) {
            stopService(serviceIntent);
            serviceIntent = null;
        }
    }

    private static final String myURL = "http://89.169.90.244:5000/moscow";
    TextView tvRez;
    TextView tvCounter;
    int queryCounter = 0;
    ImageView iv;



    public Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.5f); // round
        int height = (int) (baseline + paint.descent() + 0.5f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }




    @Override
    public void onWebAsyncTaskCompleted(String result) {
        tvRez.setText(result);
        queryCounter += 1;
        tvCounter.setText(String.valueOf(queryCounter));
    }





    public void onMyButtonClick(View view)
    {
        new WebAsyncTask(myURL,this).execute();


        Bitmap img =  textAsBitmap("22", 60, Color.RED);
        iv.setImageBitmap(img);

        CreatorNotification notification = new CreatorNotification("myChnl", getApplicationContext());

        notification.send("Test", img);

    }


    public void onStartClick(View view) {
        MainService.start(getApplicationContext());
    }

    public void onStopClick(View view) {


    }


}
