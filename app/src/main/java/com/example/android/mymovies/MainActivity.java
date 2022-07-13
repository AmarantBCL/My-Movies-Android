package com.example.android.mymovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.mymovies.adapters.MovieAdapter;
import com.example.android.mymovies.architecture.MainViewModel;
import com.example.android.mymovies.data.Movie;
import com.example.android.mymovies.data.Trailer;
import com.example.android.mymovies.utils.JSONUtils;
import com.example.android.mymovies.utils.NetworkUtils;

import org.json.JSONObject;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<JSONObject> {
    private Switch switchSort;
    private ProgressBar progressBarLoading;
    private TextView textViewPopular, textViewTopRated;
    private RecyclerView recyclerView;

    private MovieAdapter adapter;
    private MainViewModel viewModel;

    private static final int LOADER_ID = 133;
    private LoaderManager loaderManager;

    private static int page = 1;
    private static int methodSort;
    private static boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loaderManager = LoaderManager.getInstance(this);
        initViews();
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        adapter = new MovieAdapter();
        recyclerView.setAdapter(adapter);
        switchSort.setChecked(true);
        switchSort.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                page = 1;
                switchSorting(isChecked);
            }
        });
        switchSort.setChecked(false);
        textViewPopular.setOnClickListener(v -> switchSort.setChecked(!switchSort.isChecked()));
        textViewTopRated.setOnClickListener(v -> switchSort.setChecked(!switchSort.isChecked()));
        adapter.setOnItemClickListener(position -> {
            Movie movie = adapter.getMovies().get(position);
            Intent intent = DetailsActivity.newInstance(MainActivity.this, movie.getId());
            startActivity(intent);
        });
        adapter.setOnReachEndListener(() -> {
            if (!isLoading) {
                downloadData(methodSort, page);
            }
        });
        LiveData<List<Movie>> moviesFromLD = viewModel.getMovies();
        moviesFromLD.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                if (page == 1) {
                    adapter.setMovies(movies);
                }
            }
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
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    private void switchSorting(boolean isChecked) {
        methodSort = isChecked ? NetworkUtils.TOP_RATED : NetworkUtils.POPULARITY;
        int popularColor = isChecked ? R.color.white : R.color.pink;
        int topRatedColor = isChecked ? R.color.pink : R.color.white;
        textViewPopular.setTextColor(ContextCompat.getColor(MainActivity.this, popularColor));
        textViewTopRated.setTextColor(ContextCompat.getColor(MainActivity.this, topRatedColor));
        downloadData(methodSort, page);
    }

    private void downloadData(int methodOfSort, int page) {
        URL url = NetworkUtils.buildMovieUrl(methodOfSort, page);
        Bundle bundle = new Bundle();
        bundle.putString("url", url.toString());
        loaderManager.restartLoader(LOADER_ID, bundle, this);
    }

    private void initViews() {
        switchSort = findViewById(R.id.switch_sort);
        progressBarLoading = findViewById(R.id.pbar_loading);
        textViewPopular = findViewById(R.id.tv_most_popular);
        textViewTopRated = findViewById(R.id.tv_top_rated);
        recyclerView = findViewById(R.id.recycler_view_movies);
        recyclerView.setLayoutManager(new GridLayoutManager(this, getColumnCount()));
    }

    private int getColumnCount() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = (int) (displayMetrics.widthPixels / displayMetrics.density);
        return width / 185 > 2 ? width / 185 : 2;
    }

    @NonNull
    @Override
    public Loader<JSONObject> onCreateLoader(int id, @Nullable Bundle bundle) {
        NetworkUtils.JSONLoader jsonLoader = new NetworkUtils.JSONLoader(this, bundle);
        jsonLoader.setOnStartLoadingListener(new NetworkUtils.JSONLoader.OnStartLoadingListener() {
            @Override
            public void onStartLoading() {
                progressBarLoading.setVisibility(View.VISIBLE);
                isLoading = true;
            }
        });
        return jsonLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<JSONObject> loader, JSONObject data) {
        List<Movie> movies = JSONUtils.getMoviesFromJSON(data);
        if (movies != null && !movies.isEmpty()) {
            if (page == 1) {
                viewModel.deleteAllMovies();
                adapter.clear();
            }
            for (Movie movie : movies) {
                viewModel.insertMovie(movie);
            }
            adapter.addMovies(movies);
            page++;
        }
        isLoading = false;
        progressBarLoading.setVisibility(View.INVISIBLE);
        loaderManager.destroyLoader(LOADER_ID);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<JSONObject> loader) {

    }
}