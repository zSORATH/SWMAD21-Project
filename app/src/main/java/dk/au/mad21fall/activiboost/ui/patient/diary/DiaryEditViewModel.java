package dk.au.mad21fall.activiboost.ui.patient.diary;

import android.app.Application;

import androidx.lifecycle.ViewModel;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

import dk.au.mad21fall.activiboost.models.Diary;
import dk.au.mad21fall.activiboost.repository.Repository;

public class DiaryEditViewModel extends ViewModel {

    private Repository repository;
    ListenableFuture<Diary> future;

    Diary diary;

    public void CreateEditRepository(Application app, String date){
        repository = Repository.getInstance(app);  //get Repository singleton
        future = repository.findDiaryAsynch(date);

        try {
            diary = future.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Diary getDiary(String date){
        return diary;
    }

    public void setDiary(Diary diary){
        repository.updateDiaryAsynch(diary);
    }

    public void updateDiary(Diary diary){
        repository.updateDiaryAsynch(diary);
    }

    public void deleteDiary(Diary diary){
        repository.deleteDiaryAsynch(diary);
    }

}
