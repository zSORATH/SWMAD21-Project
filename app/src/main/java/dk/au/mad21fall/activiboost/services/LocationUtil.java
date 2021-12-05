package dk.au.mad21fall.activiboost.services;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;

import dk.au.mad21fall.activiboost.Constants;

public class LocationUtil {

    // Inspired by the following:
    // https://developer.android.com/training/location/retrieve-current
    // https://github.com/ideeastudios/android-fused-location-provider-example

    private static final String TAG = "LOCATION UTILITY";

    private double lat;
    private double lon;


    public void getLocationCoordinates(Context context, FragmentActivity activity, FusedLocationProviderClient mFusedLocationClient) {
        String locationString;
        checkLocationPermission(context, activity);


        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.getLastLocation().addOnSuccessListener( activity, location -> {
                // Got a response
                if (location != null) // Location might be null, if the user never used any location tracking before
                {
                    Toast.makeText(context, "Got location!", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Got location lat: " +  location.getLatitude());
                    Log.d(TAG, "Got location long: " +  location.getLongitude());

                    lat = location.getLatitude();
                    lon = location.getLongitude();
                    Constants.LOCATION = "lat="+lat+"&lon="+lon;
                    Log.d(TAG, "Constant Location: "+ Constants.LOCATION);

//                   api.getLocalWeather(locationString, context);


                } else {
                    Toast.makeText(context, "location: IS NULL", Toast.LENGTH_LONG).show();
                }

            });
        } else {

                Toast.makeText(context, "getLocation ERROR", Toast.LENGTH_LONG).show();
            }

    }


    private boolean checkLocationPermission(Context context, FragmentActivity activity) {
        //check the location permissions and return true or false.
        if ( ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //permissions granted
            Log.d(TAG, "Permission granted");
            return true;
        } else {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_COARSE_LOCATION)) {
               // Asynchronous dialog box, explaining the permission request
                //TODO: Set as string
                new AlertDialog.Builder(context)
                        .setTitle("Permissions request")//TODO: Set as string
                        .setMessage("We need your permission to show the weather for your location")//TODO: Set as string
                        .setPositiveButton("I understand", (dialogInterface, i) -> {
                            // Asks for the permission
                            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, Constants.LOCATION_REQUEST);
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(activity,  new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, Constants.LOCATION_REQUEST);
            }
            return false;
        }
    }
}
