package dk.au.mad21fall.activiboost.models;

import java.util.ArrayList;
import java.util.Date;

public class Activity {

    private String activityName;
    private Date time;

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

}
