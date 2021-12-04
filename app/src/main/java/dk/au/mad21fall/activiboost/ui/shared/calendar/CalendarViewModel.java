package dk.au.mad21fall.activiboost.ui.shared.calendar;

import static dk.au.mad21fall.activiboost.Constants.CAREGIVER;
import static dk.au.mad21fall.activiboost.Constants.PATIENT;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import dk.au.mad21fall.activiboost.models.Activity;
import dk.au.mad21fall.activiboost.models.Caregiver;
import dk.au.mad21fall.activiboost.models.Patient;
import dk.au.mad21fall.activiboost.repository.Repository;

// Using https://github.com/SpongeBobSun/mCalendarView

public class CalendarViewModel extends AndroidViewModel {

    private static final String TAG = "CALENDAR FRAGMENT VM";

    private Repository repository;
    private LiveData<ArrayList<Activity>> activities;
    private MutableLiveData<ArrayList<Activity>> lActivities;
    private MutableLiveData<Patient> patient;
    private MutableLiveData<Caregiver> caregiver;

    public CalendarViewModel(@NonNull Application app) {
        super(app);
        repository = Repository.getInstance(getApplication());
        activities = repository.getActivities();
    }

    public LiveData<Patient> getPatient(String uid){
        if(patient == null){
            patient = new MutableLiveData<Patient>();
        }
        patient.setValue(repository.findPatient(uid));
        return patient;
    }

    public LiveData<Caregiver> getCaregiver(String uid){
        if(caregiver == null){
            caregiver = new MutableLiveData<Caregiver>();
        }
        caregiver.setValue(repository.findCaregiver(uid));
        return caregiver;
    }

    public MutableLiveData<ArrayList<Activity>> getActivities(String uid) {
        if(lActivities == null){
            lActivities = new MutableLiveData<ArrayList<Activity>>();
        }
        ArrayList<Activity> as = new ArrayList<>();

        if (getUserType(uid) == PATIENT) {
            for (Activity a : activities.getValue()){
                if (a.getPatients().containsKey(uid)){
                    as.add(a);
                }
            }
        } else {
            for (Activity a : activities.getValue()){
                as.add(a);
            }
        }
        lActivities.setValue(as);
        return lActivities;
    }

    //public Activity getActivity(String date) {

    //}

    public String getUserType(String uid) {
        if (repository.patientExists(uid)) {
            Log.d(TAG, "User type: PATIENT");
            return PATIENT;
        } else {
            Log.d(TAG, "User type: CAREGIVER");
            return CAREGIVER;
        }
    }
}