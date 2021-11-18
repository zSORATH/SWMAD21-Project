package dk.au.mad21fall.activiboost.ui.caregiver.patients;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import dk.au.mad21fall.activiboost.databinding.FragmentPatientsBinding;

public class PatientsFragment extends Fragment {

    private PatientsViewModel patientsViewModel;
    private FragmentPatientsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        patientsViewModel =
                new ViewModelProvider(this).get(PatientsViewModel.class);

        binding = FragmentPatientsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textPatients;
        patientsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}