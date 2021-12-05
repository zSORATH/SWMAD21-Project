package dk.au.mad21fall.activiboost.ui.caregiver.activities;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import dk.au.mad21fall.activiboost.models.Activity;
import dk.au.mad21fall.activiboost.models.Caregiver;
import dk.au.mad21fall.activiboost.models.Patient;
import dk.au.mad21fall.activiboost.repository.Repository;

// Inspiration to Livedata and ViewModel Tracker demo
public class CaregiverActivitiesViewModel extends AndroidViewModel {
        private Repository repository;
        private LiveData<ArrayList<Activity>> lSugActivities;
        private MutableLiveData<ArrayList<Activity>> lActivities, slActivities;
        private MutableLiveData<Caregiver> caregiver;
        private LiveData<ArrayList<Activity>> activities;


        public CaregiverActivitiesViewModel(@NonNull Application app){
            super(app);
            repository = Repository.getInstance(getApplication());
            lSugActivities = repository.getSuggestedActivities();
            activities = repository.getActivities();
        }

        public LiveData<Caregiver> getCaregiver(String userId){
            if(caregiver == null){
                caregiver = new MutableLiveData<>();
            }
            caregiver.setValue(repository.findCaregiver(userId));
            return caregiver;
        }

        public LiveData<ArrayList<Activity>> getActivities(String userId) {
            sortList(userId);
            return lActivities;
        }

        public LiveData<ArrayList<Activity>> getSugActivities(String userId) {
            if(lSugActivities == null){
                lSugActivities = new MutableLiveData<ArrayList<Activity>>();
            }
            return lSugActivities;
        }

    public LiveData<ArrayList<Activity>> getActivitiesForString(String userId, String search){
        if(slActivities == null){
            slActivities = new MutableLiveData<ArrayList<Activity>>();
        }
        ArrayList<Activity> sas = new ArrayList<>();
        sortList(userId);
        for (Activity a : lActivities.getValue()){
            if(a.getActivityName().toLowerCase().contains(search.toLowerCase())){
                sas.add(a);
            }
        }
        slActivities.setValue(sas);
        return slActivities;
    }

        // Sort list from repository
        private void sortList( String userId){
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
            //Sort list by date: https://stackoverflow.com/questions/5927109/sort-objects-in-arraylist-by-date?fbclid=IwAR0yDCv8PzWKIWuoiiGNrtA2OZo0byOTQaCsIUyYnspop8pAWjsN7rw2jOM
            Collections.sort(as, new Comparator<Activity>() {
                public int compare(Activity a1, Activity a2) {
                    return a1.getTime().compareTo(a2.getTime());
                }
            });

            lActivities.setValue(as);
        }

        public void addUserToActivity(Activity a){
            repository.updateActivity("caregivers", a);
        }

        public void deleteActivity(String collectionName,Activity a){
            repository.deleteActivity(collectionName, a);
        }

}