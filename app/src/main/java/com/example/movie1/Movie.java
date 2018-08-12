package com.example.movie1;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Νικος Νακκας on 23/2/2018.
 */

public class Movie implements Serializable{
    @SerializedName("title")
    private String title;
    @SerializedName("poster_path")
    private String image;
    @SerializedName("overview")
    private String plotSynopsis;

    public Movie(){

    }
    public Movie(String title,String image,String plotSynopsis,float userRating,String releaseDate){
        this.title=title;
        this.image=image;
        this.plotSynopsis=plotSynopsis;
        this.userRating=userRating;
        this.releaseDate=releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public float getUserRating() {
        return userRating;
    }

    public void setUserRating(float userRating) {
        this.userRating = userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
    @SerializedName("vote_average")
    private float userRating;
    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("id")
    private int id;

    public int getId() {
        return id;
    }
    private int favourited=0;

    public int getFavourited() {
        return favourited;
    }

    public void setFavourited(int favourited) {
        this.favourited = favourited;
    }
}

