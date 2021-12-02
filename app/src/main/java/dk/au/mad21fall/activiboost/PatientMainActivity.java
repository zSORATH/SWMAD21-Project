package dk.au.mad21fall.activiboost;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.auth.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import dk.au.mad21fall.activiboost.databinding.ActivityPatientMainBinding;
import dk.au.mad21fall.activiboost.ui.caregiver.activities.CaregiverActivitiesFragment;
import dk.au.mad21fall.activiboost.ui.caregiver.patients.PatientsFragment;
import dk.au.mad21fall.activiboost.ui.patient.activities.PatientActivitiesFragment;
import dk.au.mad21fall.activiboost.ui.patient.diary.DiaryFragment;
import dk.au.mad21fall.activiboost.ui.shared.calendar.CalendarFragment;
import dk.au.mad21fall.activiboost.ui.shared.home.HomeFragment;
import dk.au.mad21fall.activiboost.weatherApi.WeatherApi;

public class PatientMainActivity extends AppCompatActivity {

    private static final String TAG = "PATIENT MAIN ACTIVITY";

    private String uid;
    private ActivityPatientMainBinding binding;
    private AppBarConfiguration appBarConfiguration;
    private NavController navController;
    private BottomNavigationView navView;

    WeatherApi api = new WeatherApi();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPatientMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_calendar, R.id.navigation_diary, R.id.navigation_activities)
                .build();
        navView = binding.navView;

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        setUser();
    }

    // TODO: Get intent in fragments correctly
    // https://stackoverflow.com/questions/26939759/android-getintent-from-a-fragment
    void setUser() {
        api.getLocalWeather("aarhus", this);

        uid = (String) getIntent().getSerializableExtra("user");

        Bundle bundle = new Bundle();
        bundle.putSerializable("user", uid);

        HomeFragment homeFragment = new HomeFragment();
        /*CalendarFragment calendarFragment = new CalendarFragment();
        DiaryFragment diaryFragment = new DiaryFragment();
        PatientActivitiesFragment patientActivitiesFragment = new PatientActivitiesFragment();/*

        homeFragment.setArguments(bundle);
        /*calendarFragment.setArguments(bundle);
        diaryFragment.setArguments(bundle);
        patientActivitiesFragment.setArguments(bundle);*/

    }
}