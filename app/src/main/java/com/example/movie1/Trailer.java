package com.example.movie1;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Νικος Νακκας on 24/6/2018.
 */

public class Trailer {


    @SerializedName("key")
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
