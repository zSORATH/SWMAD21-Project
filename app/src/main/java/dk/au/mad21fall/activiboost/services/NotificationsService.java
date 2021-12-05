package dk.au.mad21fall.activiboost.services;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.Observer;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dk.au.mad21fall.activiboost.R;
import dk.au.mad21fall.activiboost.models.Activity;
import dk.au.mad21fall.activiboost.repository.Repository;

// Copied and modifyed from Mie's assignment 2, which were
// Heavily inspired by Lesson 5 Demo code
public class NotificationsService extends LifecycleService {

    public static final int NOTIFICATION_ID = 22;
    private static final String TAG = "NotificationService";

    Repository repo;
    List<Activity> activities;
    Random randomActivity = new Random();
    Application application;

    ExecutorService executorService;
    boolean started = false;

    public NotificationsService(){}

    @Override
    public void onCreate(){
        super.onCreate();
    }

    // Initial startup;
    @Override
    public int onStartCommand(Intent intent, int flag, int startUid){
        super.onStartCommand(intent, flag, startUid);

        Application app = getApplication();
        repo = Repository.getInstance(app);
        repo.getActivities().observe(this, (Observer<List<Activity>>) _activities -> activities = _activities);

        Log.e(TAG, "Checking build versions");
        NotificationChannel channel = new NotificationChannel("ServiceID", "Notification Service", NotificationManager.IMPORTANCE_LOW);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);

        Notification notification = new NotificationCompat.Builder(this, "ServiceID")
                .setContentTitle("ActiviBoost")
                .setContentText(getResources().getString(R.string.suggestions))
                .setSmallIcon(R.drawable.custom_icon_foreground)

                .build();

        startForeground(NOTIFICATION_ID, notification);

        runInBackground();


        return START_STICKY;
    }

    //After the service has been started, give movie suggestions
    private void runInBackground() {
        if (! started)
        {
            started = true;
        }
        giveSuggestions();
    }


    private void giveSuggestions() {
        if(executorService == null){
            executorService = Executors.newSingleThreadExecutor();
        }

        executorService.submit(() -> {
            try {

                Thread.sleep(600); //Show every minute

                Activity activity = activities.get(randomActivity.nextInt(activities.size()));
                Log.e(TAG,"sending suggestion");
                Notification notification = new NotificationCompat.Builder(application.getApplicationContext(), "ServiceID")
                        .setContentTitle(getResources().getString(R.string.trythis))
                        .setContentText(activity.getActivityName())
                        .setSmallIcon(R.drawable.custom_icon_foreground)
                        .build();
            }
            catch (InterruptedException e){
                Log.e(TAG, "Exception.", e);
            }
            if(started){
                giveSuggestions();
            }
        });
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        return null;
    }

    @Override
    public void onDestroy() {
        started = false;
        super.onDestroy();
    }

}
