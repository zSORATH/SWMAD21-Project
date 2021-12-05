package dk.au.mad21fall.activiboost.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import dk.au.mad21fall.activiboost.models.Submodels.Clouds;
import dk.au.mad21fall.activiboost.models.Submodels.Coord;
import dk.au.mad21fall.activiboost.models.Submodels.MainSM;
import dk.au.mad21fall.activiboost.models.Submodels.Sys;
import dk.au.mad21fall.activiboost.models.Submodels.WeatherSM;
import dk.au.mad21fall.activiboost.models.Submodels.Wind;

// Inspired by RickandMortyGallery_1.9 class demo
public class WeatherModel {

    @SerializedName("coord")
    @Expose
    private Coord coord;

    @SerializedName("weather")
    @Expose
    private List<WeatherSM> weatherSM;
    @SerializedName("base")
    @Expose
    private String base;

    @SerializedName("main")
    @Expose
    private MainSM mainSM;
    @SerializedName("visibility")
    @Expose
    private int visibility;


    @SerializedName("wind")
    @Expose
    private Wind wind;

    @SerializedName("clouds")
    @Expose
    private Clouds clouds;

    @SerializedName("dt")
    @Expose
    private int dt;

    @SerializedName("sys")
    @Expose
    private Sys sys;

    @SerializedName("timezone")
    @Expose
    private int timezone;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("cod")
    @Expose
    private int cod;

    // City name
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

}
