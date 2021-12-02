package dk.au.mad21fall.activiboost.ui.caregiver.activities.add;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import dk.au.mad21fall.activiboost.R;
import dk.au.mad21fall.activiboost.models.Activity;
import dk.au.mad21fall.activiboost.ui.patient.activities.PatientActivitiesViewModel;

public class AddActivity extends AppCompatActivity {

    private EditText title, time, date, description;
    private Button addBtn;
    private AddActivityViewModel addActivityViewModel;
    private String activitytitle, activitytime, activitydate, activitydesctiption;

    public AddActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        title = findViewById(R.id.addActivityTitle);
        time = findViewById(R.id.addActivityTime);
        date = findViewById(R.id.addActivityDate);
        description = findViewById(R.id.addActivityDescription);
        addBtn = findViewById(R.id.addActivityBtn);

        addActivityViewModel = new ViewModelProvider(this).get(AddActivityViewModel.class);

        activitytitle = (String) getIntent().getSerializableExtra("title");
        activitydesctiption = (String) getIntent().getSerializableExtra("title");
        updateUI();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveActivity();
            }
        });

    }

    private void updateUI(){
        title.setText(activitytitle);
        description.setText(activitydesctiption);
    }

    private void saveActivity(){
        Activity a = new Activity();
        a.setActivityName(title.getText().toString());
        a.setDescription(description.getText().toString());
        addActivityViewModel.saveActivity(a);
    }
}