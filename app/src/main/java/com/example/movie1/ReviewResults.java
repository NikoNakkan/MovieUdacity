package com.example.movie1;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Νικος Νακκας on 22/6/2018.
 */

public class ReviewResults
{




        @SerializedName("results")


        private List<Review> reviewLis=new ArrayList<>();

        public List<Review> getReviewLis() {
            return reviewLis;
        }


    }








