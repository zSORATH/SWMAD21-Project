package dk.au.mad21fall.activiboost.ui.shared.activities.add;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import dk.au.mad21fall.activiboost.R;

public class AddActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        textView = findViewById(R.id.text_add);
        textView.setText("This is add activity");
    }
}