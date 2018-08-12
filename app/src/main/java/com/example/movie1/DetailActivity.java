package com.example.movie1;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    public String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185";
    private String NO_REVIEWS_AVAILABLE="No reviews available";
    private String NO_TRAILERS_AVAILABLE="No trailers available";
    private static List<Review> reviewList =new ArrayList<>();
    private static List<Trailer> trailerList=new ArrayList<>();
    protected static int id=0;
    protected static String queryChoiceString="";
    protected static String TRAILER_STRING="Trailer";
    protected static String REVIEW_STRING="Review";

    TextView titleTv;
    TextView synopsisTv;
    TextView releaseDateTv;
    TextView userRatingTv;
    ImageView posterIv;
    LinearLayout linearLayout;
    Button heartButton;
    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        titleTv = findViewById(R.id.detail_movie_title_tv);
        releaseDateTv = findViewById(R.id.release_date_tv);
        synopsisTv = findViewById(R.id.synopsis_tv);
        userRatingTv = findViewById(R.id.user_rating_tv);
        posterIv = findViewById(R.id.detail_image_iv);
        trailerList.clear();
        heartButton=(Button)findViewById(R.id.heart_button);
        Intent intent = getIntent();
        movie=(Movie)intent.getSerializableExtra("Object");
        final Movie movie1 = (Movie) intent.getSerializableExtra("Object");
         id = movie1.getId();

        makeSearchQueryReview(id);

        titleTv.setText(movie1.getTitle());
        synopsisTv.setText(movie1.getPlotSynopsis());
        Picasso.with(this)
                .load(IMAGE_BASE_URL + movie1.getImage())
                .into(posterIv);
        userRatingTv.setText(String.valueOf(movie1.getUserRating()));
        releaseDateTv.setText(movie1.getReleaseDate());

        heartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(movie1.getFavourited()==0) { //check movie taken from intent,if it is not favourited:


                  Toast.makeText(DetailActivity.this, "Movie Favourited", Toast.LENGTH_SHORT).show();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(FavouriteListContract.FavouriteListEntry.COLUMN_TITLE, movie1.getTitle());
                    contentValues.put(FavouriteListContract.FavouriteListEntry.COLUMN_MOVIE_ID, movie1.getId());
                    contentValues.put(FavouriteListContract.FavouriteListEntry.COLUMN_AVERAGE, movie1.getUserRating());
                    contentValues.put(FavouriteListContract.FavouriteListEntry.COLUMN_DATE, movie1.getReleaseDate());
                    contentValues.put(FavouriteListContract.FavouriteListEntry.CULUMN_OVERVIEW, movie1.getPlotSynopsis());
                    contentValues.put(FavouriteListContract.FavouriteListEntry.COLUMN_POSTER_PATH, movie1.getImage());
                    contentValues.put(FavouriteListContract.FavouriteListEntry.COLUMN_FAVOURITED,1);
                    Uri uri = getContentResolver().insert(FavouriteListContract.FavouriteListEntry.CONTENT_URI, contentValues);
                    movie1.setFavourited(1);
                   if (uri != null) {
                   }
                }
           else{
                    Toast.makeText(DetailActivity.this, "Movie Unfavourited", Toast.LENGTH_SHORT).show();
                    String selection = FavouriteListContract.FavouriteListEntry.COLUMN_MOVIE_ID + "=?";
                    String[] selectionArgs = {String.valueOf(movie1.getId())};
                    getContentResolver().delete(FavouriteListContract.FavouriteListEntry.CONTENT_URI, selection, selectionArgs);
                    movie1.setFavourited(0);
               }


            }
        });
    }


    private void makeSearchQueryReview(int Id) {
        ConnectivityManager cm =
                (ConnectivityManager)DetailActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if(!isConnected)
            Toast.makeText(this, "Turn on connection for trailers and reviews", Toast.LENGTH_SHORT).show();
        queryChoiceString=REVIEW_STRING;
        URL SearchReviewUrl = MovieUtils.buildReviewUrl(String.valueOf(Id));
        new DetailActivity.TMDBQueryTask().execute(SearchReviewUrl);

    }

    private void makeSearchQueryTrailer(int Id) {
        queryChoiceString=TRAILER_STRING;
        URL SearchTrailerUrl = MovieUtils.buildTrailerUrl(String.valueOf(Id));
        new DetailActivity.TMDBQueryTask().execute(SearchTrailerUrl);

    }


   public class TMDBQueryTask extends AsyncTask<URL, Void, String> {
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String searchResults = null;
            try {


                if(queryChoiceString.equals(REVIEW_STRING)){
                    searchResults = MovieUtils.getResponseFromHttpUrl(searchUrl);
                Gson gson =new Gson();
                ReviewResults reviewResult=gson.fromJson(searchResults,ReviewResults.class);
                    for (int i = 0; i < reviewResult.getReviewLis().size(); i++) {
                    Review review = new Review();
                    review.setAuthor(reviewResult.getReviewLis().get(i).getAuthor());
                    review.setContent(reviewResult.getReviewLis().get(i).getContent());
                    reviewList.add(review);
                }
                    Cursor cursor=getContentResolver().query(FavouriteListContract.FavouriteListEntry.CONTENT_URI,null,null,null,null );
                    if(cursor.moveToFirst()) {
                        while (cursor.moveToNext()) {
                            if(id==cursor.getInt(cursor.getColumnIndex(FavouriteListContract.FavouriteListEntry.COLUMN_MOVIE_ID))){
                                movie.setFavourited(1);
                            }

                        }
                    }
                    cursor.close();

                }
                else {
                    if(queryChoiceString.equals(TRAILER_STRING)){
                        searchResults = MovieUtils.getResponseFromHttpUrl(searchUrl);
                        Gson gson =new Gson();

                        VideoResults trailerResult=gson.fromJson(searchResults,VideoResults.class);

                        for (int i = 0; i < trailerResult.getTrailerList().size(); i++) {
                            Trailer trailer= new Trailer();
                            trailer.setKey(trailerResult.getTrailerList().get(i).getKey());
                            trailerList.add(trailer);

                        }

                    }
                }


            } catch (IOException e) {
                e.printStackTrace();

            }
            return searchResults;
        }

        @Override

        protected void onPostExecute(String searchResults) {

            if (queryChoiceString.equals(REVIEW_STRING)) {
                populateLayoutReview();
                makeSearchQueryTrailer(id);
            }
           else if (queryChoiceString.equals(TRAILER_STRING)) {
                populateLayoutTrailer();
            }
        }



    }

    void populateLayoutReview() {
        linearLayout = (LinearLayout) findViewById(R.id.part2baselinear);
        TextView review;
        TextView author;
        TextView line;
        TextView emptyList;
        for (int i = 0; i < reviewList.size(); i++) {
            line = new TextView(DetailActivity.this);
            line.setWidth(100);
            line.setGravity(1);
            line.setHeight(1);
            line.setBackgroundColor(Color.WHITE);

            author = new TextView(DetailActivity.this);
            author.setText("By: " + reviewList.get(i).getAuthor());
            author.setTextSize(15);
            author.setGravity(1);
            author.setTypeface(null, Typeface.ITALIC);
            author.setTextColor(Color.WHITE);
            review = new TextView(DetailActivity.this);
            review.setTextSize(14);
            review.setText(reviewList.get(i).getContent());
            review.setTextColor(Color.WHITE);
            linearLayout.addView(author);
            linearLayout.addView(review);
            linearLayout.addView(line);

        }
        if (reviewList.size() == 0) {
            emptyList = new TextView(DetailActivity.this);
            emptyList.setText(NO_REVIEWS_AVAILABLE);
            emptyList.setGravity(1);
            emptyList.setTextSize(14);
            emptyList.setTextColor(Color.WHITE);
            linearLayout.addView(emptyList);
        }
            reviewList.clear();

    }
    void populateLayoutTrailer(){
        LinearLayout linearLayout1=findViewById(R.id.trailer_linear);
        Button button;
        TextView emptyList;
        for(int i=0;i<trailerList.size();i++){
            button=new Button(DetailActivity.this);
            final int k;
            k=i;
            button.setText("Trailer "+ (i+1));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    watchYoutubeVideo(DetailActivity.this,trailerList.get(k).getKey());
                }
            });
           linearLayout1.addView(button); }
        if (trailerList.size() == 0) {
            emptyList = new TextView(DetailActivity.this);
            emptyList.setText(NO_TRAILERS_AVAILABLE);
            emptyList.setGravity(1);
            emptyList.setTextSize(14);
            emptyList.setTextColor(Color.WHITE);
            linearLayout1.addView(emptyList);
        }


    }
    public static void watchYoutubeVideo(Context context, String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            context.startActivity(appIntent);
        } catch (Exception ex) {
            context.startActivity(webIntent);
        }
    }


}

