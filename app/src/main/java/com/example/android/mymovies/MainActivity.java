package com.example.android.mymovies;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.mymovies.adapters.MovieAdapter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
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
            Toast.makeText(MainActivity.this, "Click " + position, Toast.LENGTH_SHORT).show();
        });
        adapter.setOnReachEndListener(() -> {
            Toast.makeText(MainActivity.this, "Конец списка", Toast.LENGTH_SHORT).show();
        });
    }

    private void switchSorting(boolean isChecked) {
        int methodSort = isChecked ? NetworkUtils.TOP_RATED : NetworkUtils.POPULARITY;
        int popularColor = isChecked ? R.color.white : R.color.pink;
        int topRatedColor = isChecked ? R.color.pink : R.color.white;
        textViewPopular.setTextColor(ContextCompat.getColor(MainActivity.this, popularColor));
        textViewTopRated.setTextColor(ContextCompat.getColor(MainActivity.this, topRatedColor));
        JSONObject jsonObject = NetworkUtils.getJSONFromNetwork(methodSort, 1);
        List<Movie> movies = JSONUtils.getMoviesFromJSON(jsonObject);
        adapter.setMovies(movies);
    }

    private void initViews() {
        switchSort = findViewById(R.id.switch_sort);
        textViewPopular = findViewById(R.id.tv_most_popular);
        textViewTopRated = findViewById(R.id.tv_top_rated);
        recyclerView = findViewById(R.id.recycler_view_movies);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }
}