package dk.au.mad21fall.activiboost.ui.shared.home;

import static dk.au.mad21fall.activiboost.Constants.PATIENT;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import dk.au.mad21fall.activiboost.Constants;
import dk.au.mad21fall.activiboost.R;
import dk.au.mad21fall.activiboost.databinding.FragmentHomeBinding;
import dk.au.mad21fall.activiboost.models.Activity;
import dk.au.mad21fall.activiboost.services.LocationUtil;
import dk.au.mad21fall.activiboost.services.WeatherApi;
import dk.au.mad21fall.activiboost.ui.shared.calendar.CalendarViewModel;
import sun.bob.mcalendarview.vo.DateData;

public class HomeFragment extends Fragment {

    private static final String TAG = "HOME FRAGMENT";

    private HomeViewModel hmv;
    private FragmentHomeBinding binding;
    private String name;
    private String uid;
    private LocationUtil locationUtil = new LocationUtil();
    private String location;

    private Activity currentActivity;


    private MutableLiveData<ArrayList<Activity>> mActivities = new MutableLiveData<>();
    private ArrayList<Activity> activities = new ArrayList<>();

    private CalendarViewModel cvm;

    WeatherApi api = new WeatherApi();
    private FusedLocationProviderClient fusedLocationClient;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());



        getWeather();

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        hmv = new ViewModelProvider(this).get(HomeViewModel.class);
        cvm = new ViewModelProvider(this).get(CalendarViewModel.class);


        if(getCalendar() == true){
            Toast.makeText(getContext(), "You have an activity today", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "You have an activity today");
            // TODO: Show activity
        } else {
            Toast.makeText(getContext(),  "No activities today", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "No activities today");
            // TODO: Give no activity message in view
        }

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        uid = (String) getActivity().getIntent().getSerializableExtra("user");
        hmv.setUid(uid);

        if (hmv.getUserType() == PATIENT) {
            name = hmv.getPatient(uid).getName();
        } else {
            name = hmv.getCaregiver(uid).getName();
        }

        final TextView textView = binding.textHome;
        hmv.getText().observe(getViewLifecycleOwner(), s -> textView.setText(s + name));
        return root;
    }

    private boolean getCalendar() {
        mActivities = cvm.getActivities(uid);
        activities = mActivities.getValue();
        Calendar calendar = Calendar.getInstance();
        String today = ""+ calendar.get(Calendar.YEAR) + calendar.get(Calendar.MONTH) + calendar.get(Calendar.DAY_OF_MONTH);;
        String _aDate;


        Log.d(TAG, "Today is: " + today);

        for (Activity a : activities) {

            _aDate = "" + calendar.get(Calendar.YEAR) + (calendar.get(Calendar.MONTH)+1) + calendar.get(Calendar.DAY_OF_MONTH);
            if (today.equals(_aDate)) {
                Log.d(TAG, "Activity found: " + a.getActivityName());
                currentActivity = a;
                return true;
            }
        }
        return false;

    }

    private void getWeather() {
        Context context = getContext();
        FragmentActivity activity = getActivity();

         locationUtil.getLocationCoordinates(context, activity, fusedLocationClient);

         Log.d(TAG, "Location from constants: " + Constants.LOCATION);
         String testLocation =  "lat=56.16216216216216&lon=10.160215237798806";
         api.getLocalWeather(testLocation, context);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}