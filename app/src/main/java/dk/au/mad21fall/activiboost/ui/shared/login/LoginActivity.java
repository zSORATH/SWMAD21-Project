package dk.au.mad21fall.activiboost.ui.shared.login;

import static dk.au.mad21fall.activiboost.Constants.CAREGIVER;
import static dk.au.mad21fall.activiboost.Constants.PATIENT;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.firebase.ui.auth.data.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import dk.au.mad21fall.activiboost.CaregiverMainActivity;
import dk.au.mad21fall.activiboost.PatientMainActivity;
import dk.au.mad21fall.activiboost.R;
import dk.au.mad21fall.activiboost.models.Caregiver;
import dk.au.mad21fall.activiboost.models.Patient;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LOGIN ACTIVITY";

    private Button btnLogin, btnSignUp, btnPLogin, btnCLogin;
    private LoginViewModel lvm;

    // Authentication: https://firebase.google.com/docs/auth/android/firebaseui?authuser=0#java
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        lvm = new ViewModelProvider(this).get(LoginViewModel.class);

        getSupportActionBar().hide();

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(view -> {
            createSignInIntent();
        });

        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(view -> {
            openSignUp();
        });

        btnPLogin = findViewById(R.id.btnLogin2);
        btnPLogin.setOnClickListener(view -> {
            Intent intent = new Intent(this, PatientMainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", "aDzD01WbDXcWS4zcx5boZpsrzDl1");
            intent.putExtras(bundle);
            launcher.launch(intent);
        });

        btnCLogin = findViewById(R.id.btnLogin3);
        btnCLogin.setOnClickListener(view -> {
            Intent intent = new Intent(this, CaregiverMainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", "zX8be2WTWjUL04unsrAHqD4sBnG3");
            intent.putExtras(bundle);
            launcher.launch(intent);
        });
    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            Patient patient = lvm.getPatient(user.getUid());
            Caregiver caregiver = lvm.getCaregiver(user.getUid());
            if (patient != null) {
                Log.d(TAG, "Patient login.");
                Intent intent = new Intent(this, PatientMainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", patient.getId());
                intent.putExtras(bundle);
                launcher.launch(intent);
            } else
            if (caregiver != null) {
                Log.d(TAG, "Caregiver login.");
                Intent intent = new Intent(this, CaregiverMainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", caregiver.getId());
                intent.putExtras(bundle);
                launcher.launch(intent);
            } else {
                Context context = getApplicationContext();
                Toast toast = Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT);
                toast.show();
            }
        } else {
            // Failed login
            Context context = getApplicationContext();
            String text = response.getError().getMessage();
            Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void createSignInIntent() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build());

        // Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .build();
        signInLauncher.launch(signInIntent);
    }

    // https://stackoverflow.com/questions/4134117/edittext-on-a-popup-window
    public void openSignUp() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Title");
        alert.setMessage("Message");

        // Set an EditText view to get user input
        final EditText enterEmail = new EditText(this);
        alert.setView(enterEmail);

        final EditText enterPass = new EditText(this);
        alert.setView(enterPass);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {


            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {


            }
        });

        alert.show();
    }

}