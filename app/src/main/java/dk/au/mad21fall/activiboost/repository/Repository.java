package dk.au.mad21fall.activiboost.repository;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dk.au.mad21fall.activiboost.models.Diary;
import dk.au.mad21fall.activiboost.database.DiaryDatabase;
import dk.au.mad21fall.activiboost.models.Activity;
import dk.au.mad21fall.activiboost.models.Patient;

// This is inspired by "Code Demo / walkthrough : using Room (and SharedPreferences)" from lecture 4
// And the "Room Demo Asynch" code provided in L5.
public class Repository {

    private static final String TAG = "Getting document:";
    private FirebaseFirestore fdb;
    private DiaryDatabase db;               //database
    private ExecutorService executor;       //for asynch processing
    private LiveData<List<Diary>> diaries;    //livedata
    private static Repository instance;     //for Singleton pattern
    private MutableLiveData<ArrayList<Patient>> patients;
    private MutableLiveData<ArrayList<Patient>> activitypatients;
    private MutableLiveData<ArrayList<Activity>> activities;
    private MutableLiveData<ArrayList<Activity>> suggestedactivities;

    //Singleton pattern to make sure there is only one instance of the Repository in use
    public static Repository getInstance(Application app){
        if(instance==null){
            instance = new Repository(app);
        }
        return instance;
    }

    //constructor - takes Application object for context
    private Repository(Application app){
        fdb = FirebaseFirestore.getInstance();
        db = DiaryDatabase.getDatabase(app.getApplicationContext());  //initialize database
        executor = Executors.newSingleThreadExecutor();                //executor for background processing
        diaries = db.diaryDAO().getAll();                             //get LiveData reference to all entries
        loadData("patients", "p");
        loadData("activities", "a");
        loadData("activitySuggestions", "s");
        loadActivityPaticipants("p");
    }

    public MutableLiveData<ArrayList<Patient>> getPatients() {
        return patients;
    }

    public LiveData<List<Diary>> getDiaries() {
        return diaries;
    }

    //update Diary in database
    public void updateDiaryAsynch(Diary diary){

        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.diaryDAO().updateDiary(diary);
            }
        });
    }

    //add a new Diary to database
    public void addDiaryAsynch(String content, Integer rating, String date){

        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.diaryDAO().addDiary(new Diary(content, rating, date));
            }
        });
    }

    //delete Diary from database
    public void deleteDiaryAsynch(Diary diary){

        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.diaryDAO().deleteDiary(diary);
            }
        });
    }

    //find diary with specific date
    public ListenableFuture<Diary> findDiaryAsynch(String date) {
        return db.diaryDAO().findDiary(date);
    }

    public LiveData<ArrayList<Activity>> getActivities() {
        return activities;
    }


    //Firebase requests : https://firebase.google.com/docs/firestore/query-data/listen
    private void loadData(String collectionName, String type) {
        fdb.collection(collectionName)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot valueg, @Nullable FirebaseFirestoreException error) {
                        if(valueg != null && !valueg.isEmpty()){
                            if(type.equals("p")) {
                                ArrayList<Patient> updatedPatients = new ArrayList<>();
                                for (DocumentSnapshot docg : valueg.getDocuments()) {
                                    Log.d(TAG, "DocumentSnapshot data: " + docg.getData());
                                    Patient p = docg.toObject(Patient.class);
                                    if (p != null) {
                                        updatedPatients.add(p);
                                    }
                                }
                                if (patients == null) {
                                    patients = new MutableLiveData<ArrayList<Patient>>();
                                }
                                patients.setValue(updatedPatients);
                            }
                            if(type.equals("a")){
                                ArrayList<Activity> updatedActivities = new ArrayList<>();
                                for(DocumentSnapshot docg : valueg.getDocuments()) {
                                    Log.d(TAG, "DocumentSnapshot data: " + docg.getData());
                                    Activity a = docg.toObject(Activity.class);
                                    if (a != null) {
                                        a.setId(docg.getId());
                                        updatedActivities.add(a);
                                    }
                                }
                               if (activities == null) {
                                    activities = new MutableLiveData<ArrayList<Activity>>();
                                }
                                activities.setValue(updatedActivities);
                            }
                            if(type.equals("s")){
                                ArrayList<Activity> sActivities = new ArrayList<>();
                                for(DocumentSnapshot docg : valueg.getDocuments()) {
                                    Log.d(TAG, "DocumentSnapshot data: " + docg.getData());
                                    Activity a = docg.toObject(Activity.class);
                                    if (a != null) {
                                        sActivities.add(a);
                                    }
                                }
                                if (suggestedactivities == null) {
                                    suggestedactivities = new MutableLiveData<ArrayList<Activity>>();
                                }
                                suggestedactivities.setValue(sActivities);
                            }
                        }
                    }
                });
    }

    // method from: https://firebase.google.com/docs/firestore/query-data/listen
    private void loadActivityPaticipants(String type){
        fdb.collection("activities").document("000001").collection("activitypatients")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot valueg, @Nullable FirebaseFirestoreException error) {
                        if(valueg != null && !valueg.isEmpty()){
                            if(type.equals("p")) {
                                ArrayList<Patient> updatedPatients = new ArrayList<>();
                                for (DocumentSnapshot docg : valueg.getDocuments()) {
                                    Log.d(TAG, "DocumentSnapshot data: " + docg.getData());
                                    Patient p = docg.toObject(Patient.class);
                                    if (p != null) {
                                        updatedPatients.add(p);
                                    }
                                }
                                if (activitypatients == null) {
                                    activitypatients = new MutableLiveData<ArrayList<Patient>>();
                                }
                                activitypatients.setValue(updatedPatients);
                            }
                        }
            }
        });
    }

    // Method from firebase: https://firebase.google.com/docs/firestore/manage-data/add-data#java_20
    public void updateActivity(String userid, Activity a){
        DocumentReference docRef = fdb.collection("activities").document(a.getId());
        docRef
                .update("patients", a.getPatients())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });


    }
    // Method from firebase: https://firebase.google.com/docs/firestore/manage-data/add-data#java_20
    public void suggestActivity(String collectionName, Activity a){
        Map<String, Object> activity = new HashMap<>();
        activity.put("activityName", a.getActivityName());
        activity.put("time", a.getTime());
        activity.put("description", a.getDescription());
        activity.put("patients", a.getPatients());
        activity.put("caregivers", a.getCaregivers());

        fdb.collection(collectionName).document(a.getActivityName())
                .set(activity)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }
}