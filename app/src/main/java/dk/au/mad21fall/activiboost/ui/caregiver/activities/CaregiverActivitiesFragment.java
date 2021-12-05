package dk.au.mad21fall.activiboost.ui.caregiver.activities;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import dk.au.mad21fall.activiboost.PatientMainActivity;
import dk.au.mad21fall.activiboost.R;
import dk.au.mad21fall.activiboost.databinding.FragmentCaregiverActivitiesBinding;
import dk.au.mad21fall.activiboost.databinding.FragmentCaregiverActivitiesBinding;
import dk.au.mad21fall.activiboost.models.Activity;
import dk.au.mad21fall.activiboost.models.Caregiver;
import dk.au.mad21fall.activiboost.models.Patient;
import dk.au.mad21fall.activiboost.ui.caregiver.activities.add.AddActivity;
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
    private String userId;
    private LiveData<Caregiver> caregiver;
    private Button add;
    private Activity sa;
    private EditText searchString;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        activitiesViewModel = new ViewModelProvider(this).get(CaregiverActivitiesViewModel.class);

        binding = FragmentCaregiverActivitiesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Get the userid from signed ind user
        userId = (String) getActivity().getIntent().getSerializableExtra("user");

        add = binding.btnAdd;
        searchString = binding.editTextCSearch;

        firstTextView = binding.sugActivitiesText;
        firstTextView.setText(R.string.sugActivities);
        secondTextView = binding.textActivities;
        secondTextView.setText(R.string.Activities);
        activitiesAdapter = new ActivitiesAdapter(this);
        sugActivitiesAdapter = new MyActivitiesAdapter(this);

        //Recyclerview for activities - inspiration from Tracker demoh
        rcvActivities = binding.rcvActivities;
        rcvActivities.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcvActivities.setAdapter(activitiesAdapter);

        //Recyclerview for suggested activities
        rcvSugActivities = binding.rcvSuggestedActivities;
        rcvSugActivities.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcvSugActivities.setAdapter(sugActivitiesAdapter);

        getActivities();

        searchString.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                activitiesAdapter.updateActivitiesList(activitiesViewModel.getActivitiesForString(userId, s.toString()).getValue(), "p");
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddActivity.class);
                launcher.launch(intent);
            }
        });

        caregiver = activitiesViewModel.getCaregiver(userId);
        caregiver.observe(getActivity(), new Observer<Caregiver>() {
            @Override
            public void onChanged(Caregiver caregiver) {

            }
        });

        return root;
    }

    private void getActivities(){
        lactivities = activitiesViewModel.getActivities(userId);
        lactivities.observe(getActivity(), new Observer<ArrayList<Activity>>() {
            @Override
            public void onChanged(ArrayList<Activity> nactivities) {
                activitiesAdapter.updateActivitiesList(nactivities, "c");
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

    //Show dialogue - inspiration from Lists and grid demo
    private void showMyDialogue(Activity a){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.ic_activities)
                .setTitle(a.getActivityName())
                .setMessage(getText(R.string.description) +" " + a.getDescription())
                .setPositiveButton(R.string.addBtn, (dialogInterface, i) -> {openAddActivtiy(a);})
                .setNeutralButton(R.string.back, (dialogInterface, i) -> {})
                .setNegativeButton(R.string.deleteBtn, (dialogInterface, i) -> {areYouSureDialogue("activitySuggestions",a);});
        builder.create().show();
    }

    private void openAddActivtiy(Activity a) {
        sa = a;
        Intent intent = new Intent(getActivity(), AddActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("title", a.getActivityName());
        bundle.putSerializable("description", a.getDescription());
        intent.putExtras(bundle);
        launcher.launch(intent);
    }

    //Show dialogue - inspiration from Lists and grid demo
    private void showDialogue(Activity a){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.ic_activities)
                .setTitle(a.getActivityName())
                .setMessage(getText(R.string.description) +" " + a.getDescription() + "\n\n" +
                        getText(R.string.time) +" " + a.getTime() + "\n\n" +
                        getText(R.string.place) +" " + a.getPlace() + "\n\n" +
                        getText(R.string.participants) +" " + listOf(a.getPatients().values()) + "\n\n" +
                        getText(R.string.caregivers) +" " + listOf(a.getCaregivers().values()) )
                .setPositiveButton(R.string.ok, (dialogInterface, i) -> {})
                .setNegativeButton(R.string.cancelActivity, (dialogInterface, i) -> {areYouSureDialogue("activities", a);});
        builder.create().show();
    }

    private void areYouSureDialogue(String collectionName, Activity a){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.ic_activities)
                .setTitle(getText(R.string.deleteActivity))
                .setMessage(getText(R.string.sureDelete))
                .setPositiveButton(R.string.deleteBtn, (dialogInterface, i) -> {deleteActivity(collectionName,a);})
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> {});
        builder.create().show();
    }


    @Override
    public void addToActivity(Activity a){
        Map<String, String> caregivers = a.getCaregivers();
        caregivers.put(userId,caregiver.getValue().getName());
        a.setCaregivers(caregivers);
        a.setUserInActivity(true);
        activitiesViewModel.addUserToActivity(a);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getActivities();
            }
        }, 100);
        Toast.makeText(getActivity(), getText(R.string.caregiverAdded), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void unsubscribeActivity(Activity a){
        Map<String, String> caregivers = a.getCaregivers();
        caregivers.remove(userId);
        a.setCaregivers(caregivers);
        a.setUserInActivity(false);
        activitiesViewModel.addUserToActivity(a);
        Toast.makeText(getActivity(), getText(R.string.caregiverUnsubscribed), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getActivities();
            }
        }, 100);
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

    private void deleteActivity(String collectionName, Activity a){
        activitiesViewModel.deleteActivity(collectionName, a);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getActivities();
            }
        }, 100);
    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        Bundle b = data.getExtras();
                        if(sa!=null) {
                            deleteActivity("activitySuggestions",sa);
                        }
                    }
                }
            });

}