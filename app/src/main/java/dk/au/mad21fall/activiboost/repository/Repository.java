package dk.au.mad21fall.activiboost.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dk.au.mad21fall.activiboost.database.Diary;
import dk.au.mad21fall.activiboost.database.DiaryDatabase;

public class Repository {

    private DiaryDatabase db;               //database
    private ExecutorService executor;       //for asynch processing
    private LiveData<List<Diary>> diary;    //livedata
    private static Repository instance;     //for Singleton pattern

    //Singleton pattern to make sure there is only one instance of the Repository in use
    public static Repository getInstance(Application app){
        if(instance==null){
            instance = new Repository(app);
        }
        return instance;
    }

    //constructor - takes Application object for context
    private Repository(Application app){
        db = DiaryDatabase.getDatabase(app.getApplicationContext());  //initialize database
        executor = Executors.newSingleThreadExecutor();                //executor for background processing
        diary = db.diaryDAO().getAll();                             //get LiveData reference to all entries
    }

    //update Diary in database
    public void updateDiaryAsynch(Diary diary){

        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.diaryDAO().updateDiary(diary);
            }
        });
    }

    //add a new Diary to database
    public void addDiaryAsynch(String content, Double rating, String date){

        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.diaryDAO().addDiary(new Diary(content, rating, date));
            }
        });
    }

    //delete Diary
    public void deleteDiaryAsynch(Diary diary){

        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.diaryDAO().deleteDiary(diary);
            }
        });
    }



}
