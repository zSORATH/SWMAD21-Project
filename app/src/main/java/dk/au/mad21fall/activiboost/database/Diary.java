package dk.au.mad21fall.activiboost.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

// This is inspired by "Demo: Rick and Morty Gallery with Volley and Glide" from L6 in this course
@Entity
public class Diary {

    @PrimaryKey(autoGenerate = true)
    private int id;

    // data stored
    private String content;
    private Double rating;
    private String date;

    // constructor
    public Diary(String content, Double rating, String date) {
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

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
