package com.example.weatherman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class SplashScreen extends AppCompatActivity implements LocationListener {
    LocationManager locationManager;
    String Latitude,longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(ContextCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SplashScreen.this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION
                    },100);
        }
        try {
            locationManager=(LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5,SplashScreen.this);

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_splash_screen);
                Intent i= new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
                finish();


            }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        Latitude=String.valueOf(location.getLatitude());
        longitude=String.valueOf(location.getLongitude());
        Log.d("mainLocation","location="+Latitude+"/"+longitude);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

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