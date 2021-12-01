package dk.au.mad21fall.activiboost.ui.caregiver.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import dk.au.mad21fall.activiboost.R;
import dk.au.mad21fall.activiboost.databinding.FragmentCaregiverActivitiesBinding;
import dk.au.mad21fall.activiboost.databinding.FragmentCaregiverActivitiesBinding;
import dk.au.mad21fall.activiboost.models.Activity;
import dk.au.mad21fall.activiboost.ui.patient.activities.PatientActivitiesViewModel;
import dk.au.mad21fall.activiboost.ui.shared.activities.ActivitiesAdapter;
import dk.au.mad21fall.activiboost.ui.shared.activities.MyActivitiesAdapter;

public class CaregiverActivitiesFragment extends Fragment implements ActivitiesAdapter.IActivitiesItemClickedListener, MyActivitiesAdapter.IMyActivitiesItemClickedListener {

    private static final String TAG = "ACTIVITIES FRAGMENT";

    private PatientActivitiesViewModel activitiesViewModel;
    private FragmentCaregiverActivitiesBinding binding;
    private int userType;
    private Intent intent;
    private TextView firstTextView, secondTextView;
    private ActivitiesAdapter activitiesAdapter;
    private MyActivitiesAdapter myActivitiesAdapter;
    private RecyclerView rcvMyActivities, rcvActivities;
    private LiveData<ArrayList<Activity>> lactivities, lmyactivities;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        activitiesViewModel = new ViewModelProvider(this).get(PatientActivitiesViewModel.class);

        binding = FragmentCaregiverActivitiesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //activities = new ArrayList<Activity>();
        //myActivities = new ArrayList<Activity>();
        firstTextView = binding.sugActivitiesText;
        firstTextView.setText(R.string.myActivities);
        secondTextView = binding.textActivities;
        secondTextView.setText(R.string.Activities);
        activitiesAdapter = new ActivitiesAdapter(this);
        myActivitiesAdapter = new MyActivitiesAdapter(this);
        rcvActivities = binding.rcvActivities;
        rcvActivities.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcvActivities.setAdapter(activitiesAdapter);

        // TODO: This is probably not right
        rcvMyActivities = binding.rcvSuggestedActivities;
        rcvMyActivities.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcvMyActivities.setAdapter(myActivitiesAdapter);




        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onActivityClicked(int index) {
        // ArrayList<Activity> al = activities;
        // showDialogue(al.get(index));
    }

    @Override
    public void addToActivity(Activity a){

    }

    //show a dialogue
    private void showDialogue(Activity a){
        //create a dialogue popup - and show it
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(a.getActivityName())
                .setMessage("")
                .setPositiveButton(R.string.ok, (dialogInterface, i) -> {})
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> {});
        builder.create().show();
    }



}