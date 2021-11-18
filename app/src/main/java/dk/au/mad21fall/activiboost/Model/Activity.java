package dk.au.mad21fall.activiboost.Model;

import java.util.ArrayList;
import java.util.Date;

public class Activity {

    private String activityName;
    private Date time;
    private ArrayList<Patient> participants;

    public Activity(){}

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

    public ArrayList<Patient> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<Patient> participants) {
        this.participants = participants;
    }
}
