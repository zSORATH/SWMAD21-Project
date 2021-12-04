package dk.au.mad21fall.activiboost.ui.shared.login.signupdetails;

import static dk.au.mad21fall.activiboost.Constants.CAREGIVER;
import static dk.au.mad21fall.activiboost.Constants.PATIENT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Scanner;

import dk.au.mad21fall.activiboost.R;
import dk.au.mad21fall.activiboost.models.Patient;
import dk.au.mad21fall.activiboost.ui.shared.login.signup.SignUpViewModel;

public class SignUpDetailsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "SIGN UP DETAILS";

    private SignUpDetailsViewModel sdvm;
    private EditText txtName, txtAge;
    private Button btnCancel, btnCreate;
    private Spinner spinnerUserType;
    private String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_details);

        sdvm = new ViewModelProvider(this).get(SignUpDetailsViewModel.class);

        getSupportActionBar().hide();

        txtName = findViewById(R.id.txtName);
        txtAge = findViewById(R.id.txtAge);

        // Adapted from: https://developer.android.com/guide/topics/ui/controls/spinner#java
        spinnerUserType = findViewById(R.id.spinnerUserType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.user_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUserType.setAdapter(adapter);

        spinnerUserType.setOnItemSelectedListener(this);

        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(view -> {
            sdvm.deleteUser();
            setResult(RESULT_CANCELED);
            finish();
        });

        btnCreate = findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(view -> {
            if (userType.equals(PATIENT)) {
                if (tryCreatePatient()) {
                    setResult(RESULT_OK);
                    finish();
                }
            } else if (userType.equals(CAREGIVER)) {
                if (tryCreateCaregiver()) {
                    setResult(RESULT_OK);
                    finish();
                }
            } else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Please choose either patient or caregiver",
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    public boolean tryCreatePatient() {
        String name = sdvm.getName(txtName.getText().toString()).getValue();
        String age = sdvm.getAge(txtAge.getText().toString()).getValue();

        if (isNameAndAgeValid(name, age)) {
            sdvm.createPatient(name, age);
            return true;
        } else {
            return false;
        }
    }

    public boolean tryCreateCaregiver() {
        String name = sdvm.getName(txtName.getText().toString()).getValue();
        String age = sdvm.getAge(txtAge.getText().toString()).getValue();

        if (isNameAndAgeValid(name, age)) {
            sdvm.createCaregiver(name, age);
            return true;
        } else {
            return false;
        }
    }

    public boolean isNameAndAgeValid(String name, String age) {
        Toast toast;
        if (name == null || age == null || name.equals("") || age.equals("")) {
            toast = Toast.makeText(getApplicationContext(),
                    "Please fill both name and age",
                    Toast.LENGTH_SHORT);
            toast.show();
            return false;
        } else if (!isInteger(age, 10)) {
            toast = Toast.makeText(getApplicationContext(),
                    "Please enter age as a whole number",
                    Toast.LENGTH_SHORT);
            toast.show();
            return false;
        } else {
            return true;
        }
    }

    // From https://stackoverflow.com/questions/5439529/determine-if-a-string-is-an-integer-in-java
    public static boolean isInteger(String s, int radix) {
        Scanner sc = new Scanner(s.trim());
        if(!sc.hasNextInt(radix)) return false;
        sc.nextInt(radix);
        return !sc.hasNext();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getItemAtPosition(i) == "Patient") {
            userType = PATIENT;
        } else {
            userType = CAREGIVER;
        }
        Log.d(TAG, "User type: " + userType);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        userType = null;
    }
}