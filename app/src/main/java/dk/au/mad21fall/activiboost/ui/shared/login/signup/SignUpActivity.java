package dk.au.mad21fall.activiboost.ui.shared.login.signup;

import static dk.au.mad21fall.activiboost.Constants.SIGN_UP_CANCELED;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.Nullable;

import dk.au.mad21fall.activiboost.R;
import dk.au.mad21fall.activiboost.ui.shared.login.LoginViewModel;
import dk.au.mad21fall.activiboost.ui.shared.login.signupdetails.SignUpDetailsActivity;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SIGN UP ACTIVITY";

    private SignUpViewModel svm;
    private EditText txtEmail, txtPassword;
    private Button btnCancel, btnNext;

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        setResult(RESULT_OK);
                    } else {
                        svm.deleteUser();
                        Intent i = new Intent();
                        Bundle b = new Bundle();
                        b.putInt("from", SIGN_UP_CANCELED);
                        i.putExtras(b);
                        setResult(RESULT_CANCELED, i);
                    }
                    finish();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        svm = new ViewModelProvider(this).get(SignUpViewModel.class);

        getSupportActionBar().setTitle(getText(R.string.sign_up));

        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPass);

        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(view -> {
            setResult(RESULT_CANCELED);
            finish();
        });

        btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(view -> {
            tryCreateUser();
        });
    }

    public void tryCreateUser() {
        String email = svm.getEmail(txtEmail.getText().toString()).getValue();
        String password = svm.getPassword(txtPassword.getText().toString()).getValue();

        FirebaseAuth fba = FirebaseAuth.getInstance();

        if (email == null || password == null || email.equals("") || password.equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    getText(R.string.fill_in_email_password),
                    Toast.LENGTH_SHORT);
            toast.show();
        } else if (password.length() < 6) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    getText(R.string.pass_min_length),
                    Toast.LENGTH_SHORT);
            toast.show();
        } else {
            fba.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            goToSignUpDetails();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    e.getLocalizedMessage(),
                                    Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
        }
    }

    public void goToSignUpDetails() {
        Intent intent = new Intent(this, SignUpDetailsActivity.class);
        launcher.launch(intent);
    }
}