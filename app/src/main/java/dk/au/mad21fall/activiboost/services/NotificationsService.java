package dk.au.mad21fall.activiboost.services;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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

    public NotificationsService(){};

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
        repo.getActivities().observe(this, new Observer<List<Activity>>() {
            @Override
            public void onChanged(List<Activity> _activities) {
                activities = _activities;
            }
        });

        if(Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("ServiceID", "Notification Service", NotificationManager.IMPORTANCE_LOW);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }

        Notification notification = new NotificationCompat.Builder(this, "ServiceID")
                .setContentTitle("ActiviBoost")
                .setContentText("I'll give you suggestions to activities")
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

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG,"Sending a activity suggestion in notification.");

                    Thread.sleep(60000); //Show every minute

                    Activity activity = activities.get(randomActivity.nextInt(activities.size()));
                    Notification notification = new NotificationCompat.Builder(application.getApplicationContext(), "ServiceID")
                            .setContentTitle(activity.getActivityName())
                            .setContentText(activity.getDescription())
                            .setSmallIcon(R.drawable.custom_icon_foreground)
                            .build();
                }
                catch (InterruptedException e){
                    Log.e(TAG, "Exception.", e);
                }
                if(started){
                    giveSuggestions();
                }
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
