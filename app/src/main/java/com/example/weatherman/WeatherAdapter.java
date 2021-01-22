package com.example.weatherman;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.XMLConstants;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    Context mContext;
    //    ViewGroup parentVG;
    static String DayIcon, NightIcon;
    Integer[] iconList = new Integer[50];
    String lat, lon, cityKey;


    public static class WeatherViewHolder extends RecyclerView.ViewHolder {

        //create variables for layout
        public LinearLayout containerView;
        public TextView max;
        public TextView min;
        public TextView date;
        public ImageView day_icon;


        //constructor
        public WeatherViewHolder(View v) {
            super(v);
            max = v.findViewById(R.id.max_tv);
            min = v.findViewById(R.id.min_tv);
            day_icon = v.findViewById(R.id.icon_tv);
            date = v.findViewById(R.id.date_tv);
            containerView = v.findViewById(R.id.container_view);

            containerView.setOnClickListener((v1) -> {
                Forecast myForecat = (Forecast) containerView.getTag();
                Intent intent = new Intent(v1.getContext(), weather_activity.class);
                intent.putExtra("maxTemp", myForecat.max);
                intent.putExtra("minTemp", myForecat.min);
                intent.putExtra("url", myForecat.MobileUrl);
                intent.putExtra("DayIcon", myForecat.night_Icon_num);
                intent.putExtra("NightIcon", myForecat.day_Icon_num);
                v1.getContext().startActivity(intent);
            });//onCLickListener
        }//constructor closed
    }//viewHolder closed

    private final List<Forecast> values = new ArrayList<>();
    private final RequestQueue requestQueue;


    //constructor
    public WeatherAdapter(Context context, String a, String b, String c) {
        requestQueue = Volley.newRequestQueue(context);

        mContext = context;
        this.lat = a;
        this.lon = b;
        this.cityKey=c;
        setIconList();


        loadForecast();
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weather_row, parent,
                        false);
