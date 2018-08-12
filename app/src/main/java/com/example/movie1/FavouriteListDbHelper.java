package com.example.movie1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Νικος Νακκας on 1/7/2018.
 */

public class FavouriteListDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="favouriteLidt.db";
    public static final int DATABASE_VERSION=1;

    public FavouriteListDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FAVOURITELIST_TABLE="CREATE TABLE "+
                FavouriteListContract.FavouriteListEntry.TABLE_NAME+"("+
                FavouriteListContract.FavouriteListEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                FavouriteListContract.FavouriteListEntry.COLUMN_MOVIE_ID+" INTEGER NOT NULL,"+
                FavouriteListContract.FavouriteListEntry.COLUMN_TITLE+" TEXT NOT NULL,"+
                FavouriteListContract.FavouriteListEntry.COLUMN_POSTER_PATH+" TEXT NOT NULL,"+
                FavouriteListContract.FavouriteListEntry.CULUMN_OVERVIEW+" TEXT NOT NULL,"+
                FavouriteListContract.FavouriteListEntry.COLUMN_AVERAGE+" REAL NOT NULL,"+
                FavouriteListContract.FavouriteListEntry.COLUMN_DATE+" TEXT NOT NULL,"+
                FavouriteListContract.FavouriteListEntry.COLUMN_FAVOURITED+" INTEGER NOT NULL DEFAULT 0 "+

                ");";
        sqLiteDatabase.execSQL(SQL_CREATE_FAVOURITELIST_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavouriteListContract.FavouriteListEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
