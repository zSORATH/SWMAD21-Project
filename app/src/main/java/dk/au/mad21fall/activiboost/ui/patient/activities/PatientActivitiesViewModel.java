package dk.au.mad21fall.activiboost.ui.patient.activities;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import dk.au.mad21fall.activiboost.models.Activity;
import dk.au.mad21fall.activiboost.models.Patient;
import dk.au.mad21fall.activiboost.repository.Repository;

public class PatientActivitiesViewModel extends AndroidViewModel {
        private Repository repository;
        private MutableLiveData<ArrayList<Activity>> lMyActivities;
        private MutableLiveData<ArrayList<Activity>> lActivities, slActivities;
        private MutableLiveData<Patient> patient;
        private LiveData<ArrayList<Activity>> activities;


        public PatientActivitiesViewModel(@NonNull Application app){
            super(app);
            repository = Repository.getInstance(getApplication());
            activities = repository.getActivities();


        }


        public LiveData<Patient> getUser(String userId){
            if(patient == null){
                patient = new MutableLiveData<Patient>();
            }
            patient.setValue(repository.findPatient(userId));
            return patient;
        }

        public LiveData<ArrayList<Activity>> getActivities(String userId) {
            sortList(userId);
            return lActivities;
        }

        public LiveData<ArrayList<Activity>> getMyActivities(String userId) {
            sortList(userId);
            return lMyActivities;
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

        private void sortList(String userId){
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
            // for in map: https://www.geeksforgeeks.org/iterate-map-java/
            for(Activity a : activities.getValue()){
                if(a.getPatients().containsKey(userId) && a.getTime().compareTo(today)>=0){
                    myas.add(a);
                }
                if(!a.getPatients().containsKey(userId) && a.getTime().compareTo(today)>=0){
                    as.add(a);
                }
            }
            //Sort list by date: https://stackoverflow.com/questions/5927109/sort-objects-in-arraylist-by-date?fbclid=IwAR0yDCv8PzWKIWuoiiGNrtA2OZo0byOTQaCsIUyYnspop8pAWjsN7rw2jOM
            Collections.sort(as, new Comparator<Activity>() {
                public int compare(Activity a1, Activity a2) {
                    return a1.getTime().compareTo(a2.getTime());
                }
            });
            //Sort list by date: https://stackoverflow.com/questions/5927109/sort-objects-in-arraylist-by-date?fbclid=IwAR0yDCv8PzWKIWuoiiGNrtA2OZo0byOTQaCsIUyYnspop8pAWjsN7rw2jOM
            Collections.sort(myas, new Comparator<Activity>() {
                public int compare(Activity a1, Activity a2) {
                    return a1.getTime().compareTo(a2.getTime());
                }
            });
            lMyActivities.setValue(myas);
            lActivities.setValue(as);
        }

        public void addUserToActivity(Activity a){
            repository.updateActivity("patients", a);
        }

}