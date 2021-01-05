package com.example.weatherman;

import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    public static Integer[] HeadIcons= new Integer[10];
    private static final int request_location=1;
    //creating a local manager var
    LocationManager location_manager;
    //creating a string type var for storing latitude and longitude
    String Latitude,longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //declarations
        TextView TodayMin=findViewById(R.id.head_min);
        TextView TodayMax=findViewById(R.id.head_max);
        ImageView HeaderIcon= findViewById(R.id.head_icon);
        RecyclerView recyclerView = findViewById(R.id.recyclerView_main);
        setHeaderIcons();

        //code to get current location longitude and latitude
        //Adding Permission

        Log.d("Location","Lat="+this.Latitude+" Long="+this.longitude);
        RecyclerView.Adapter adapter = new WeatherAdapter(getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        String HMAX= WeatherAdapter.getHeadMax();
        String HMIN= WeatherAdapter.getHeadMin();
        String a= WeatherAdapter.HEAD_MAX;
        String b= WeatherAdapter.HEAD_MIN;

        String headIcon= WeatherAdapter.HEAD_ICON;
        Log.d("myOutput","head min "+HMIN+" b "+b);
        Log.d("myOutput","head max "+HMAX+" a "+a);

        //TodayMax.setText(HMAX);TodayMin.setText(HMIN);
        HeaderIcon.setImageResource(R.drawable.header_sunny);
    }

    public static void setHeaderIcons()
    {
        HeadIcons[0]=R.drawable.header_sunny;
    }



}

//implementation 'com.android.volley:volley:1.1.1'
//implementation "androidx.recyclerview:recyclerview:1.1.0"
//<uses-permission android:name="android.permission.INTERNET" />