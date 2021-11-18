package dk.au.mad21fall.activiboost.ui.patient.diary;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import dk.au.mad21fall.activiboost.database.Diary;
import dk.au.mad21fall.activiboost.repository.Repository;

public class DiaryViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DiaryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }



    private Repository repository;

    public void DeleteDiary(Diary diary){
        repository.deleteDiaryAsynch(diary);
    }
}