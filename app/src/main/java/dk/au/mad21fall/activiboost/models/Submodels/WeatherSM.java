package dk.au.mad21fall.activiboost.models.Submodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeatherSM {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("main")
    @Expose
    private String main;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("icon")
    @Expose
    private String icon;

}
