package dk.au.mad21fall.activiboost.ui.caregiver.patients;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PatientsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PatientsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is patients fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}