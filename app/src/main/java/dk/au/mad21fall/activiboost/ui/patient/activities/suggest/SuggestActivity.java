package dk.au.mad21fall.activiboost.ui.patient.activities.suggest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import dk.au.mad21fall.activiboost.R;
import dk.au.mad21fall.activiboost.models.Activity;
import dk.au.mad21fall.activiboost.ui.patient.activities.suggest.SuggestViewModel;

public class SuggestActivity extends AppCompatActivity {

    private EditText title, description;
    private Button suggestBtn, cancelBtn;
    private SuggestViewModel suggestViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);

        suggestBtn = findViewById(R.id.suggestBtn);
        cancelBtn = findViewById(R.id.suggestCancelBtn);
        title = findViewById(R.id.titleSuggestActivity);
        description = findViewById(R.id.descriptionSuggestActivity);

        suggestViewModel = new ViewModelProvider(this).get(SuggestViewModel.class);

        suggestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stitle = title.getText().toString();
                String sdescription = description.getText().toString();
                if(!stitle.equals("")){
                suggestViewModel.suggestActivity(title.getText().toString(), description.getText().toString());
                title.setText("");
                description.setText("");
                Toast.makeText(getApplicationContext(),getText(R.string.activitySuggested), Toast.LENGTH_SHORT).show();
                finish();
                }
                else {
                    Toast.makeText(getApplicationContext(),getText(R.string.textEmpty), Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}