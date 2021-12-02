package dk.au.mad21fall.activiboost.ui.shared.login.signupdetails;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;

import dk.au.mad21fall.activiboost.repository.Repository;

public class SignUpDetailsViewModel extends AndroidViewModel {

    private Repository repository;
    private FirebaseAuth fba;
    private FirebaseUser user;
    private MutableLiveData<String> name;
    private MutableLiveData<String> age;
    private String namePrev, agePrev;

    public SignUpDetailsViewModel(@NonNull Application app) {
        super(app);
        repository = Repository.getInstance(getApplication());
        fba = FirebaseAuth.getInstance();
        user = fba.getCurrentUser();
    }

    // Adapted from https://stackoverflow.com/questions/52516240/how-to-make-edittext-observe-a-viewmodels-livedata-and-forward-user-input-to-th
    public LiveData<String> getName(String input) {
        if (name == null || !namePrev.equals(input)) {
            namePrev = input;
            name = new MutableLiveData<>();
            name.setValue(input);
        }
        return name;
    }

    public LiveData<String> getAge(String input) {
        if (age == null || !agePrev.equals(input)) {
            agePrev = input;
            age = new MutableLiveData<>();
            age.setValue(input);
        }
        return age;
    }

    public void deleteUser() {
        user.delete();
    }

    public void createPatient(String name, String age) {
        Map<String, Object> patient = new HashMap<>();
        int ageInt = Integer.parseInt(age);

        patient.put("age", ageInt);
        patient.put("id", user.getUid());
        patient.put("name", name);

        repository.addPatient(patient);
    }

    public void createCaregiver(String name, String age) {
        Map<String, Object> caregiver = new HashMap<>();
        int ageInt = Integer.parseInt(age);

        caregiver.put("age", ageInt);
        caregiver.put("id", user.getUid());
        caregiver.put("name", name);

        repository.addCaregiver(caregiver);
    }
}
