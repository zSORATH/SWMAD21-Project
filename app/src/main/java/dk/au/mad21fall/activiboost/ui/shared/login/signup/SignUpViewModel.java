package dk.au.mad21fall.activiboost.ui.shared.login.signup;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import dk.au.mad21fall.activiboost.repository.Repository;

public class SignUpViewModel extends AndroidViewModel {

    // private Repository repository;
    private MutableLiveData<String> email;
    private MutableLiveData<String> password;
    private String emailPrev, passwordPrev;

    public SignUpViewModel(@NonNull Application app) {
        super(app);
        // repository = Repository.getInstance(getApplication());
    }

    // Adapted from https://stackoverflow.com/questions/52516240/how-to-make-edittext-observe-a-viewmodels-livedata-and-forward-user-input-to-th
    public LiveData<String> getEmail(String input) {
        if (email == null || !emailPrev.equals(input)) {
            emailPrev = input;
            email = new MutableLiveData<>();
            email.setValue(input);
        }
        return email;
    }

    public LiveData<String> getPassword(String input) {
        if (password == null || !passwordPrev.equals(input)) {
            passwordPrev = input;
            password = new MutableLiveData<>();
            password.setValue(input);
        }
        return password;
    }
}