//        parentVG=parent;
        return new WeatherViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {


        Forecast obj = values.get(position);
        holder.max.setText(obj.getMax()+"°");
        holder.min.setText(obj.getMin()+"°");
        holder.date.setText(obj.getDate());
        holder.day_icon.setImageResource(iconList[Integer.parseInt(obj.getDay_Icon_num())]);
        holder.containerView.setTag(obj);
    }

    List<String> headline = new ArrayList<>();

    //function to load weather
    public void loadForecast() {
        String url = "https://dataservice.accuweather.com/forecasts/v1/daily/5day/" +
                cityKey +
                "?apikey=zcrjySAaq4Y3sQo1aWqi9ddA9mpo5P4t";
        Log.d("myOutput", "URL used " + url);

        //onResponse
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        JSONObject firstObject = response.getJSONObject("Headline");
                        headline.add(firstObject.getString("EffectiveDate"));
                        headline.add(firstObject.getString("Severity"));
                        headline.add(firstObject.getString("Text"));
                        headline.add(firstObject.getString("MobileLink"));
                        //this data is to be used with notifications
                        for (int i = 0; i < headline.size(); i++) {
                            Log.d("myOutput", headline.get(i));
                        }

                        JSONArray results = response.getJSONArray("DailyForecasts");

                        for (int i = 0; i < results.length(); i++) {
                            JSONObject currDay = results.getJSONObject(i);

                            String MobileUrl = currDay.getString("MobileLink");
                            String date = currDay.getString("Date");
                            String pDate = date.substring(0, date.indexOf('T'));

                            JSONObject Temperature = currDay.getJSONObject("Temperature");
                            JSONObject Day = currDay.getJSONObject("Day");
                            JSONObject Night = currDay.getJSONObject("Night");

                            JSONObject minT = Temperature.getJSONObject("Minimum");
                            JSONObject maxT = Temperature.getJSONObject("Maximum");

                            DayIcon = Day.getString("Icon");
                            NightIcon = Night.getString("Icon");

                            double min = minT.getDouble("Value");
                            double max = maxT.getDouble("Value");
                            pDate = manageDate(pDate);

                            Log.d("myOutput", max + " is the max");
                            Log.d("myOutput", pDate);
                            Log.d("myOutput", DayIcon + "/" + NightIcon);

                            Forecast current = new Forecast(
                                    toCelcius(min),
                                    toCelcius(max),
                                    MobileUrl,
                                    pDate,
                                    (DayIcon),
                                    (NightIcon));
                            values.add(current);
                        }
                        Log.d("Values", values.size() + "");
                        notifyDataSetChanged();
                    }//try ends here
                    catch (JSONException e) {
                        Log.e("myError", "JSON ERROR", e);
                    }
                }
                , error -> {
            CharSequence text = "Problem at the servers. Try again after sometime";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(mContext, text, duration);
            toast.show();
            Log.e("myOutput", "VOLLEY ERROR");
            Log.e("myOutput", "using offline data");
        });//request
        requestQueue.add(request);
    }//loadForecast ends here

    private String toCelcius(double t) {
        double n = (t - 32) * (0.5556);
        return (String.format(Locale.ENGLISH, "%.1f", n));
    }

    private String manageDate(String d) {
        String[] months = {"", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        int m = Integer.parseInt(d.substring(5, d.lastIndexOf('-')));
        String s = d.substring(d.lastIndexOf('-') + 1);
        return s + " " + months[m];
    }

    private void setIconList() {
        iconList[1] = R.drawable.sunny;
        iconList[2] = R.drawable.mostly_sunny;
        iconList[3] = R.drawable.partly_sunny;
        iconList[4] = R.drawable.intermittent_clouds;
        iconList[5] = R.drawable.hazy_sunshine;
        iconList[6] = R.drawable.mostly_cloudy;
        iconList[7] = R.drawable.cloudy_night;//both day and night
        iconList[8] = R.drawable.dreary_overcast;//both day and night
        iconList[11] = R.drawable.fog;//both day and night
        iconList[12] = R.drawable.shoers;//day and night
        iconList[13] = R.drawable.mostly_cloudy_w_showers_night;//both day and night
        iconList[14] = R.drawable.partly_sunny_wshowers;
        iconList[15] = R.drawable.tshowers;
        iconList[16] = R.drawable.mostly_cloudy_w_t_storms;
        iconList[17] = R.drawable.partly_sunny_w_t_storms;
        iconList[18] = R.drawable.rain;
        iconList[19] = R.drawable.flurries;
        iconList[20] = R.drawable.mostly_cloudy_w_flurries;
        iconList[21] = R.drawable.partly_sunny_w_fluriies;
        iconList[22] = R.drawable.snow;
        iconList[23] = R.drawable.mostly_cloudy_w_snow;
        iconList[26] = R.drawable.freezing_rain;
        iconList[30] = R.drawable.hot;
        iconList[31] = R.drawable.cold;
        iconList[32] = R.drawable.windy;
        iconList[7] = R.drawable.cloudy_night;
        //Night
        iconList[33] = R.drawable.clear_night;
        iconList[7] = R.drawable.cloudy_night;
        iconList[37] = R.drawable.hazy_moonlight;
        iconList[36] = R.drawable.intermittent_clouds_night;

        iconList[34] = R.drawable.mostly_clear_night;
        iconList[38] = R.drawable.mostly_cloudy_night;
        iconList[43] = R.drawable.mostly_cloudy_w_flurries_night;
        iconList[40] = R.drawable.mostly_cloudy_w_showers_night;
        iconList[44] = R.drawable.mostly_cloudy_w_snow_night;
        iconList[42] = R.drawable.mostly_cloudy_w_t_storms_night;

        iconList[35] = R.drawable.partly_cloudy_night;
        iconList[39] = R.drawable.partly_cloudy_w_showers_night;
        iconList[41] = R.drawable.partly_cloudy_w_t_storms_night;
    }


}
