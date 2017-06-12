package com.example.da08.httpurlconnection;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements TaskInterface{

    TextView textView;
    String url = "http://google.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.textView);
            Task.newTask(this);
    }

    @Override
    public String getUrl(){
        return url;
    }

    @Override
    public void postExecute(String result){
        textView.setText(result);
    }
}