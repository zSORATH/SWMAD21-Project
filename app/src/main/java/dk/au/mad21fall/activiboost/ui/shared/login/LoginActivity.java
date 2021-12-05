package dk.au.mad21fall.activiboost.ui.shared.login;

import static dk.au.mad21fall.activiboost.Constants.SIGN_UP_CANCELED;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import dk.au.mad21fall.activiboost.CaregiverMainActivity;
import dk.au.mad21fall.activiboost.PatientMainActivity;
import dk.au.mad21fall.activiboost.R;
import dk.au.mad21fall.activiboost.ui.shared.login.signup.SignUpActivity;

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
                    Toast toast;
                    lvm.reinstateUsers();
                    int from = getIntent().getIntExtra("from", -1);
                    if (result.getResultCode() == RESULT_OK) {
                        toast = Toast.makeText(getApplicationContext(),
                                R.string.user_created,
                                Toast.LENGTH_SHORT);
                        toast.show();
                    } else if (from == SIGN_UP_CANCELED) { // only show this Toast if signup is canceled
                        toast = Toast.makeText(getApplicationContext(),
                                "Sign up was cancelled",
                                Toast.LENGTH_SHORT);
                        toast.show();
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
            gotoSignUp();
        });
    }

    // Authentication: https://firebase.google.com/docs/auth/android/firebaseui?authuser=0#java
    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        Context context = getApplicationContext();
        String text = "";
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if (lvm.patientExists(user.getUid())) {
                Log.d(TAG, "Patient login.");
                Intent intent = new Intent(this, PatientMainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user.getUid());
                intent.putExtras(bundle);
                launcher.launch(intent);
            } else
            if (lvm.caregiverExists(user.getUid())) {
                Log.d(TAG, "Caregiver login.");
                Intent intent = new Intent(this, CaregiverMainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user.getUid());
                intent.putExtras(bundle);
                launcher.launch(intent);
            } else {
                text = getText(R.string.login_failed).toString();
            }
        } else {
            // Failed login
            if (response == null) {
                text = getText(R.string.login_failed).toString();
            }
            else {
                text = response.getError().getLocalizedMessage();
            }
        }
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
    }

    // Authentication: https://firebase.google.com/docs/auth/android/firebaseui?authuser=0#java
    public void createSignInIntent() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build());

        // Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .setTheme(R.style.Theme_ActiviBoost)
                .setLogo(R.drawable.custom_icon_foreground)
                .build();
        signInLauncher.launch(signInIntent);
    }

    public void gotoSignUp() {
        Intent intent = new Intent(this, SignUpActivity.class);
        launcher.launch(intent);
    }
}