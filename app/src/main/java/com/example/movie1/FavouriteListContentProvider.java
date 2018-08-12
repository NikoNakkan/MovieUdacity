package com.example.movie1;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Νικος Νακκας on 1/7/2018.
 */

public class FavouriteListContentProvider extends ContentProvider {
    private FavouriteListDbHelper dbHelper;

    public static final int FAVOURITE=100;
    public static final int FAVOURITEBYID=101;
    public static final  UriMatcher sUriMatcher=buildUriMatcher();

    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(FavouriteListContract.AUTHORITY,FavouriteListContract.PATH_FAVOURITES,FAVOURITE);
        uriMatcher.addURI(FavouriteListContract.AUTHORITY,FavouriteListContract.PATH_FAVOURITES+"/#",FAVOURITEBYID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper=new FavouriteListDbHelper(getContext());


        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        final SQLiteDatabase sqLiteDatabase=dbHelper.getReadableDatabase();
        int match=sUriMatcher.match(uri);
        Cursor qCursor=null;

        switch (match){
            case FAVOURITE:
                qCursor=sqLiteDatabase.query(FavouriteListContract.FavouriteListEntry.TABLE_NAME,strings,s,strings1,null,null,s1);

                qCursor.setNotificationUri(getContext().getContentResolver(),uri);
                break;
            case FAVOURITEBYID:
                String movieId=uri.getPathSegments().get(1);
                qCursor=sqLiteDatabase.query(FavouriteListContract.FavouriteListEntry.TABLE_NAME,strings,"_id=?",new String[]{movieId},null ,null,s1);
                break;
        }

        return qCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
       final SQLiteDatabase db= dbHelper.getWritableDatabase();
       int match=sUriMatcher.match(uri);

        Uri returnUri=null;
       switch(match){
           case FAVOURITE:
               long id=db.insert(FavouriteListContract.FavouriteListEntry.TABLE_NAME,null,contentValues);
               if(id>0){
                   returnUri= ContentUris.withAppendedId(FavouriteListContract.FavouriteListEntry.CONTENT_URI,id);
               }

       }
       getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int favouriteDeleted = 0;


        switch (match) {
            case FAVOURITE:

                favouriteDeleted = db.delete(FavouriteListContract.FavouriteListEntry.TABLE_NAME, s,strings);
                break;
            case FAVOURITEBYID:
                String id=uri.getPathSegments().get(1);
                favouriteDeleted=db.delete(FavouriteListContract.FavouriteListEntry.TABLE_NAME,"_id=?",new String[]{id});
                break;
            default:
                Log.v("Uri", "Uri error");
        }
        if(favouriteDeleted!=0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return favouriteDeleted;


        }


    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        try {
            throw new Exception("fail occured");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
