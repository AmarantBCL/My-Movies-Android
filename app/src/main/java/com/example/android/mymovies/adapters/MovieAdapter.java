package com.example.android.mymovies.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.mymovies.R;
import com.example.android.mymovies.data.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movies = new ArrayList<>();
    private OnItemClickListener listener;
    private OnReachEndListener reachEndListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnReachEndListener {
        void onReachEnd();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        if (reachEndListener != null && movies.size() >= 20 && position > movies.size() - 4) {
            reachEndListener.onReachEnd();
        }
        Movie movie = movies.get(position);
        Picasso.get().load(movie.getPosterPath()).into(holder.imageViewMovie);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void clear() {
        movies.clear();
        notifyDataSetChanged();
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    public void addMovies(List<Movie> movies) {
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnReachEndListener(OnReachEndListener listener) {
        this.reachEndListener = listener;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewMovie;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewMovie = itemView.findViewById(R.id.img_movie);
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}
