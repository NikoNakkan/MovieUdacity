package com.example.movie1;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Νικος Νακκας on 21/6/2018.
 */

public class Review {
    public Review(){

    }
    @SerializedName("author")
    private String author;
    @SerializedName("content")
    private String content;



    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
