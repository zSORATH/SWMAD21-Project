package dk.au.mad21fall.activiboost.ui.patient.diary;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
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
    TextView diary_edit_textfield;
    Button button_diary_cancel, button_diary_savechanges, button_diary_delete;

    DiaryEditViewModel diaryEditViewModel;

    String date;
    Diary diary;
    int rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_diary);

        Intent intent = getIntent();
        date = intent.getStringExtra(Constants.DIARY_DATE);

        if(diaryEditViewModel == null) {
            diaryEditViewModel = new ViewModelProvider(this).get(DiaryEditViewModel.class);
        }

        diary = diaryEditViewModel.getDiary(date);

        setupUI();
    }

    private void setupUI() {

        diary_edit_date = findViewById(R.id.diary_edit_date);
        diary_edit_textfield = findViewById(R.id.diary_edit_textfeld);

        // Smiley rating is inspired by https://github.com/sujithkanna/SmileyRating
        smiley_rating = findViewById(R.id.smiley_rating);
        smiley_rating.setRating(diary.getRating());

        smiley_rating.setSmileySelectedListener(new SmileyRating.OnSmileySelectedListener() {
            @Override
            public void onSmileySelected(SmileyRating.Type type) {
                rating = type.getRating();
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
        finish();
    }

    private void saveChanges() {
        diary.setRating(rating);
        diary.setContent(diary_edit_textfield.getText().toString());
        diaryEditViewModel.setDiary(diary);
        setResult(RESULT_OK);
        finish();
    }

    //TODO: LAV STRINGS TIL DENNE PROMT SÅ DER KAN VÆRE FLERE SPROG
    private void delete() {
        // Promt is inspired by https://stackoverflow.com/questions/2257963/how-to-show-a-dialog-to-confirm-that-the-user-wishes-to-exit-an-android-activity
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Delete Diary")
                .setMessage("Are you sure you want to delete this diary entry?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        diaryEditViewModel.deleteDiary(diary);
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
}
