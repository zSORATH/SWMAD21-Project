package dk.au.mad21fall.activiboost.ui.caregiver.activities.add;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import dk.au.mad21fall.activiboost.models.Activity;
import dk.au.mad21fall.activiboost.repository.Repository;

public class AddActivityViewModel extends AndroidViewModel {
    private Repository repository;
    private LiveData<ArrayList<Activity>> lSugActivities;
    private MutableLiveData<ArrayList<Activity>> lActivities;
    private LiveData<ArrayList<Activity>> activities, sugactivities;


    public AddActivityViewModel(@NonNull Application app){
        super(app);
        repository = Repository.getInstance(getApplication());


    }

    public void saveActivity(Activity a){
        repository.suggestActivity("activities", a);
    }
}
