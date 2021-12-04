package dk.au.mad21fall.activiboost.ui.shared.home;

import static dk.au.mad21fall.activiboost.Constants.CAREGIVER;
import static dk.au.mad21fall.activiboost.Constants.PATIENT;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import dk.au.mad21fall.activiboost.models.Caregiver;
import dk.au.mad21fall.activiboost.models.Patient;
import dk.au.mad21fall.activiboost.repository.Repository;

public class HomeViewModel extends AndroidViewModel {

    private MutableLiveData<String> mText;
    private Repository repository;
    private MutableLiveData<String> uid = new MutableLiveData<String>();
    private String userType;


    public HomeViewModel(@NonNull Application app) {
        super(app);
        repository = Repository.getInstance(getApplication());

        // OBS: Her er det nok bedre at bruge firebase til at get uid istedet for med intent men
        // vær opmærksom på at man ikke er logget ind med firebase hvis man bruger fast login knap
        // uid.setValue(FirebaseAuth.getInstance().getUid());
        // setUserType();


        mText = new MutableLiveData<>();
        mText.setValue("Logged in as ");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void setUid(String _uid) {
        this.uid.setValue(_uid);
        if (getPatient(uid.getValue()) != null) {
            setUserType(PATIENT);
        } else {
            setUserType(CAREGIVER);
        }
    }

    /*
    public void setUserType() {
        if (getPatient(uid.getValue()) != null) {
            setUserType(PATIENT);
        } else {
            setUserType(CAREGIVER);
        }
    }
     */

    public String getUserType() {
        return userType;
    }

    public String getUid() {
        return uid.getValue();
    }

    public Patient getPatient(String uid) {
        return repository.findPatient(uid);
    }

    public Caregiver getCaregiver(String uid) {
        return repository.findCaregiver(uid);
    }

    public void setUserType(String userType) { this.userType = userType; }
}