package dk.au.mad21fall.activiboost.models;

import com.google.firebase.firestore.PropertyName;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Activity {

    private String id;
    private String activityName;
    private Date time;
    // map guide from: javatpoint.com/java-map
    private Map<String, String> patients;
    private Map<String, String> caregivers;
    private String description;
    private Boolean userInActivity = false;
    private String place;

    public Activity(){}

    public Activity(String activityName, String description){
        this.activityName = activityName;
        this.description = description;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Map<String, String> getPatients() {
        return patients;
    }

    public void setPatients(Map<String, String> patients) {
        this.patients = patients;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, String> getCaregivers() {
        return caregivers;
    }

    public void setCaregivers(Map<String, String> caregivers) {
        this.caregivers = caregivers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getUserInActivity() {
        return userInActivity;
    }

    public void setUserInActivity(Boolean userInActivity) {
        this.userInActivity = userInActivity;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
