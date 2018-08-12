package com.example.movie1;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.google.gson.Gson;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieListClickListener {

    private MovieAdapter adapter;
    private RecyclerView recyclerView;
    protected final String POPULAR = "popular";
    protected final String TOP_RATED = "top_rated";
    private  List<Movie> list = new ArrayList<>();
    private static int superSecretCode=0;
    private static int searchId=R.id.popular;
    private Parcelable adapterState;
    GridLayoutManager gridLayoutManager;
    private final String ADAPTER_STATE="AdapterState";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapteredListMaking();
        makeSearchQuery(searchId);
    }

    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);

        adapterState = gridLayoutManager.onSaveInstanceState();
        state.putParcelable(ADAPTER_STATE, adapterState);
    }

    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);

        if(state != null)
            adapterState = state.getParcelable(ADAPTER_STATE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (adapterState != null) {
            gridLayoutManager.onRestoreInstanceState(adapterState);
            if(superSecretCode==R.id.favourites){
                list.clear();
                makeSearchQuery(R.id.favourites);
            }

        }
    }



    private void adapteredListMaking() {
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.rv_movies);
        adapter=new MovieAdapter(getApplicationContext(), list,MainActivity.this );
    }


    private void makeSearchQuery(int Id) {
        ConnectivityManager cm =
                (ConnectivityManager)MainActivity.this
                        .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();///////////////////ADDED THE CONNECTION CHECK
        boolean isConnected = activeNetwork != null &&         ///////////////// FROM REVIEW 2
                activeNetwork.isConnectedOrConnecting();
        if(isConnected||Id==R.id.favourites) {           //I STILL WANT THINGS TO SHOW UP US FAVOURITES DON'T NEED ANY NETWORK

            URL searchUrl = MovieUtils.buildUrl(chooseSortString(Id));
            new TMDBQueryTask().execute(searchUrl);
            superSecretCode = Id;
        }
        else{
            Toast.makeText(this, "Network connection failed", Toast.LENGTH_SHORT).show();
        }
    }


    public class TMDBQueryTask extends AsyncTask<URL, Void, String> {
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String searchResults = null;


            try {
                 if(superSecretCode==R.id.favourites){
                     Cursor cursor=getContentResolver().query(FavouriteListContract.FavouriteListEntry.CONTENT_URI,null,null,null,null );
                       Movie movie;
                       if(cursor.moveToFirst()) {
                           do {
                               movie = new Movie();
                               movie.setId(cursor.getInt(cursor.getColumnIndex(FavouriteListContract.FavouriteListEntry.COLUMN_MOVIE_ID)));
                               movie.setImage(cursor.getString(cursor.getColumnIndex(FavouriteListContract.FavouriteListEntry.COLUMN_POSTER_PATH)));
                               movie.setTitle(cursor.getString(cursor.getColumnIndex(FavouriteListContract.FavouriteListEntry.COLUMN_TITLE)));
                               movie.setPlotSynopsis(cursor.getString(cursor.getColumnIndex(FavouriteListContract.FavouriteListEntry.CULUMN_OVERVIEW)));
                               movie.setUserRating(cursor.getFloat(cursor.getColumnIndex(FavouriteListContract.FavouriteListEntry.COLUMN_AVERAGE)));
                               movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(FavouriteListContract.FavouriteListEntry.COLUMN_DATE)));
                               movie.setFavourited(1); //If I get it from the favourited list ,obv it is favourited.I need it in the case the user presses one of the favourite list objects
                               list.add(movie);
                           }
                           while (cursor.moveToNext());


                       }

                        cursor.close();


                    }
                    else{
                    searchResults = MovieUtils.getResponseFromHttpUrl(searchUrl);
                    if (!list.isEmpty())
                        list.clear();
                    Gson gson = new Gson();
                    Results results = gson.fromJson(searchResults, Results.class);
                    for (int i = 0; i < results.getMovieLis().size(); i++) {
                        list.add(results.getMovieLis().get(i));

                    }}


            } catch (IOException e) {
                e.printStackTrace();
            }
            return searchResults;
        }

        @Override

        protected void onPostExecute(String searchResults) {

            gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
            recyclerView.setLayoutManager(gridLayoutManager);
            adapter = new MovieAdapter(getApplicationContext(), list,MainActivity.this );
            recyclerView.setAdapter(adapter);


        }
    }


    protected String chooseSortString(int id) {

            if (id == R.id.popular) {
                return POPULAR;
            } else {
                return TOP_RATED;
            }
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.popular:
                list.clear();
                makeSearchQuery(R.id.popular);
                adapter.notifyDataSetChanged();
                searchId=R.id.popular;
                return true;
            case R.id.top_rated:
                list.clear();
                makeSearchQuery(R.id.top_rated);
                adapter.notifyDataSetChanged();
                searchId=R.id.top_rated;
                return true;
            case R.id.favourites:
                list.clear();
                makeSearchQuery(R.id.favourites);
                adapter.notifyDataSetChanged();
                searchId=R.id.favourites;
                return true;

        }
        return true;
    }

    @Override
    public void OnListItemClick(int clickedItemPosition) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("Object",list.get(clickedItemPosition));

        startActivity(intent);


    }


}




















////
