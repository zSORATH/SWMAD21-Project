package dk.au.mad21fall.activiboost.ui.shared.activities;

import static dk.au.mad21fall.activiboost.ui.shared.login.User.CAREGIVER;
import static dk.au.mad21fall.activiboost.ui.shared.login.User.PATIENT;

import android.content.Intent;
import  android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import dk.au.mad21fall.activiboost.MainActivity;
import dk.au.mad21fall.activiboost.ui.shared.activities.suggest.SuggestActivity;
import dk.au.mad21fall.activiboost.ui.shared.activities.add.AddActivity;
import dk.au.mad21fall.activiboost.databinding.FragmentActivitiesBinding;

public class ActivitiesFragment extends Fragment {

    private static final String TAG = "ACTIVITIES FRAGMENT";

    private ActivitiesViewModel activitiesViewModel;
    private FragmentActivitiesBinding binding;
    private int userType;
    private Intent intent;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        activitiesViewModel = new ViewModelProvider(this).get(ActivitiesViewModel.class);

        binding = FragmentActivitiesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textActivities;
        activitiesViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        // userType = this.getArguments().getInt("user");
        userType = getActivity().getIntent().getIntExtra("user", userType);
        final FloatingActionButton btnAdd = binding.btnAdd;
        btnAdd.setOnClickListener(view -> {
            switch (userType) {
                case PATIENT:
                    intent = new Intent(view.getContext(), SuggestActivity.class);
                    break;
                case CAREGIVER:
                    intent = new Intent(view.getContext(), AddActivity.class);
                    break;
                default:
            }
            startActivity(intent);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}