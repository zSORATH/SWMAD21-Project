package dk.au.mad21fall.activiboost.models;

import com.google.firebase.firestore.PropertyName;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Activity {

    private String activityName;
    private Date time;
    // map guide from: javatpoint.com/java-map
    private Map<String, Boolean> patients;
    private Map<String, Boolean> caregivers;
    private String description;


    public Activity(){}

    public Activity(String activityName, String description){
        this.activityName = activityName;
        this.description = description;
    }

    public Activity(String activityName, String description, Date time){
        this.time = time;
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

    public Map<String, Boolean> getPatients() {
        return patients;
    }

    public void setPatients(Map<String, Boolean> patients) {
        this.patients = patients;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Boolean> getCaregivers() {
        return caregivers;
    }

    public void setCaregivers(Map<String, Boolean> caregivers) {
        this.caregivers = caregivers;
    }
}
