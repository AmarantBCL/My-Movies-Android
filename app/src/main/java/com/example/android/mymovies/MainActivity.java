package com.example.android.mymovies;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.mymovies.adapters.MovieAdapter;
import com.example.android.mymovies.architecture.MainViewModel;
import com.example.android.mymovies.data.Movie;
import com.example.android.mymovies.utils.JSONUtils;
import com.example.android.mymovies.utils.NetworkUtils;

import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Switch switchSort;
    private TextView textViewPopular, textViewTopRated;
    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        adapter = new MovieAdapter();
        recyclerView.setAdapter(adapter);
        switchSort.setChecked(true);
        switchSort.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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
            Toast.makeText(MainActivity.this, "Конец списка", Toast.LENGTH_SHORT).show();
        });
        LiveData<List<Movie>> moviesFromLD = viewModel.getMovies();
        moviesFromLD.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                adapter.setMovies(movies);
            }
        });
    }

    private void switchSorting(boolean isChecked) {
        int methodSort = isChecked ? NetworkUtils.TOP_RATED : NetworkUtils.POPULARITY;
        int popularColor = isChecked ? R.color.white : R.color.pink;
        int topRatedColor = isChecked ? R.color.pink : R.color.white;
        textViewPopular.setTextColor(ContextCompat.getColor(MainActivity.this, popularColor));
        textViewTopRated.setTextColor(ContextCompat.getColor(MainActivity.this, topRatedColor));
        downloadData(methodSort, 1);
    }

    private void downloadData(int methodSort, int page) {
        JSONObject jsonObject = NetworkUtils.getJSONFromNetwork(methodSort, page);
        List<Movie> movies = JSONUtils.getMoviesFromJSON(jsonObject);
        if (movies != null && !movies.isEmpty()) {
//            viewModel.deleteAllMovies(); // TODO optimize deleteing & inserting
//            for (Movie movie : movies) {
//                viewModel.insertMovie(movie);
//            }
        }
    }

    private void initViews() {
        switchSort = findViewById(R.id.switch_sort);
        textViewPopular = findViewById(R.id.tv_most_popular);
        textViewTopRated = findViewById(R.id.tv_top_rated);
        recyclerView = findViewById(R.id.recycler_view_movies);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }
}