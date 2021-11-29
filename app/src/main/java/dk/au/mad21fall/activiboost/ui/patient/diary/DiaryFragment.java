package dk.au.mad21fall.activiboost.ui.patient.diary;

import android.app.Application;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import dk.au.mad21fall.activiboost.R;
import dk.au.mad21fall.activiboost.databinding.FragmentDiaryBinding;
import dk.au.mad21fall.activiboost.models.Diary;
import dk.au.mad21fall.activiboost.models.Patient;
import dk.au.mad21fall.activiboost.ui.caregiver.patients.PatientsAdapter;
import dk.au.mad21fall.activiboost.ui.caregiver.patients.PatientsViewModel;

public class DiaryFragment extends Fragment {

    private RecyclerView rcvDiaries;
    private DiaryAdapter adapter;

    private DiaryViewModel diaryViewModel;
    private FragmentDiaryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDiaryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        adapter = new DiaryAdapter(root);
        rcvDiaries = root.findViewById(R.id.rcv_diaries);
        rcvDiaries.setLayoutManager(new LinearLayoutManager(root.getContext()));
        rcvDiaries.setAdapter(adapter);

        // Making sure that a new View  Model    only is made if there isn't one already
        if (diaryViewModel == null) {
            Application app = getActivity().getApplication();
            diaryViewModel = new ViewModelProvider(this).get(DiaryViewModel.class);
            diaryViewModel.CreateRepository(app);
        }

        // Updating the movies in the app upon any change
        diaryViewModel.GetDiaryLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Diary>>() {
            @Override
            public void onChanged(ArrayList<Diary> diaries) {
                adapter.updateDiaryList(diaries);
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