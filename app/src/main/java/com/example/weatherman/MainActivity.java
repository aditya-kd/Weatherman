package com.example.weatherman;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.LongFunction;

public class MainActivity extends AppCompatActivity implements LocationListener {
    public static Integer[] HeadIcons= new Integer[10];
    private Context AppContext;
    Integer[] iconList= new Integer[50];
    private RequestQueue requestQueue;
    LocationManager locationManager;
    String Latitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Code to get location*/
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION
                    },100);
        }
        try {
            locationManager=(LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5,MainActivity.this);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        /*Ends here*/









        RecyclerView recyclerView = findViewById(R.id.recyclerView_main);
        AppContext=getApplicationContext();
        setIconList();
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
                            String MobileLink= current_hr.getString("MobileLink");
                            double t2 = t.getDouble("Value");
                            String temp = toCelcius(t2);

                            String pDate=date.substring(0,date.indexOf('T'));
                            pDate=manageDate(pDate);
                            Log.d("main", "iconPhrase" + iconPhrase);
                            Log.d("main", "date" + date);
                            Log.d("main", "iconNumber" + iconNumber);
                            Log.d("main", "rain" + rain);
                            Log.d("main", "double " + t2 + "");
                            Log.d("main", "temp" + temp);
                            Log.d("main", "MobileLink" + MobileLink);

                            if (i == 0) {
                                TextView presentDay_temp_textview= findViewById(R.id.head_max);
                                presentDay_temp_textview.setText(temp+"°");
                                ImageView presentDay_icon_view= findViewById(R.id.head_icon);
                                Log.d("main",Integer.parseInt(iconNumber)+"");
                                presentDay_icon_view.setImageResource(iconList[Integer.parseInt(iconNumber)]);
//                                TodayMax.setText(temp);
//                                HeaderIcon.setImageResource(iconList[Integer.parseInt(iconNumber)]);
                            }
                            todayList.add(new Forecast("0",temp,MobileLink, pDate,iconNumber,iconPhrase));
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
    private String manageDate(String d)
    {
        String[] months={"","Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        int m= Integer.parseInt(d.substring(5,d.lastIndexOf('-')));
        String s=d.substring(d.lastIndexOf('-')+1);
        return s+" "+months[m];
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

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Latitude=String.valueOf(location.getLatitude());
        longitude=String.valueOf(location.getLongitude());
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

//implementation 'com.android.volley:volley:1.1.1'
//implementation "androidx.recyclerview:recyclerview:1.1.0"
//<uses-permission android:name="android.permission.INTERNET" />