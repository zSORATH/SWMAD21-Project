package dk.au.mad21fall.activiboost.ui.patient.diary;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import dk.au.mad21fall.activiboost.Constants;
import dk.au.mad21fall.activiboost.R;
import dk.au.mad21fall.activiboost.databinding.FragmentDiaryBinding;
import dk.au.mad21fall.activiboost.models.Diary;
import dk.au.mad21fall.activiboost.models.Patient;
import dk.au.mad21fall.activiboost.ui.caregiver.patients.PatientsAdapter;
import dk.au.mad21fall.activiboost.ui.caregiver.patients.PatientsViewModel;

public class DiaryFragment extends Fragment implements DiaryAdapter.IDiaryItemClickedListener {

    private RecyclerView rcvDiaries;
    private DiaryAdapter adapter;

    private DiaryViewModel diaryViewModel;
    private FragmentDiaryBinding binding;

    Button button_add_diary;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDiaryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        adapter = new DiaryAdapter(this);
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
        diaryViewModel.GetDiaryLiveData().observe(getViewLifecycleOwner(), new Observer<List<Diary>>() {
            @Override
            public void onChanged(List<Diary> diaries) {
                adapter.updateDiaryList(diaries);
            }
        });

        button_add_diary = root.findViewById(R.id.button_add_diary);
        button_add_diary.setOnClickListener(view -> {
            addDiary();
        });

        return root;
    }

    private void addDiary() {
        //Getting the date is inspired from https://stackoverflow.com/questions/8654990/how-can-i-get-current-date-in-android
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        if(diaryViewModel.isStored(currentDate)) {
            Toast.makeText(getActivity(), "You already added a diary for today.", Toast.LENGTH_SHORT).show();
            return;
        }
        diaryViewModel.addDiary("",-1, currentDate);

        Intent intent = new Intent(getContext(), DiaryEditActivity.class);
        intent.putExtra(Constants.DIARY_DATE, currentDate);
        startActivity(intent);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onDiaryClicked(int index) {
        String diaryDate = diaryViewModel.getDiary(index).getDate();
        Intent intent = new Intent(getContext(), DiaryEditActivity.class);
        intent.putExtra(Constants.DIARY_DATE, diaryDate);
        startActivity(intent);
    }
}