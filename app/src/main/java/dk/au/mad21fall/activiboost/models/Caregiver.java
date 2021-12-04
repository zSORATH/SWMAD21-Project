package dk.au.mad21fall.activiboost.models;

import androidx.room.PrimaryKey;

public class Caregiver {

    private int cid;

    private String name;
    private int age;
    private String id;

    public Caregiver(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getId() { return id; }

    public void setId(String id) {
        this.id = id;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

}