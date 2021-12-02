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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import dk.au.mad21fall.activiboost.R;
import dk.au.mad21fall.activiboost.databinding.FragmentCaregiverActivitiesBinding;
import dk.au.mad21fall.activiboost.databinding.FragmentCaregiverActivitiesBinding;
import dk.au.mad21fall.activiboost.models.Activity;
import dk.au.mad21fall.activiboost.ui.patient.activities.PatientActivitiesViewModel;
import dk.au.mad21fall.activiboost.ui.shared.activities.ActivitiesAdapter;
import dk.au.mad21fall.activiboost.ui.shared.activities.MyActivitiesAdapter;

public class CaregiverActivitiesFragment extends Fragment implements ActivitiesAdapter.IActivitiesItemClickedListener, MyActivitiesAdapter.IMyActivitiesItemClickedListener {

    private static final String TAG = "ACTIVITIES FRAGMENT";

    private CaregiverActivitiesViewModel activitiesViewModel;
    private FragmentCaregiverActivitiesBinding binding;
    private TextView firstTextView, secondTextView;
    private ActivitiesAdapter activitiesAdapter;
    private MyActivitiesAdapter sugActivitiesAdapter;
    private RecyclerView rcvSugActivities, rcvActivities;
    private LiveData<ArrayList<Activity>> lactivities, lsugactivities;
    private ArrayList<Activity> activities, sugactivities;
    private String userId = "LKP";
    private String username = "Line";



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        activitiesViewModel = new ViewModelProvider(this).get(CaregiverActivitiesViewModel.class);

        binding = FragmentCaregiverActivitiesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //activities = new ArrayList<Activity>();
        //myActivities = new ArrayList<Activity>();
        firstTextView = binding.sugActivitiesText;
        firstTextView.setText(R.string.sugActivities);
        secondTextView = binding.textActivities;
        secondTextView.setText(R.string.Activities);
        activitiesAdapter = new ActivitiesAdapter(this);
        sugActivitiesAdapter = new MyActivitiesAdapter(this);

        rcvActivities = binding.rcvActivities;
        rcvActivities.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcvActivities.setAdapter(activitiesAdapter);


        rcvSugActivities = binding.rcvSuggestedActivities;
        rcvSugActivities.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcvSugActivities.setAdapter(sugActivitiesAdapter);

        getActivities();

        return root;
    }

    private void getActivities(){
        lactivities = activitiesViewModel.getActivities(userId);
        lactivities.observe(getActivity(), new Observer<ArrayList<Activity>>() {
            @Override
            public void onChanged(ArrayList<Activity> nactivities) {
                activitiesAdapter.updateActivitiesList(nactivities);
                activities = nactivities;
            }
        });
        lsugactivities = activitiesViewModel.getSugActivities(userId);
        lsugactivities.observe(getActivity(), new Observer<ArrayList<Activity>>() {
            @Override
            public void onChanged(ArrayList<Activity> nactivities) {
                sugActivitiesAdapter.updateActivitiesList(nactivities);
                sugactivities = nactivities;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onActivityClicked(int index) {
        ArrayList<Activity> al = activities;
        showDialogue(al.get(index));
    }

    @Override
    public void onMyActivityClicked(int index) {
        ArrayList<Activity> al = sugactivities;
        showMyDialogue(al.get(index));
    }

    //show a dialogue
    private void showMyDialogue(Activity a){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(a.getActivityName())
                .setMessage(getText(R.string.description) +" " + a.getDescription())
                .setPositiveButton(R.string.addBtn, (dialogInterface, i) -> {})
                .setNegativeButton(R.string.deleteBtn, (dialogInterface, i) -> {deleteSugActivity(a);});
        builder.create().show();
    }

    private void showDialogue(Activity a){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(a.getActivityName())
                .setMessage(getText(R.string.description) +" " + a.getDescription() + "\n\n" +
                        getText(R.string.time) +" " + a.getTime() + "\n\n" +
                        getText(R.string.participants) +" " + listOf(a.getPatients().values()) + "\n\n" +
                        getText(R.string.caregivers) +" " + listOf(a.getCaregivers().values()) )
                .setPositiveButton(R.string.addBtn, (dialogInterface, i) -> {addToActivity(a);})
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> {});
        builder.create().show();
    }

    @Override
    public void addToActivity(Activity a){
        Map<String, String> caregivers = a.getCaregivers();
        caregivers.put(userId,username);
        a.setCaregivers(caregivers);
        activitiesViewModel.addUserToActivity(userId,a);
        getActivities();
    }

    @Override
    public void unsubscribeActivity(Activity a){
        Map<String, String> caregivers = a.getCaregivers();
        caregivers.remove(userId);
        a.setCaregivers(caregivers);
        activitiesViewModel.addUserToActivity(userId,a);
        getActivities();
    }

    private String listOf(Collection<String> c){
        String s = new String();
        for (String name : c){
            if(s.equals("")) {
                s = name;
            }
            else {
                s = s + ", " + name;
            }
        }
        return s;
    }

    private void deleteSugActivity(Activity a){
        activitiesViewModel.deleteSugActivity(a);
    }



}