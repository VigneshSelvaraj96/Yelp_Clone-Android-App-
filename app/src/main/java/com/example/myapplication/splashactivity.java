package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

public class splashactivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_MyApplication);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashactivity);
        System.out.println("we went to splash activity first");
        new Timer().schedule(
                new TimerTask(){

                    @Override
                    public void run(){

                        startActivity(new Intent(splashactivity.this, MainActivity.class));
                        finish();
                    }

                }, 3000);

    }

}