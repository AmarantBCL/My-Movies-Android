package com.example.android.mymovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.android.mymovies.adapters.MovieAdapter;
import com.example.android.mymovies.architecture.MainViewModel;
import com.example.android.mymovies.data.FavoriteMovie;
import com.example.android.mymovies.data.Movie;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        recyclerView = findViewById(R.id.recycler_view_favorites);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new MovieAdapter();
        recyclerView.setAdapter(adapter);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        LiveData<List<FavoriteMovie>> favoriteMovies = viewModel.getFavoriteMovies();
        favoriteMovies.observe(this, new Observer<List<FavoriteMovie>>() {
            @Override
            public void onChanged(List<FavoriteMovie> favoriteMovies) {
                List<Movie> movies = new ArrayList<>();
                if (favoriteMovies != null) {
                    movies.addAll(favoriteMovies);
                    adapter.setMovies(movies);
                }
            }
        });
        adapter.setOnItemClickListener(position -> {
            Movie movie = adapter.getMovies().get(position);
            Intent intent = DetailsActivity.newInstance(FavoritesActivity.this, movie.getId());
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_favorites, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item_main:
                Intent intent = MainActivity.newInstance(this);
                startActivity(intent);
                break;
            case R.id.item_favorites:
                Intent intentFavorites = FavoritesActivity.newInstance(this);
                startActivity(intentFavorites);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, FavoritesActivity.class);
        return intent;
    }
}