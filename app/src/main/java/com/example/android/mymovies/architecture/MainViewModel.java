package com.example.android.mymovies.architecture;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.android.mymovies.data.Movie;
import com.example.android.mymovies.database.MovieDatabase;

import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainViewModel extends AndroidViewModel {
    private static MovieDatabase database;
    private LiveData<List<Movie>> movies;

    public MainViewModel(@NonNull Application application) {
        super(application);
        database = MovieDatabase.getInstance(application);
        movies = database.movieDAO().getAllMovies();
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    public Movie getMovieById(int id) {
        try {
            return new GetMovieTask().execute(id).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insertMovie(Movie movie) {
        new Thread(() -> {
           database.movieDAO().insertMovie(movie);
        }).start();
    }

    public void deleteAllMovies() {
        new Thread(() -> {
            database.movieDAO().deleteAllMovies();
        }).start();
    }

    public void deleteMovie(Movie movie) {
        new Thread(() -> {
            database.movieDAO().deleteMovie(movie);
        }).start();
    }

    private static class GetMovieTask extends AsyncTask<Integer, Void, Movie> {
        @Override
        protected Movie doInBackground(Integer... integers) {
            if (integers != null && integers.length > 0) {
                return database.movieDAO().getMovieById(integers[0]);
            }
            return null;
        }
    }
}
