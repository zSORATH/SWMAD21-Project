package dk.au.mad21fall.activiboost.ui.shared.activities;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import dk.au.mad21fall.activiboost.models.Activity;
import dk.au.mad21fall.activiboost.repository.Repository;

public class ActivitiesViewModel extends AndroidViewModel {
        private Repository repository;
        MutableLiveData<ArrayList<Activity>> lActivities;
        LiveData<ArrayList<Activity>> activities;


        public ActivitiesViewModel(@NonNull Application app){
            super(app);
            repository = Repository.getInstance(getApplication());
            activities = repository.getActivities();

        }

        public LiveData<ArrayList<Activity>> getActivities() {
            if(activities == null){
                activities = new MutableLiveData<ArrayList<Activity>>();
            }
            return activities;
        }

}