package dk.au.mad21fall.activiboost.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

// This is inspired by "Demo: Rick and Morty Gallery with Volley and Glide" from L6 in this course
@Entity
public class Diary {

    @PrimaryKey(autoGenerate = true)
    private int id;

    // data stored
    private String content;
    private int rating;
    private String date;

    // constructor
    public Diary(String content, int rating, String date) {
        this.content = content;
        this.rating = rating;
        this.date = date;
    }

    // getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}