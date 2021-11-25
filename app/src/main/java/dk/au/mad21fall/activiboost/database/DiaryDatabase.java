package dk.au.mad21fall.activiboost.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import dk.au.mad21fall.activiboost.models.Diary;

// This is from the "Code Demo / walkthrough : using Room (and SharedPreferences)" from lecture 4
@Database(entities = {Diary.class}, version = 4)
public abstract class DiaryDatabase extends RoomDatabase{

    public abstract DiaryDAO diaryDAO();  //mandatory DAO getter
    private static DiaryDatabase instance; //database instance for singleton

    public static DiaryDatabase getDatabase(final Context context) {
        if (instance == null) {
            synchronized (DiaryDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            DiaryDatabase.class, "diary_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }
}