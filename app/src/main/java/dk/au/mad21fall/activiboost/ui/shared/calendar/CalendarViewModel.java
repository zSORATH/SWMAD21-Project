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

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dk.au.mad21fall.activiboost.models.Activity;
import dk.au.mad21fall.activiboost.models.Caregiver;
import dk.au.mad21fall.activiboost.models.Diary;
import dk.au.mad21fall.activiboost.models.Patient;
import dk.au.mad21fall.activiboost.repository.Repository;

// Using https://github.com/SpongeBobSun/mCalendarView

public class CalendarViewModel extends AndroidViewModel {

    private static final String TAG = "CALENDAR FRAGMENT VM";

    private Repository repository;
    private LiveData<ArrayList<Activity>> activities;
    private LiveData<List<Diary>> diaries;
    private MutableLiveData<ArrayList<Activity>> lActivities;
    private Calendar cal = Calendar.getInstance();

    public CalendarViewModel(@NonNull Application app) {
        super(app);
        repository = Repository.getInstance(getApplication());
        activities = repository.getActivities();
        diaries = repository.getDiaries();
    }

    public MutableLiveData<ArrayList<Activity>> getActivities(String uid) {
        if (lActivities == null) {
            lActivities = new MutableLiveData<ArrayList<Activity>>();
        }
        ArrayList<Activity> as = new ArrayList<>();

        if (getUserType(uid) == PATIENT) {
            for (Activity a : activities.getValue()){
                if (a.getPatients().containsKey(uid)){
                    Log.d(TAG, "Activity added: " + a.getActivityName());
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

    public String getUserType(String uid) {
        if (repository.patientExists(uid)) {
            Log.d(TAG, "User type: PATIENT");
            return PATIENT;
        } else if (repository.caregiverExists(uid)) {
            Log.d(TAG, "User type: CAREGIVER");
            return CAREGIVER;
        } else {
            Log.d(TAG, "Couldn't find matching uid in database.");
            return null;
        }
    }

    public ArrayList<Activity> getActivitiesOnDate(Date date) {
        if (lActivities == null) {
            lActivities = new MutableLiveData<ArrayList<Activity>>();
        }
        ArrayList<Activity> as = new ArrayList<>();

        for (Activity a : lActivities.getValue()) {
            if (getDateDiff(a.getTime(), date) == 0) {
                as.add(a);
            }
        }
        return as;
    }

    public boolean dateHasActivity(Date date) {
        if (lActivities == null) {
            return false;
        }

        for (Activity a : lActivities.getValue()) {
            Log.d(TAG, "Date difference: " + getDateDiff(date, a.getTime()));
            if (getDateDiff(date, a.getTime()) == 0) {
                Log.d(TAG, "Activity found: " + a.getActivityName());
                return true;
            }
        }
        return false;
    }

    public long getDateDiff(Date date1, Date date2) {
        LocalDate lDate1 = convertToLocalDateViaInstant(date1);
        LocalDate lDate2 = convertToLocalDateViaInstant(date2);
        return ChronoUnit.DAYS.between(lDate1, lDate2);

    }

    //From: https://www.baeldung.com/java-date-to-localdate-and-localdatetime
    public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}