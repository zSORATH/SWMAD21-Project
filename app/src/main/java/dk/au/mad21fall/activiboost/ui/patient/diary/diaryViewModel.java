package dk.au.mad21fall.activiboost.ui.patient.diary;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class diaryViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public diaryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}