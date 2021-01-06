package com.example.weatherman;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.function.LongFunction;

public class MainActivity extends AppCompatActivity {
    public static Integer[] HeadIcons= new Integer[10];
    public String max_temp_for_curDay;
    private static final int request_location=1;
    //creating a local manager var
    LocationManager location_manager;
    //creating a string type var for storing latitude and longitude
    String Latitude,longitude;
    private Context AppContext;
    Integer[] iconList= new Integer[50];
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        RecyclerView recyclerView = findViewById(R.id.recyclerView_main);

        AppContext=getApplicationContext();
        initiateLoading();

        RecyclerView.Adapter adapter = new WeatherAdapter(getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);



    }
    List<Forecast> todayList= new ArrayList<>(12);
    private void initiateLoading()
    {
        requestQueue= Volley.newRequestQueue(AppContext);
        loadTodayForecast();
    }
    private void loadTodayForecast()
    {
        TextView TodayMax=findViewById(R.id.head_max);
        ImageView HeaderIcon= findViewById(R.id.head_icon);
        String url="https://dataservice.accuweather.com/forecasts/v1/hourly/12hour/206679?apikey=zcrjySAaq4Y3sQo1aWqi9ddA9mpo5P4t";
        Log.d("main","url="+url);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try{
                        for(int i=0;i<response.length();i++) {
                            JSONObject current_hr = response.getJSONObject(i);
                            String iconPhrase = current_hr.getString("IconPhrase");
                            String date = current_hr.getString("DateTime");
                            String iconNumber = current_hr.getString("WeatherIcon");
                            String rain = current_hr.getString("PrecipitationProbability");
                            JSONObject t = current_hr.getJSONObject("Temperature");
                            double t2 = t.getDouble("Value");
                            String temp = toCelcius(t2);
                            Log.d("main", "iconPhrase" + iconPhrase);
                            Log.d("main", "date" + date);
                            Log.d("main", "iconNumber" + iconNumber);
                            Log.d("main", "rain" + rain);
                            Log.d("main", "double " + t2 + "");
                            Log.d("main", "temp" + temp);

                            if (i == 0) {
//                                TodayMax.setText(temp);
//                                HeaderIcon.setImageResource(iconList[Integer.parseInt(iconNumber)]);
                            }
                        }
                    }
                    catch (JSONException e)
                    {
                        Log.e("main","catch error occured",e);
                    }
                },
                error -> {
                    CharSequence text = "Problem at the servers. Try again after sometime";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(AppContext, text, duration);
                    toast.show();
                    Log.e("main","VOLLEY ERROR");
                    Log.e("main","using offline data");
                }
        );
        requestQueue.add(request);
    }
    private String toCelcius(double t)
    {
        double n=(t-32)*(0.5556);
        return (String.format(Locale.ENGLISH,"%.1f",n));
    }
    private void setIconList()
    {
        iconList[1]=R.drawable.sunny;
        iconList[2]=R.drawable.mostly_sunny;
        iconList[3]=R.drawable.partly_sunny;
        iconList[4]=R.drawable.intermittent_clouds;
        iconList[5]=R.drawable.hazy_sunshine;
        iconList[6]=R.drawable.mostly_cloudy;
        iconList[7]=R.drawable.cloudy_night;//both day and night
        iconList[8]=R.drawable.dreary_overcast;//both day and night
        iconList[11]=R.drawable.fog;//both day and night
        iconList[12]=R.drawable.shoers;//day and night
        iconList[13]=R.drawable.mostly_cloudy_w_showers_night;//both day and night
        iconList[14]=R.drawable.partly_sunny_wshowers;
        iconList[15]=R.drawable.tshowers;
        iconList[16]=R.drawable.mostly_cloudy_w_t_storms;
        iconList[17]=R.drawable.partly_sunny_w_t_storms;
        iconList[18]=R.drawable.rain;
        iconList[19]=R.drawable.flurries;
        iconList[20]=R.drawable.mostly_cloudy_w_flurries;
        iconList[21]=R.drawable.partly_sunny_w_fluriies;
        iconList[22]=R.drawable.snow;
        iconList[23]=R.drawable.mostly_cloudy_w_snow;
        iconList[26]=R.drawable.freezing_rain;
        iconList[30]=R.drawable.hot;
        iconList[31]=R.drawable.cold;
        iconList[32]=R.drawable.windy;
        iconList[7]=R.drawable.cloudy_night;
        //Night
        iconList[33]=R.drawable.clear_night;
        iconList[7]=R.drawable.cloudy_night;
        iconList[37]=R.drawable.hazy_moonlight;
        iconList[36]=R.drawable.intermittent_clouds_night;

        iconList[34]=R.drawable.mostly_clear_night;
        iconList[38]=R.drawable.mostly_cloudy_night;
        iconList[43]=R.drawable.mostly_cloudy_w_flurries_night;
        iconList[40]=R.drawable.mostly_cloudy_w_showers_night;
        iconList[44]=R.drawable.mostly_cloudy_w_snow_night;
        iconList[42]=R.drawable.mostly_cloudy_w_t_storms_night;

        iconList[35]=R.drawable.partly_cloudy_night;
        iconList[39]=R.drawable.partly_cloudy_w_showers_night;
        iconList[41]=R.drawable.partly_cloudy_w_t_storms_night;
    }
}

//implementation 'com.android.volley:volley:1.1.1'
//implementation "androidx.recyclerview:recyclerview:1.1.0"
//<uses-permission android:name="android.permission.INTERNET" />