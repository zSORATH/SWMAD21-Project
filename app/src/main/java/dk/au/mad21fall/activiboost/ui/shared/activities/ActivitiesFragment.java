package dk.au.mad21fall.activiboost.ui.shared.activities;

import static dk.au.mad21fall.activiboost.ui.shared.login.User.CAREGIVER;
import static dk.au.mad21fall.activiboost.ui.shared.login.User.PATIENT;

import android.content.Intent;
import  android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import dk.au.mad21fall.activiboost.MainActivity;
import dk.au.mad21fall.activiboost.R;
import dk.au.mad21fall.activiboost.models.Activity;
import dk.au.mad21fall.activiboost.ui.shared.activities.suggest.SuggestActivity;
import dk.au.mad21fall.activiboost.ui.shared.activities.add.AddActivity;
import dk.au.mad21fall.activiboost.databinding.FragmentActivitiesBinding;

public class ActivitiesFragment extends Fragment implements ActivitiesAdapter.IActivitiesItemClickedListener{

    private static final String TAG = "ACTIVITIES FRAGMENT";

    private ActivitiesViewModel activitiesViewModel;
    private FragmentActivitiesBinding binding;
    private int userType;
    private Intent intent;
    private TextView firstTextView, secondTextView;
    private ActivitiesAdapter activitiesAdapter;
    private RecyclerView rcvMyActivities, rcvActivities;
    private ArrayList<Activity> activities;
    private LiveData<ArrayList<Activity>> lactivities;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        activitiesViewModel = new ViewModelProvider(this).get(ActivitiesViewModel.class);

        binding = FragmentActivitiesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        firstTextView = binding.activityTextView1;
        secondTextView = binding.activityTextView2;
        activitiesAdapter = new ActivitiesAdapter(this);
        rcvActivities = binding.activityRCV2;
        rcvActivities.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcvActivities.setAdapter(activitiesAdapter);

        rcvMyActivities = binding.activityRCV1;

        lactivities = activitiesViewModel.getActivities();
        lactivities.observe(getActivity(), new Observer<ArrayList<Activity>>() {
            @Override
            public void onChanged(ArrayList<Activity> nactivities) {
                activitiesAdapter.updateActivitiesList(nactivities);
            }
        });

        // this is how we determine what type of user is corrently logged in
        // this will probably change when we implent user classes better
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
                    Log.d(TAG, "Error in selecting user type");
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

    @Override
    public void onActivityClicked(int index) {
        ArrayList<Activity> al = activities;
        showDialogue(al.get(index));
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