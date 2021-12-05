package dk.au.mad21fall.activiboost.ui.shared.home;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;
import static dk.au.mad21fall.activiboost.Constants.PATIENT;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import dk.au.mad21fall.activiboost.Constants;
import dk.au.mad21fall.activiboost.R;
import dk.au.mad21fall.activiboost.databinding.FragmentHomeBinding;
import dk.au.mad21fall.activiboost.models.Submodels.WeatherSM;
import dk.au.mad21fall.activiboost.models.WeatherModel;
import dk.au.mad21fall.activiboost.services.LocationUtil;
import dk.au.mad21fall.activiboost.services.WeatherApi;

public class HomeFragment extends Fragment {

    private static final String TAG = "HOME FRAGMENT";

    private HomeViewModel hmv;
    private FragmentHomeBinding binding;
    private String name;
    private String uid;

    WeatherApi api;
    private FusedLocationProviderClient fusedLocationClient;
    WeatherModel weather;

    //UI
    TextView txtName, txtCity, txtTemp, txtWeatherType;
    ImageView imgIcon;


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
        }, 800);

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
                    Log.d(TAG, "Delayed - Received weather: "+ weather.getName());
                    setUI();
                }, 1200);
            } else{
                Log.d(TAG, "Received delayed weather: "+weather.getName());
                setUI();
            }
        }, 700);

//
//        final TextView textView = binding.textUserName;
//        hmv.getText().observe(getViewLifecycleOwner(), s -> textView.setText(s + name));
        return root;
    }

    private void setUI() {
        /// Image load: https://stackoverflow.com/questions/6407324/how-to-display-image-from-url-on-android

        List<WeatherSM> weatherSM = weather.getWeatherSM();
        String icon = weatherSM.get(0).getIcon();
        Log.e(TAG, "Iconcode: "+ icon);
        String imgUrl = "http://openweathermap.org/img/w/" + icon + ".png";
        Log.e(TAG, "ImgURL: "+ imgUrl);

        imgIcon = binding.imgWeather;
        try {
            Glide.with(getContext())
                    .load(imgUrl)
                    .placeholder(R.drawable.custom_icon_foreground)
                    .dontAnimate()
                    .into(imgIcon);
        } catch (Exception e) {
            Log.w(TAG, "Image exception found:  " + e);
        }

        txtName = binding.textUserName;
        hmv.getText().observe(getViewLifecycleOwner(), s -> txtName.setText(s + name));
        txtCity = binding.textCity;
        txtCity.setText(weather.getName());
        txtWeatherType = binding.textWeatherType;
        txtWeatherType.setText(weatherSM.get(0).getMain());
        txtTemp = binding.textTemperatue;
        String temperature = String.format("%.2f",(weather.getMainSM().getTemp()/100));
        txtTemp.setText(""+temperature+ "\u2103");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}