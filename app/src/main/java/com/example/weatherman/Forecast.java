package com.example.weatherman;

public class Forecast {
    String min;
    String max;
    String date;
    String MobileUrl;
    String day_Icon_num, night_Icon_num;

    public Forecast(String min, String max, String url,String date, String icon_1, String icon_2)
    {
        this.max=max;
        this.min=min;
        this.MobileUrl=url;
        this.date=date;
        this.day_Icon_num=icon_1;
        this.night_Icon_num=icon_2;
    }

    public String getMin(){
        return this.min;
    }

    public String getMax() {
        return this.max;
    }

    public String getMobileUrl() {
        return this.MobileUrl;
    }

    public String getDate(){ return this.date;}

    public String getDay_Icon_num() {
        return day_Icon_num;
    }

    public String getNight_Icon_num() {
        return night_Icon_num;
    }
}
