package dk.au.mad21fall.activiboost.database;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.List;

import dk.au.mad21fall.activiboost.models.Diary;

// This is inspired by the code in L3 "Data storage demo" in this course.
@Dao
public interface DiaryDAO {

    //TODO: Hvad mangler der ellers?

    @Query("SELECT * FROM diary")
    LiveData<List<Diary>> getAll();

    @Query("SELECT * FROM diary WHERE date LIKE :date LIMIT 1")
    ListenableFuture<Diary> findDiary(String date);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addDiary(Diary diary);

    @Update
    void updateDiary(Diary diary);

    @Delete
    void deleteDiary(Diary diary);
}
