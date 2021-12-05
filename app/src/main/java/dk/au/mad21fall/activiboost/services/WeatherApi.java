package dk.au.mad21fall.activiboost.services;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dk.au.mad21fall.activiboost.Constants;
import dk.au.mad21fall.activiboost.models.WeatherModel;
import dk.au.mad21fall.activiboost.repository.Repository;

public class WeatherApi {

    final static String TAG = "WeatherAPI";
   // Application app;
    RequestQueue queue;
    String API_KEY = "e07d416ac8563e8c545e92ad7be56a77";
    String currentLocation;
    private LocationUtil locationUtil = new LocationUtil();
    public WeatherModel weather;

    public WeatherApi(Context context, FragmentActivity activity, FusedLocationProviderClient fusedLocationProvided) {
        getLocalWeather(context,activity,fusedLocationProvided);
        Log.d(TAG, "Called weatherAPI");
    }

    public void getLocalWeather(Context context, FragmentActivity activity, FusedLocationProviderClient fusedLocationProvided){

        locationUtil.getLocationCoordinates(context, activity, fusedLocationProvided);
        currentLocation = locationUtil.getCoordinatesString();
        new Handler().postDelayed(() -> {
            currentLocation = locationUtil.getCoordinatesString();
            if (currentLocation == null){
                currentLocation = "lat=55.16216216216216&lon=10.160215237798806";
                Log.e(TAG, "Coordinates manually set");
            } else{
                Log.d(TAG, "Got Coordinates: " + currentLocation);
            }
            String url = "https://api.openweathermap.org/data/2.5/weather?"+currentLocation+"&appid="+API_KEY;

            if (queue == null)
            {
                queue = Volley.newRequestQueue(context);
            }

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    response -> {
                        Log.d(TAG, "onResponse: " + response);
                        parseJson(response);
                    }, error -> Log.e(TAG, "That did not work!", error));
            Log.d(TAG, "StringRequest: "+stringRequest);
            queue.add(stringRequest);

        }, 300);

    }

    private WeatherModel parseJson(String json) {
        Gson gson = new GsonBuilder().create();
        weather = gson.fromJson(json, WeatherModel.class);
        if (weather != null) {
            Log.d(TAG, "Received: " + gson.toJson(weather));
            Log.d(TAG, "Weather area: " +weather.getName());
        }
        return weather;
    }

    private WeatherModel getPrivateWeather(Context context, FragmentActivity activity, FusedLocationProviderClient fusedLocationProvided){

        if (weather == null) {
            getLocalWeather(context, activity, fusedLocationProvided);

            Log.e(TAG, "No weather object found.. Calling get");
        }
        return weather;

    }

    public WeatherModel getWeather(Context context, FragmentActivity activity, FusedLocationProviderClient fusedLocationProvided){

       // WeatherModel myWeather = getPrivateWeather(context, activity, fusedLocationProvided);

        new Handler().postDelayed(() -> {
            if (weather == null){
                Log.e(TAG, "My weather is null :( ");

            } else {
                Log.e(TAG, "This is my location: " + weather.getName());

            }
        }, 450);

        return weather;
    }
}
