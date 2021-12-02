package dk.au.mad21fall.activiboost.ui.shared.login.signupdetails;

import static dk.au.mad21fall.activiboost.Constants.CAREGIVER;
import static dk.au.mad21fall.activiboost.Constants.PATIENT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Scanner;

import dk.au.mad21fall.activiboost.R;
import dk.au.mad21fall.activiboost.ui.shared.login.signup.SignUpViewModel;

public class SignUpDetailsActivity extends AppCompatActivity {

    private SignUpDetailsViewModel sdvm;
    private EditText txtName, txtAge;
    private Button btnCancel, btnCreate;
    private CheckBox checkPatient, checkCaregiver;
    private String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_details);

        sdvm = new ViewModelProvider(this).get(SignUpDetailsViewModel.class);

        getSupportActionBar().hide();

        txtName = findViewById(R.id.txtName);
        txtAge = findViewById(R.id.txtAge);

        checkPatient = findViewById(R.id.checkPatient);
        checkPatient.setOnClickListener(view -> {
            onCheckBoxClicked(view);
        });

        checkCaregiver = findViewById(R.id.checkCaregiver);
        checkCaregiver.setOnClickListener(view -> {
            onCheckBoxClicked(view);
        });

        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(view -> {
            sdvm.deleteUser();
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Sign up was cancelled",
                    Toast.LENGTH_SHORT);
            toast.show();
            finish();
        });

        btnCreate = findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(view -> {
            if (userType.equals(PATIENT)) {
                tryCreatePatient();
            } else if (userType.equals(CAREGIVER)) {
                tryCreateCaregiver();
            } else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Please choose either patient or caregiver",
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    public void tryCreatePatient() {
        String name = sdvm.getName(txtName.getText().toString()).getValue();
        String age = sdvm.getAge(txtAge.getText().toString()).getValue();

        if (isNameAndAgeValid(name, age)) {
            sdvm.createPatient(name, age);
        }
    }

    public void tryCreateCaregiver() {
        String name = sdvm.getName(txtName.getText().toString()).getValue();
        String age = sdvm.getAge(txtAge.getText().toString()).getValue();

        if (isNameAndAgeValid(name, age)) {
            sdvm.createCaregiver(name, age);
        }
    }

    // Apapted from https://developer.android.com/guide/topics/ui/controls/checkbox#java
    public void onCheckBoxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch(view.getId()) {
            case R.id.checkPatient:
                if (checked) {
                    if (userType == CAREGIVER) { userType = null; }
                    else { userType = PATIENT; }
                } else {
                    if (userType == PATIENT) { userType = null; }
                    else { userType = CAREGIVER; }
                }
                break;
            case R.id.checkCaregiver:
                if (checked) {
                    if (userType == PATIENT) { userType = null; }
                    else { userType = CAREGIVER; }
                } else {
                    if (userType == CAREGIVER) { userType = null; }
                    else { userType = PATIENT; }
                }
                break;
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
}