package dk.au.mad21fall.activiboost.ui.shared.activities.suggest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import dk.au.mad21fall.activiboost.R;

public class SuggestActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);

        textView = findViewById(R.id.text_suggest);
        textView.setText("This is suggest activity");
    }
}