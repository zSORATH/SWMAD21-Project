package dk.au.mad21fall.activiboost.ui.shared.login;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;


import java.util.ArrayList;

import dk.au.mad21fall.activiboost.models.Caregiver;
import dk.au.mad21fall.activiboost.models.Patient;
import dk.au.mad21fall.activiboost.repository.Repository;

public class LoginViewModel extends AndroidViewModel {

    private Repository repository;

    public LoginViewModel(@NonNull Application app) {
        super(app);
        repository = Repository.getInstance(getApplication());
    }

    public void reinstateUsers() {
        repository.loadUsers();
    }

    public boolean patientExists(String uid) {
        return repository.patientExists(uid);
    }

    public boolean caregiverExists(String uid) {
        return repository.caregiverExists(uid);
    }
}
