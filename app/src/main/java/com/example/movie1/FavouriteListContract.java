package com.example.movie1;

import android.net.Uri;
import android.provider.BaseColumns;

import java.net.URI;

/**
 * Created by Νικος Νακκας on 1/7/2018.
 */

public class FavouriteListContract {

    public static final String AUTHORITY ="com.example.movie1";

    public static final Uri BASE_CONTENT_URI= Uri.parse("content://"+AUTHORITY);

    public static final String PATH_FAVOURITES="favourites";

    public static final class FavouriteListEntry implements BaseColumns{

        public static final Uri CONTENT_URI=BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVOURITES).build();

        public static final String TABLE_NAME="favouriteList";
        public static final String COLUMN_MOVIE_ID="movieId";
        public static final String COLUMN_TITLE="title";
        public static final String COLUMN_POSTER_PATH="posterPath";
        public static final String CULUMN_OVERVIEW="overview";
        public static final String COLUMN_AVERAGE="average";
        public static final String COLUMN_DATE="date";
        public static final String COLUMN_FAVOURITED="favourited";
    }
}
