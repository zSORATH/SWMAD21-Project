package dk.au.mad21fall.activiboost.ui.patient.diary;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.concurrent.ExecutionException;

import dk.au.mad21fall.activiboost.models.Diary;
import dk.au.mad21fall.activiboost.repository.Repository;

public class DiaryViewModel extends AndroidViewModel {

    private Repository repository;

    private LiveData<List<Diary>> diaries;


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