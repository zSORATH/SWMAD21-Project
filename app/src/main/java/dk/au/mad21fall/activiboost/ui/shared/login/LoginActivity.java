package dk.au.mad21fall.activiboost.ui.shared.login;

import static dk.au.mad21fall.activiboost.ui.shared.login.User.CAREGIVER;
import static dk.au.mad21fall.activiboost.ui.shared.login.User.PATIENT;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import dk.au.mad21fall.activiboost.MainActivity;
import dk.au.mad21fall.activiboost.R;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LOGIN ACTIVITY";

    Button btnPatient, btnCaregiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        btnPatient = findViewById(R.id.btnPatient);
        btnPatient.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("user", PATIENT);
            startActivity(intent);
        });

        btnCaregiver = findViewById(R.id.btnCaregiver);
        btnCaregiver.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("user", CAREGIVER);
            startActivity(intent);
        });
    }
}