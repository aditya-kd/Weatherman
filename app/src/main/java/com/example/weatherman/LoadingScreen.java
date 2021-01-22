package com.example.weatherman;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LoadingScreen extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        ProgressBar circle= (ProgressBar)findViewById(R.id.circle);
        circle.setVisibility(circle.VISIBLE);


        //loadTodayForecast(key);
        Intent i= new Intent(LoadingScreen.this, MainActivity.class);
        startActivity(i);

    }



//    private void initiateLoading(String key, String name)
//    {
//        citykey=key;
//        TextView presentLocation= findViewById(R.id.present_location);
//        //presentLocation.setText("Lucknow");
//
//       // Log.d("key in initiateLoading", key);
//
//
//
//    }
//    //load main screen condition
    //load 12 hour forecast

    //convert to celcius
    //load City Key Function

}