package dk.au.mad21fall.activiboost.ui.caregiver.patients;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import dk.au.mad21fall.activiboost.Constants;
import dk.au.mad21fall.activiboost.R;
import dk.au.mad21fall.activiboost.databinding.FragmentPatientsBinding;
import dk.au.mad21fall.activiboost.models.Patient;

public class PatientsFragment extends Fragment {

    private RecyclerView rcvPatients;
    private PatientsAdapter adapter;

    private PatientsViewModel patientsViewModel;
    private FragmentPatientsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPatientsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Context context = getContext();
        adapter = new PatientsAdapter(context);
        rcvPatients = root.findViewById(R.id.rcv_patients);
        rcvPatients.setLayoutManager(new LinearLayoutManager(root.getContext()));
        rcvPatients.setAdapter(adapter);

        // Making sure that a new ViewModel only is made if there isn't one already
        if (patientsViewModel == null) {
            Application app = getActivity().getApplication();
            patientsViewModel = new ViewModelProvider(this).get(PatientsViewModel.class);
            patientsViewModel.CreateRepository(app);
        }

        // Updating the movies in the app upon any change
        patientsViewModel.GetPatientLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Patient>>() {
            @Override
            public void onChanged(ArrayList<Patient> patients) {
                adapter.updatePatientList(patients);
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