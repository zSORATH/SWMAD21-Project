package dk.au.mad21fall.activiboost.ui.shared.home;

import static dk.au.mad21fall.activiboost.Constants.PATIENT;

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

import dk.au.mad21fall.activiboost.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private HomeViewModel hmv;
    private FragmentHomeBinding binding;
    private String name;
    private String uid;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        hmv = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        uid = getArguments().getString("user");
        hmv.setUid(uid);

        if (hmv.getUserType() == PATIENT) {
            name = hmv.getPatient(uid).getName();
        } else {
            name = hmv.getCaregiver(uid).getName();
        }

        final TextView textView = binding.textHome;
        hmv.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s + name);
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