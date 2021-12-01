package dk.au.mad21fall.activiboost.ui.patient.diary;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

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

    public void addDiary(String content, Integer rating, String date){
        repository.addDiaryAsynch(content, rating, date);
    }

    public void updateDiary(Diary diary){
        repository.updateDiaryAsynch(diary);
    }

    public void deleteDiary(Diary diary){
        repository.deleteDiaryAsynch(diary);
    }

    public Boolean isStored(String date){
        Diary diary = null;
        ListenableFuture<Diary> future = repository.findDiaryAsynch(date);

        try {
            diary = future.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return diary!= null;
    }


}