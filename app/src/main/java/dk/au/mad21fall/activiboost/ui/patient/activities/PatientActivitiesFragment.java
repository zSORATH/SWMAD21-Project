package dk.au.mad21fall.activiboost.ui.patient.activities;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import  android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import dk.au.mad21fall.activiboost.models.Activity;
import dk.au.mad21fall.activiboost.databinding.FragmentPatientActivitiesBinding;
import dk.au.mad21fall.activiboost.ui.patient.activities.suggest.SuggestActivity;
import dk.au.mad21fall.activiboost.ui.shared.activities.ActivitiesAdapter;
import dk.au.mad21fall.activiboost.ui.shared.activities.MyActivitiesAdapter;

public class PatientActivitiesFragment extends Fragment implements ActivitiesAdapter.IActivitiesItemClickedListener, MyActivitiesAdapter.IMyActivitiesItemClickedListener {

    private static final String TAG = "ACTIVITIES FRAGMENT";

    private PatientActivitiesViewModel activitiesViewModel;
    private FragmentPatientActivitiesBinding binding;
    private int userType;
    private Intent intent;
    private TextView firstTextView, secondTextView;
    private ActivitiesAdapter activitiesAdapter;
    private MyActivitiesAdapter myActivitiesAdapter;
    private RecyclerView rcvMyActivities, rcvActivities;
    private LiveData<ArrayList<Activity>> lactivities, lmyactivities;
    private ArrayList<Activity> activities, myactivities;
    private Button suggestBtn;
    private String userId = "1234567899";
    private String username = "John";



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        activitiesViewModel = new ViewModelProvider(this).get(PatientActivitiesViewModel.class);

        binding = FragmentPatientActivitiesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //activities = new ArrayList<Activity>();
        //myActivities = new ArrayList<Activity>();
        firstTextView = binding.activityTextView1;
        firstTextView.setText(R.string.myActivities);
        secondTextView = binding.activityTextView2;
        secondTextView.setText(R.string.Activities);
        activitiesAdapter = new ActivitiesAdapter(this);
        myActivitiesAdapter = new MyActivitiesAdapter(this);
        rcvActivities = binding.activityRCV2;
        rcvActivities.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcvActivities.setAdapter(activitiesAdapter);

        rcvMyActivities = binding.activityRCV1;
        rcvMyActivities.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcvMyActivities.setAdapter(myActivitiesAdapter);

        getActivities();

        suggestBtn = binding.btnAddActivity;
        suggestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getActivity(), SuggestActivity.class);
                launcher.launch(intent);
            }
        });


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
        lmyactivities = activitiesViewModel.getMyActivities(userId);
        lmyactivities.observe(getActivity(), new Observer<ArrayList<Activity>>() {
            @Override
            public void onChanged(ArrayList<Activity> nactivities) {
                myActivitiesAdapter.updateActivitiesList(nactivities);
                myactivities = nactivities;
            }
        });
    }


    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                    }
                }
            });

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
        ArrayList<Activity> al = myactivities;
        showMyDialogue(al.get(index));
    }

    @Override
    public void addToActivity(Activity a){
        Map<String, String> patients = a.getPatients();
        patients.put(userId,username);
        a.setPatients(patients);
        activitiesViewModel.addUserToActivity(userId,a);
        getActivities();
    }

    //show a dialogue
    private void showMyDialogue(Activity a){
        //create a dialogue popup - and show it
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(a.getActivityName())
                .setMessage(getText(R.string.description) +" " + a.getDescription() + "\n\n" +
                        getText(R.string.time) +" " + a.getTime() + "\n\n" +
                        getText(R.string.participants) +" " + listOf(a.getPatients().values()) + "\n\n" +
                        getText(R.string.caregivers) +" " + listOf(a.getCaregivers().values()) )
                .setPositiveButton(R.string.ok, (dialogInterface, i) -> {})
                .setNegativeButton(R.string.unsubscribe, (dialogInterface, i) -> {unsubscribeActivity(a);});
        builder.create().show();
    }

    private void showDialogue(Activity a){
        //create a dialogue popup - and show it
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
    public void unsubscribeActivity(Activity a){
        Map<String, String> patients = a.getPatients();
        patients.remove(userId);
        a.setPatients(patients);
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


}