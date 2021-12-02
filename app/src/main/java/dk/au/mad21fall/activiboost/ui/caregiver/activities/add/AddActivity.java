package dk.au.mad21fall.activiboost.ui.caregiver.activities.add;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import dk.au.mad21fall.activiboost.R;
import dk.au.mad21fall.activiboost.models.Activity;

public class AddActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private EditText title, description;
    private Button addBtn, cancelBtn
            ;
    private AddActivityViewModel addActivityViewModel;
    private String activitytitle, activitytime, activitydate, activitydesctiption;
    private Calendar mC;
    private TextView dateView;
    private LiveData<Date> date;

    public AddActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        title = findViewById(R.id.addActivityTitle);
        dateView = findViewById(R.id.addActivityDate);
        description = findViewById(R.id.addActivityDescription);
        addBtn = findViewById(R.id.addActivityBtn);
        cancelBtn = findViewById(R.id.cancelBtn);
        mC = Calendar.getInstance();

        addActivityViewModel = new ViewModelProvider(this).get(AddActivityViewModel.class);

        activitytitle = (String) getIntent().getSerializableExtra("title");
        activitydesctiption = (String) getIntent().getSerializableExtra("description");
        updateUI();

        date = addActivityViewModel.getDate();
        date.observe(this, new Observer<Date>() {
            @Override
            public void onChanged(Date date) {
                Date d = date;
                dateView.setText(d.toString());
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveActivity();
                Toast.makeText(getApplicationContext(), getText(R.string.activityAdded), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Help with date from: https://www.tutorialspoint.com/how-to-use-date-time-picker-in-android
        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = mC.get(Calendar.YEAR);
                int month = mC.get(Calendar.MONTH);
                int day = mC.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddActivity.this, AddActivity.this, year, month, day);
                datePickerDialog.show();
            }
        });

    }

    private void updateUI() {
        title.setText(activitytitle);
        description.setText(activitydesctiption);
    }

    private void saveActivity() {
        Activity a = new Activity();
        a.setActivityName(title.getText().toString());
        a.setDescription(description.getText().toString());
        a.setTime(date.getValue());
        Map<String, String> caregivers = new HashMap<>();
        a.setCaregivers(caregivers);
        Map<String, String> patients = new HashMap<>();
        a.setPatients(patients);
        addActivityViewModel.saveActivity(a);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mC.set(Calendar.YEAR, year);
        mC.set(Calendar.MONTH, month);
        mC.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        int hour = mC.get(Calendar.HOUR);
        int minute = mC.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(AddActivity.this, AddActivity.this, hour, minute, true);
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mC.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mC.set(Calendar.MINUTE, minute);
        addActivityViewModel.setDate(mC.getTime());
       // dateView.setText(date.toString());



    }
}