package com.example.movie1;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Νικος Νακκας on 24/2/2018.
 */

public class Results
{


        @SerializedName("results")

        private List<Movie> movieLis = new ArrayList<>();



    public List<Movie> getMovieLis() {
            return movieLis;
        }

        public void setMovieLis(List<Movie> movie) {
            movieLis= movie;
        }
    }



