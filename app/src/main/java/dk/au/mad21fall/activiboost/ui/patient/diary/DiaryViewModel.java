package dk.au.mad21fall.activiboost.ui.patient.diary;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import dk.au.mad21fall.activiboost.models.Diary;
import dk.au.mad21fall.activiboost.repository.Repository;

public class DiaryViewModel extends AndroidViewModel {

    private Repository repository;

    private LiveData<List<Diary>> diaries;

    ListenableFuture<Diary> future;

    //TODO: FJERN?
    Diary diary;

    public DiaryViewModel(@NonNull Application app) {
        super(app);
        repository = Repository.getInstance(getApplication());
        diaries = repository.getDiaries();
    }

    public LiveData<List<Diary>> GetDiaryLiveData(){
        return diaries;
    }

    public Diary getDiary(int index){
        return diaries.getValue().get(index);
    }

    public void addDiary(String content, int rating, String date){
        repository.addDiaryAsynch(content, rating, date);
    }

    public void updateDiary(Diary diary){
        //repository.updateDiaryAsynch(diary);
        future = repository.getDiaryAsynch(diary.getDate());

        try {
            diary = future.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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