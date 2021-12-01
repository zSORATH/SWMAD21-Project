package dk.au.mad21fall.activiboost.ui.patient.activities.suggest;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import dk.au.mad21fall.activiboost.models.Activity;
import dk.au.mad21fall.activiboost.repository.Repository;

public class SuggestViewModel extends AndroidViewModel {
    private Repository repository;


    public SuggestViewModel(@NonNull Application app){
        super(app);
        repository = Repository.getInstance(getApplication());

    }

    public void suggestActivity(String activityTitle, String activityDescription){
        Activity a = new Activity(activityTitle, activityDescription);
        repository.suggestActivity("activitySuggestions", a);
    }
}