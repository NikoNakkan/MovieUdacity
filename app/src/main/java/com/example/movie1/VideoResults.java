package com.example.movie1;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Νικος Νακκας on 24/6/2018.
 */

public class VideoResults {

    public List<Trailer> getTrailerList() {
        return trailerList;
    }

    public void setTrailerList(List<Trailer> trailerList) {
        this.trailerList = trailerList;
    }

    @SerializedName("results")

    private List<Trailer> trailerList=new ArrayList<>();


}
