package dk.au.mad21fall.activiboost.ui.patient.diary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.hsalf.smilerating.SmileRating;
import com.hsalf.smileyrating.SmileyRating;

import dk.au.mad21fall.activiboost.Constants;
import dk.au.mad21fall.activiboost.R;
import dk.au.mad21fall.activiboost.models.Diary;

public class DiaryEditActivity extends AppCompatActivity {

    SmileyRating smiley_rating;
    TextView diary_edit_date;
    EditText diary_edit_textfield;
    Button button_diary_cancel, button_diary_savechanges, button_diary_delete;

    DiaryEditViewModel diaryEditViewModel;

    String date;
    Diary diary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_diary);

        Intent intent = getIntent();
        date = intent.getStringExtra(Constants.DIARY_DATE);

        if(diaryEditViewModel == null) {
            diaryEditViewModel = new ViewModelProvider(this).get(DiaryEditViewModel.class);
            diaryEditViewModel.CreateEditRepository(getApplication(), date);
        }

        diary = diaryEditViewModel.getDiary(date);

        setupUI();
    }

    private void setupUI() {

        diary_edit_date = findViewById(R.id.diary_edit_date);
        diary_edit_textfield = findViewById(R.id.diary_edit_textfeld);

        smiley_rating = findViewById(R.id.smiley_rating);
        smiley_rating.setRating(diary.getRating());

        smiley_rating.setSmileySelectedListener(new SmileyRating.OnSmileySelectedListener() {
            @Override
            public void onSmileySelected(SmileyRating.Type type) {
                int rating = type.getRating();
                diary.setRating(rating);
                diaryEditViewModel.setDiary(diary);
            }
        });

        button_diary_cancel = findViewById(R.id.button_diary_cancel);
        button_diary_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        
        button_diary_savechanges = findViewById(R.id.button_diary_savechanges);
        button_diary_savechanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });
        
        button_diary_delete = findViewById(R.id.button_diary_delete);
        button_diary_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });
    }

    private void cancel() {
    }

    private void saveChanges() {
    }

    private void delete() {
    }
}
