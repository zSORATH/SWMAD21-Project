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
import dk.au.mad21fall.activiboost.ui.caregiver.patients.PatientsFragment;
import dk.au.mad21fall.activiboost.ui.patient.activities.PatientActivitiesFragment;
import dk.au.mad21fall.activiboost.ui.shared.calendar.CalendarFragment;
import dk.au.mad21fall.activiboost.ui.shared.home.HomeFragment;
import dk.au.mad21fall.activiboost.weatherApi.WeatherApi;

public class CaregiverMainActivity extends AppCompatActivity {

    private static final String TAG = "CAREGIVER MAIN ACTIVITY";

    public String uid;
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

        setUser();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    // https://stackoverflow.com/questions/26939759/android-getintent-from-a-fragment
    void setUser() {
        api.getLocalWeather("aarhus", this);

        uid = (String) getIntent().getSerializableExtra("user");
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", uid);

        HomeFragment homeFragment = new HomeFragment();
        /*CalendarFragment calendarFragment = new CalendarFragment();
        PatientsFragment patientsFragment = new PatientsFragment();
        CaregiverActivitiesFragment caregiverActivitiesFragment = new CaregiverActivitiesFragment();

        homeFragment.setArguments(bundle);
        calendarFragment.setArguments(bundle);
        patientsFragment.setArguments(bundle);
        caregiverActivitiesFragment.setArguments(bundle);*/
    }
}