package com.example.weatherman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i= new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        },2000);

    }
}
//new Handler().postDelayed(new Runnable() {
//@Override
//public void run() {
//        Intent i=new Intent(MainActivity.this,
//        SecondActivity.class);
//        //Intent is used to switch from one activity to another.
//
//        startActivity(i);
//        //invoke the SecondActivity.
//
//        finish();
//        //the current activity will get finished.
//        }
//        }, SPLASH_SCREEN_TIME_OUT);