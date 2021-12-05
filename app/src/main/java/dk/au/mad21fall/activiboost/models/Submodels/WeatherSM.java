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

    // Weather type
    public void setMain(String main) {
        this.main = main;
    }
    public String getMain() {
        return main;
    }
    // Img icon
    public void setIcon(String icon) {
        this.icon = icon;
    }
    public String getIcon() {
        return icon;
    }
}
