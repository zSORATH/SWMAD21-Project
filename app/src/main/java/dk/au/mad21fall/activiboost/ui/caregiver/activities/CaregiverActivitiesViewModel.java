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
        private LiveData<ArrayList<Activity>> lSugActivities;
        private MutableLiveData<ArrayList<Activity>> lActivities;
        private LiveData<ArrayList<Activity>> activities, sugactivities;


        public CaregiverActivitiesViewModel(@NonNull Application app){
            super(app);
            repository = Repository.getInstance(getApplication());
            lSugActivities = repository.getSuggestedActivities();
            activities = repository.getActivities();


        }

        public LiveData<ArrayList<Activity>> getActivities(String userId) {
            sortList(activities, userId);
            return lActivities;
        }

        public LiveData<ArrayList<Activity>> getSugActivities(String userId) {
            if(lSugActivities == null){
                lSugActivities = new MutableLiveData<ArrayList<Activity>>();
            }
            return lSugActivities;
        }

        private void sortList(LiveData<ArrayList<Activity>> al, String userId){
            if(lActivities == null){
                lActivities = new MutableLiveData<ArrayList<Activity>>();
            }
            ArrayList<Activity> as = new ArrayList<>();
            //https://alvinalexander.com/java/java-today-get-todays-date-now/
            Date today = Calendar.getInstance().getTime();
            //compare dates from: https://www.guru99.com/java-date.html
            for(Activity a : activities.getValue()){
                if(a.getCaregivers().containsKey(userId)){
                    a.setUserInActivity(true);
                }
                if(a.getTime().compareTo(today)>=0){
                    as.add(a);
                }

            }
            lActivities.setValue(as);
        }

        public void addUserToActivity(String userId, Activity a){
            repository.updateCaregiverActivity(userId, a);
        }

        public void deleteSugActivity(Activity a){
            repository.deleteActivity(a);
        }

}