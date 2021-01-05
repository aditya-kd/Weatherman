package com.example.weatherman;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class weather_activity extends AppCompatActivity {
    Integer[] iconList= new Integer[50];
    TextView maximum;
    TextView minimum;
    ImageView DayIcon_view, NightIcon_view;
    Button moreData;
    String url,minTemp,maxTemp,DayIconNumber,NightIconNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_activity);
        setIconList();
        minTemp=getIntent().getStringExtra("minTemp");
        maxTemp=getIntent().getStringExtra("maxTemp");
        url=getIntent().getStringExtra("url");
        DayIconNumber= getIntent().getStringExtra("DayIcon");
        NightIconNumber= getIntent().getStringExtra("NightIcon");
//        String final_temp=maxTemp+"/"+minTemp+"℃";

        maximum=findViewById(R.id.max_tv);
        minimum=findViewById(R.id.min_tv);
        moreData=findViewById(R.id.button_tv);
        DayIcon_view=findViewById(R.id.icon_main_day);
        NightIcon_view= findViewById(R.id.icon_main_night);

        maximum.setText(maxTemp);
        minimum.setText(minTemp+"℃");
        Log.d("details","DayIconNumber "+DayIconNumber+" NightIconNumber "+NightIconNumber);
        DayIcon_view.setImageResource(iconList[Integer.parseInt(DayIconNumber)]);
        NightIcon_view.setImageResource(iconList[Integer.parseInt(NightIconNumber)]);


        moreData.setOnClickListener(v -> {

            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(this, Uri.parse(url));
        });
    }

    void setIconList()
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

//        moreData.setOnClickListener(v -> {
//            Log.d("new_act","BUTTON CLICKED");
//            Uri uri = Uri.parse(url);
//            Intent intent= new Intent(Intent.ACTION_VIEW,uri);
//            v.getContext().startActivity(intent);
//        });