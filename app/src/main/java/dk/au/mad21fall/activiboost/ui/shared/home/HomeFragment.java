package dk.au.mad21fall.activiboost.ui.shared.home;

import static dk.au.mad21fall.activiboost.Constants.PATIENT;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import dk.au.mad21fall.activiboost.Constants;
import dk.au.mad21fall.activiboost.databinding.FragmentHomeBinding;
import dk.au.mad21fall.activiboost.models.WeatherModel;
import dk.au.mad21fall.activiboost.services.LocationUtil;
import dk.au.mad21fall.activiboost.services.WeatherApi;

public class HomeFragment extends Fragment {

    private static final String TAG = "HOME FRAGMENT";

    private HomeViewModel hmv;
    private FragmentHomeBinding binding;
    private String name;
    private String uid;
    private LocationUtil locationUtil = new LocationUtil();
    private String location;


    WeatherApi api;
    private FusedLocationProviderClient fusedLocationClient;
    WeatherModel weather;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        Context context = getContext();
        FragmentActivity activity = getActivity();
        api = new WeatherApi(context,activity,fusedLocationClient);

        new Handler().postDelayed(() -> {
            weather = api.getWeather(context,activity,fusedLocationClient);
            Log.e(TAG, "Received weather: "+ weather.getName());
        }, 500);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        hmv = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        uid = (String) getActivity().getIntent().getSerializableExtra("user");
        hmv.setUid(uid);

        if (hmv.getUserType() == PATIENT) {
            name = hmv.getPatient(uid).getName();
        } else {
            name = hmv.getCaregiver(uid).getName();
        }

        new Handler().postDelayed(() -> {
            if(weather == null){
                new Handler().postDelayed(() -> {
                    Log.e(TAG, "Delayed - Received weather: "+ weather.getName());
                }, 1200);
            } else{
                Log.e(TAG, "Received delayed weather: "+weather.getName());
            }
        }, 500);


        final TextView textView = binding.textHome;
        hmv.getText().observe(getViewLifecycleOwner(), s -> textView.setText(s + name));
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}