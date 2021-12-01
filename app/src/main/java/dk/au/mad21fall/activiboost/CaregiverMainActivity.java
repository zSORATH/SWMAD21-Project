package dk.au.mad21fall.activiboost;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import dk.au.mad21fall.activiboost.databinding.ActivityCaregiverMainBinding;
import dk.au.mad21fall.activiboost.ui.caregiver.activities.CaregiverActivitiesFragment;
import dk.au.mad21fall.activiboost.weatherApi.WeatherApi;

public class CaregiverMainActivity extends AppCompatActivity {

    private static final String TAG = "CAREGIVER MAIN ACTIVITY";

    public int userType;
    private ActivityCaregiverMainBinding binding;
    private AppBarConfiguration appBarConfiguration;
    private NavController navController;
    private BottomNavigationView navView;

    WeatherApi api = new WeatherApi();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCaregiverMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_calendar, R.id.navigation_patients, R.id.navigation_activities)
                .build();
        navView = binding.navView;

        setUserType();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    // https://stackoverflow.com/questions/26939759/android-getintent-from-a-fragment
    void setUserType() {
        api.getLocalWeather("aarhus", this);
        CaregiverActivitiesFragment activitiesFragment = new CaregiverActivitiesFragment();
        userType = getIntent().getIntExtra("user", userType);
        Bundle bundle = new Bundle();
        bundle.putInt("user", userType);
        activitiesFragment.setArguments(bundle);
    }
}