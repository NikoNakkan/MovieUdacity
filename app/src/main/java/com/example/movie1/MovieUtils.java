package com.example.movie1;

import android.net.Uri;
import android.util.Log;

import com.example.movie1.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Νικος Νακκας on 24/2/2018.
 */

public class MovieUtils {
    final static String TMDB_BASE_URL="https://api.themoviedb.org/3/movie/";
    final static String MY_API_KEY="yourkey";
    final static String KEY_AS_STRING="api_key";
    final static String REVIEW_PATH_STRING="reviews";
    final static String TRAILER_PATH_STRING="videos";

    public static URL buildUrl(String TMDBSearchQuery) {
        Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon().appendPath(TMDBSearchQuery)
                .appendQueryParameter(KEY_AS_STRING,MY_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildReviewUrl(String TMDBSearchQuery) {
        Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon().appendPath(TMDBSearchQuery)
                .appendPath(REVIEW_PATH_STRING)
                .appendQueryParameter(KEY_AS_STRING,MY_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }



    public static URL buildTrailerUrl(String TMDBSearchQuery) {
        Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon().appendPath(TMDBSearchQuery)
                .appendPath(TRAILER_PATH_STRING)
                .appendQueryParameter(KEY_AS_STRING,MY_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }







     static String getResponseFromHttpUrl(URL url) throws IOException {
         HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

         try {

            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
