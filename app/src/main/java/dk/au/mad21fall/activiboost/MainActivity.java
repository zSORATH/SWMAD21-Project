package dk.au.mad21fall.activiboost;

import static dk.au.mad21fall.activiboost.ui.shared.login.User.CAREGIVER;
import static dk.au.mad21fall.activiboost.ui.shared.login.User.PATIENT;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import dk.au.mad21fall.activiboost.databinding.ActivityMainBinding;
import dk.au.mad21fall.activiboost.ui.shared.activities.patient.ActivitiesFragment;
import dk.au.mad21fall.activiboost.weatherApi.WeatherApi;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAIN ACTIVITY";

    public int userType;
    private ActivityMainBinding binding;
    private AppBarConfiguration appBarConfiguration;
    private NavController navController;
    private BottomNavigationView navView;

    WeatherApi api = new WeatherApi();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_calendar, R.id.navigation_diary,
                R.id.navigation_patients, R.id.navigation_activities)
                .build();
        navView = binding.navView;

       setUserType();

        switch (userType) {
            case PATIENT:
                setPatientView();
                break;
            case CAREGIVER:
                setCaregiverView();
                break;
            default:
                Log.d(TAG, "Error determining user type");
                finish();
        }
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    void setPatientView() {
        if (navView.getMenu().findItem(R.id.navigation_patients) != null) {
            navView.getMenu().removeItem(R.id.navigation_patients);
        }
       /* if (navView.getMenu().findItem(R.id.navigation_cactivities) != null) {
            navView.getMenu().removeItem(R.id.navigation_cactivities);
        }*/
    }

    void setCaregiverView() {
        if (navView.getMenu().findItem(R.id.navigation_diary) != null) {
            navView.getMenu().removeItem(R.id.navigation_diary);
        }
        /*if (navView.getMenu().findItem(R.id.navigation_activities) != null) {
            navView.getMenu().removeItem(R.id.navigation_activities);
        }*/
    }

    // https://stackoverflow.com/questions/26939759/android-getintent-from-a-fragment
    void setUserType() {
        api.getLocalWeather("aarhus", this);
        ActivitiesFragment activitiesFragment = new ActivitiesFragment();
        userType = getIntent().getIntExtra("user", userType);
        Bundle bundle = new Bundle();
        bundle.putInt("user", userType);
        activitiesFragment.setArguments(bundle);
    }
}