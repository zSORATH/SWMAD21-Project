package dk.au.mad21fall.activiboost.ui.caregiver.activities;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import dk.au.mad21fall.activiboost.models.Activity;
import dk.au.mad21fall.activiboost.repository.Repository;

public class CaregiverActivitiesViewModel extends AndroidViewModel {
        private Repository repository;
        private MutableLiveData<ArrayList<Activity>> lMyActivities;
        private MutableLiveData<ArrayList<Activity>> lActivities;
        private LiveData<ArrayList<Activity>> activities;


        public CaregiverActivitiesViewModel(@NonNull Application app){
            super(app);
            repository = Repository.getInstance(getApplication());
            activities = repository.getActivities();

        }

        public LiveData<ArrayList<Activity>> getActivities(String userId) {
            sortList(activities, userId);
            return lActivities;
        }

        public LiveData<ArrayList<Activity>> getMyActivities(String userId) {
            sortList(activities, userId);
            return lMyActivities;
        }

        private void sortList(LiveData<ArrayList<Activity>> al, String userId){
            if(lMyActivities == null){
                lMyActivities = new MutableLiveData<ArrayList<Activity>>();
            }
            if(lActivities == null){
                lActivities = new MutableLiveData<ArrayList<Activity>>();
            }
            ArrayList<Activity> myas = new ArrayList<>();
            ArrayList<Activity> as = new ArrayList<>();
            //
            Date today = Calendar.getInstance().getTime();
            //compare dates from: https://www.guru99.com/java-date.html
            for(Activity a : activities.getValue()){
                if(a.getPatients().containsKey(userId) && a.getTime().compareTo(today)>=0){
                    myas.add(a);
                }
                if(!a.getPatients().containsKey(userId) && a.getTime().compareTo(today)>=0){
                    as.add(a);
                }
            }
            lMyActivities.setValue(myas);
            lActivities.setValue(as);
        }

        public void addUserToActivity(String userId, Activity a){
            repository.updateActivity(userId, a);
        }

}