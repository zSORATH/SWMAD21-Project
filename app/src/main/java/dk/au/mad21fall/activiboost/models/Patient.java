package dk.au.mad21fall.activiboost.models;

// This is inspired by "Demo: Rick and Morty Gallery with Volley and Glide" from L6 in this course
public class Patient {

    private String name;
    private int age;
    private String id;

    public Patient(){}

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}