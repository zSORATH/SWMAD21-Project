package dk.au.mad21fall.activiboost.models.Submodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MainSM {
    @SerializedName("temp")
    @Expose
    private double temp;
    @SerializedName("feels_like")
    @Expose
    private double feels_like;
    @SerializedName("temp_min")
    @Expose
    private double temp_min;
    @SerializedName("temp_max")
    @Expose
    private double temp_max;
    @SerializedName("pressure")
    @Expose
    private int pressure;
    @SerializedName("humidity")
    @Expose
    private int humidity;
    @SerializedName("sea_level")
    @Expose
    private int sea_level;
    @SerializedName("grnd_level")
    @Expose
    private int grnd_level;


    // City name
    public void setTemp(double temp) {
        this.temp = temp;
    }
    public double getTemp() {
        return temp;
    }

}
