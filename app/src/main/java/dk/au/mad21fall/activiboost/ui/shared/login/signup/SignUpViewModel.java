package dk.au.mad21fall.activiboost.ui.shared.login.signup;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import dk.au.mad21fall.activiboost.repository.Repository;

public class SignUpViewModel extends AndroidViewModel {

    private static final String TAG = "SIGN UP VM";

    // private Repository repository;
    private MutableLiveData<String> email;
    private MutableLiveData<String> password;
    private String emailPrev, passwordPrev;
    private FirebaseAuth fba;
    private FirebaseUser user;

    public SignUpViewModel(@NonNull Application app) {
        super(app);
        // repository = Repository.getInstance(getApplication());
        fba = FirebaseAuth.getInstance();
        user = fba.getCurrentUser();
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

    public void deleteUser() {
        user = fba.getCurrentUser();
        if (user != null) {
            Log.d(TAG, "User deleted");
            user.delete();
        }
    }
}
