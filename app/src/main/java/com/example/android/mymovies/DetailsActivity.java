package com.example.android.mymovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.mymovies.architecture.MainViewModel;
import com.example.android.mymovies.data.FavoriteMovie;
import com.example.android.mymovies.data.Movie;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class DetailsActivity extends AppCompatActivity {
    private ImageView imageViewLogo, imageViewStar;
    private TextView textViewTitle, textViewOriginal, textViewRelease, textViewRating, textViewDesc;
    private MainViewModel viewModel;
    private int id;
    private Movie movie;
    private FavoriteMovie favoriteMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        initViews();
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("id")) {
            id = intent.getIntExtra("id", -1);
        } else {
            finish();
        }
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        loadMovieInfo();
        imageViewStar.setOnClickListener(v -> clickFavorite());
        setFavorite();
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

    public static Intent newInstance(Context context, int id) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra("id", id);
        return intent;
    }

    private void clickFavorite() {
        if (favoriteMovie == null) {
            viewModel.insertFavoriteMovie(new FavoriteMovie(movie));
            Toast.makeText(DetailsActivity.this, R.string.add_to_favorite, Toast.LENGTH_SHORT).show();
        } else {
            viewModel.deleteFavoriteMovie(favoriteMovie);
            Toast.makeText(DetailsActivity.this, R.string.remove_from_favorite, Toast.LENGTH_SHORT).show();
        }
        setFavorite();
    }

    private void setFavorite() {
        favoriteMovie = viewModel.getFavoriteMovieById(id);
        if (favoriteMovie == null) {
            imageViewStar.setImageResource(R.drawable.fav_gray);
        } else {
            imageViewStar.setImageResource(R.drawable.fav_yellow);
        }
    }

    private void loadMovieInfo() {
        movie = viewModel.getMovieById(id);
        Picasso.get().load(movie.getBigPosterPath()).into(imageViewLogo);
        textViewTitle.setText(movie.getTitle());
        textViewOriginal.setText(movie.getOriginalTitle());
        textViewRelease.setText(movie.getReleaseDate());
        textViewRating.setText(String.format(Locale.getDefault(), "%s", movie.getVoteAverage()));
        textViewDesc.setText(movie.getOverview());
    }

    private void initViews() {
        imageViewLogo = findViewById(R.id.img_logo);
        imageViewStar = findViewById(R.id.img_star);
        textViewTitle = findViewById(R.id.tv_title);
        textViewOriginal = findViewById(R.id.tv_original);
        textViewRelease = findViewById(R.id.tv_release);
        textViewRating = findViewById(R.id.tv_rating);
        textViewDesc = findViewById(R.id.tv_desc);
    }
}