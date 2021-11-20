package dk.au.mad21fall.activiboost.ui.shared.login;

import static dk.au.mad21fall.activiboost.ui.shared.login.User.CAREGIVER;
import static dk.au.mad21fall.activiboost.ui.shared.login.User.PATIENT;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import dk.au.mad21fall.activiboost.MainActivity;
import dk.au.mad21fall.activiboost.R;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LOGIN ACTIVITY";

    Button btnPatient, btnCaregiver;
    private LoginViewModel lvm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        lvm = new ViewModelProvider(this).get(LoginViewModel.class);

        getSupportActionBar().hide();

        btnPatient = findViewById(R.id.btnPatient);
        btnPatient.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("user", PATIENT);
            launcher.launch(intent);
        });

        btnCaregiver = findViewById(R.id.btnCaregiver);
        btnCaregiver.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("user", CAREGIVER);
            launcher.launch(intent);
        });


}
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        Bundle b = data.getExtras();
                        int j = b.getInt("int");
                        if (j == 1){
                            finish();
                        }

                    }

                }
            });
}