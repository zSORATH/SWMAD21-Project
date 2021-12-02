package dk.au.mad21fall.activiboost.ui.caregiver.activities.add;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import dk.au.mad21fall.activiboost.R;
import dk.au.mad21fall.activiboost.models.Activity;
import dk.au.mad21fall.activiboost.ui.patient.activities.PatientActivitiesViewModel;

public class AddActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private EditText title, description;
    private Button addBtn;
    private AddActivityViewModel addActivityViewModel;
    private String activitytitle, activitytime, activitydate, activitydesctiption;
    private Calendar mCalendar;
    private TextView date;

    public AddActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        title = findViewById(R.id.addActivityTitle);
        date = findViewById(R.id.addActivityDate);
        description = findViewById(R.id.addActivityDescription);
        addBtn = findViewById(R.id.addActivityBtn);
        mCalendar = Calendar.getInstance();

        addActivityViewModel = new ViewModelProvider(this).get(AddActivityViewModel.class);

        activitytitle = (String) getIntent().getSerializableExtra("title");
        activitydesctiption = (String) getIntent().getSerializableExtra("description");
        updateUI();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveActivity();
                Toast.makeText(getApplicationContext(), getText(R.string.activityAdded), Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        //Help with date from: https://www.tutorialspoint.com/how-to-use-date-time-picker-in-android
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
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
        addActivityViewModel.saveActivity(a);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        int hour = mCalendar.get(Calendar.HOUR);
        int minute = mCalendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(AddActivity.this, AddActivity.this, hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mCalendar.set(Calendar.HOUR, hourOfDay);
        mCalendar.set(Calendar.MINUTE, minute);
        date.setText(mCalendar.getTime().toString());



    }
}