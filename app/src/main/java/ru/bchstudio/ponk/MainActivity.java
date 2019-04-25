package ru.bchstudio.ponk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;




public class MainActivity extends AppCompatActivity implements OnWebAsyncTaskCompleted {


    private static final String myURL = "http://89.169.90.244:5000/moscow";
    TextView tvRez;
    TextView tvCounter;
    int queryCounter = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvRez = findViewById(R.id.textView2);
        tvCounter = findViewById(R.id.textView);


    }

    @Override
    public void onWebAsyncTaskCompleted(String result) {
        tvRez.setText(result);
        queryCounter += 1;
        tvCounter.setText(String.valueOf(queryCounter));
        // выводим сообщение
        Toast.makeText(this, "Connect", Toast.LENGTH_SHORT).show();
    }


    public void onMyButtonClick(View view)
    {
        new WebAsyncTask(myURL,this).execute();
    }


}
