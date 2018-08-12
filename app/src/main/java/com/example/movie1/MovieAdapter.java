package com.example.movie1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Νικος Νακκας on 23/2/2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{
    private List<Movie> movieList;
    public String IMAGE_BASE_URL="http://image.tmdb.org/t/p/w185";
    final private MovieListClickListener movieListClickListener;



    public MovieAdapter(Context context, List<Movie> movieList,MovieListClickListener movieListClickListener) {
        this.movieList = movieList;
        this.movieListClickListener=movieListClickListener;


    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item,viewGroup,false );
        MovieViewHolder viewHolder = new MovieViewHolder(view);
        return viewHolder;
    }


    public interface MovieListClickListener{
        void OnListItemClick(int clickedItemPosition);

    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

        Movie movie=movieList.get(position);
        Picasso.with(holder.itemView.getContext()).load(IMAGE_BASE_URL+movie.getImage()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {

            return movieList.size();

    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        protected ImageView imageView;
        public MovieViewHolder(View view){
            super(view);
            imageView=(ImageView)view.findViewById(R.id.iv_list_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition=getAdapterPosition();
            movieListClickListener.OnListItemClick(clickedPosition);
        }
    }
}
