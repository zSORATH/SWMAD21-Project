package dk.au.mad21fall.activiboost.ui.shared.login;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import dk.au.mad21fall.activiboost.repository.Repository;

public class LoginViewModel extends AndroidViewModel {

    private Repository repository;

    public LoginViewModel(@NonNull Application app) {
        super(app);
        repository = Repository.getInstance(app);
    }

}
