package com.example.android.mymovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.mymovies.adapters.MovieAdapter;
import com.example.android.mymovies.data.Movie;
import com.example.android.mymovies.utils.JSONUtils;
import com.example.android.mymovies.utils.NetworkUtils;

import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        adapter = new MovieAdapter();
        JSONObject jsonObject = NetworkUtils.getJSONFromNetwork(NetworkUtils.POPULARITY, 1);
        List<Movie> movies = JSONUtils.getMoviesFromJSON(jsonObject);
        adapter.setMovies(movies);
        recyclerView.setAdapter(adapter);
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recycler_view_movies);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }
}