package dk.au.mad21fall.activiboost.weatherApi;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dk.au.mad21fall.activiboost.models.WeatherModel;

public class WeatherApi {

    final static String TAG = "WeatherAPI";
   // Application app;
    RequestQueue queue;
    String API_KEY = "e07d416ac8563e8c545e92ad7be56a77";

    public void testUrl(String city){
        String url = "http://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+API_KEY;
        Log.d("TAG2", "Calling url: " + url);
    }

    public void getLocalWeather(String city, Context context){

        String url = "https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+API_KEY;
        Log.d(TAG, "Calling url 2: " + url);
        if (queue == null)
        {
            queue = Volley.newRequestQueue(context);
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                        parseJson(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "That did not work!", error);
            }
        });
        Log.e(TAG, "StringRequest: "+stringRequest);
        queue.add(stringRequest);
    };

    private void parseJson(String json) {
        Gson gson = new GsonBuilder().create();
        WeatherModel weather = gson.fromJson(json, WeatherModel.class);
        if (weather != null) {
            Log.d(TAG, "Recieved: " + gson.toJson(weather));
        }
    }
}
