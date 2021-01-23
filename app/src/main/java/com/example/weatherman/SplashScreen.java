package com.example.weatherman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class SplashScreen extends AppCompatActivity implements LocationListener {
    LocationManager locationManager;
    String Latitude,longitude;
    private Context AppContext;
    private RequestQueue requestQueue;
    public Intent i;
    String globalKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppContext = getApplicationContext();
        requestQueue= Volley.newRequestQueue(AppContext);

        /* Location prompts and then collecting the coordinates*/

        if(ContextCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(SplashScreen.this, new String[]
                    {
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },100);
        }
        try {
            locationManager=(LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5,SplashScreen.this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location)
    {
        Latitude=String.valueOf(location.getLatitude());
        longitude=String.valueOf(location.getLongitude());

        Log.d("SplashScreen","location="+Latitude+"/"+longitude);

        loadCityKey();
        setContentView(R.layout.activity_splash_screen);

    }

    public void loadCityKey() {

        /* completing the url*/
        String baseUrl = "https://dataservice.accuweather.com/locations/v1/cities/geoposition/search?apikey=zcrjySAaq4Y3sQo1aWqi9ddA9mpo5P4t&q=" +
                Latitude +
                "," +
                longitude +
                "&details=true";

        Log.d("SplashScreen ","COORDINATES: "+Latitude+","+longitude);
        Log.d("SplashScreen","URL: "+baseUrl);

        JsonObjectRequest getKeyRequest = new JsonObjectRequest(
                Request.Method.GET,
                baseUrl,
                null,
                response -> {
                    try {

                        String myResponse= response.toString();
                        String key=(myResponse.substring(20, 26));
                        String LocalizedName="";
                        int nameIndex= myResponse.indexOf("LocalizedName");
                        nameIndex+=12+3+1;
                        char ch='0';
                        ch=myResponse.charAt(nameIndex);

                        while(ch!='"')
                        {
                            LocalizedName+= ch;
                            ch=myResponse.charAt(++nameIndex);
                        }

                        Log.d("SplashScreen","KEY: "+key);
                        Log.d("SplashScreen","LocalizedName"+ LocalizedName);

                        this.setKey(key, LocalizedName);
                    }
                    catch (StringIndexOutOfBoundsException e)
                    {
                        Log.e("SplashScreen","json error in catch");
                    }
                },
                error -> {
                    CharSequence text = "Problem at the servers. Try again after sometime";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(AppContext, text, duration);
                    toast.show();
                    Log.e("SplashScreen", "VOLLEY ERROR");
                    Log.e("SplashScreen", "using offline data");
                }
        );
        requestQueue.add(getKeyRequest);
    }

    /* this function creates an Intent as soon as the key and localized name is fetched*/
    public void setKey(String key, String cityName)
    {
        globalKey= key;
        Log.d("SplashScreen","GlobalKey Value: "+globalKey);
        i=new Intent(SplashScreen.this, MainActivity.class);

        Log.d("SpalshScreen","Lat"+Latitude+ "Long "+longitude+" CityKey "+globalKey+" Localized Name: "+cityName);

        i.putExtra("Latitude",Latitude);
        i.putExtra("Longitude", longitude);
        i.putExtra("CityKey",globalKey);
        i.putExtra("cityName",cityName);
        startActivity(i);
        finish();
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
    @Override
    public void onProviderEnabled(@NonNull String provider) {}
    @Override
    public void onProviderDisabled(@NonNull String provider) {}
}