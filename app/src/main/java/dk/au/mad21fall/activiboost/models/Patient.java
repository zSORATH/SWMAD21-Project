package dk.au.mad21fall.activiboost.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity
public class Patient {

    @PrimaryKey(autoGenerate = true)
    private int pid;

    private String name;
    private String age;
    private String id;

    public Patient(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}