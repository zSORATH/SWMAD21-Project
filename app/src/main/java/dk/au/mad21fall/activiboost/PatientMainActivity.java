package dk.au.mad21fall.activiboost;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import dk.au.mad21fall.activiboost.databinding.ActivityPatientMainBinding;
import dk.au.mad21fall.activiboost.ui.shared.home.HomeFragment;
import dk.au.mad21fall.activiboost.services.WeatherApi;

public class PatientMainActivity extends AppCompatActivity {

    private static final String TAG = "PATIENT MAIN ACTIVITY";

    private String uid;
    private ActivityPatientMainBinding binding;
    private AppBarConfiguration appBarConfiguration;
    private NavController navController;
    private BottomNavigationView navView;



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

        Log.d(TAG, "Nav bar height: " + navView.getHeight());
    }
}