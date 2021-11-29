package dk.au.mad21fall.activiboost.ui.patient.diary;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import dk.au.mad21fall.activiboost.models.Diary;
import dk.au.mad21fall.activiboost.models.Patient;
import dk.au.mad21fall.activiboost.repository.Repository;

public class DiaryViewModel extends ViewModel {

    private Repository repository;

    private LiveData<List<Diary>> diaries;

    public DiaryViewModel() {
    }

    public void CreateRepository(Application app){
        repository = Repository.getInstance(app);  //get Repository singleton
        diaries = repository.getDiaries();
    }

    public LiveData<List<Diary>> GetDiaryLiveData(){
        return diaries;
    }
}