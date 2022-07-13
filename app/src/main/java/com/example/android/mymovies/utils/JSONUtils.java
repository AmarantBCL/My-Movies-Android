package com.example.android.mymovies.utils;

import com.example.android.mymovies.data.Movie;
import com.example.android.mymovies.data.Review;
import com.example.android.mymovies.data.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONUtils {
    private static final String KEY_RESULTS = "results";

    // Trailers
    private static final String BASE_YOUTUBE_URL = "https://www.youtube.com/watch?v=";
    private static final String KEY_VIDEO_NAME = "name";
    private static final String KEY_VIDEO_KEY = "key";

    // Reviews
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_CONTENT = "content";

    // Movies
    private static final String KEY_VOTE_COUNT = "vote_count";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_ORIGINAL_TITLE = "original_title";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_POSTER_PATH = "poster_path";
    private static final String KEY_BACKDROP_PATH = "backdrop_path";
    private static final String KEY_VOTE_AVERAGE = "vote_average";
    private static final String KEY_RELEASE_DATE = "release_date";

    // Posters
    public static final String BASE_POSTER_URL = "https://image.tmdb.org/t/p/";
    public static final String SMALL_POSTER_SIZE = "w185";
    public static final String BIG_POSTER_SIZE = "w780";

    public static List<Trailer> getTrailersFromJSON(JSONObject jsonObject) {
        List<Trailer> result = new ArrayList<>();
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(KEY_RESULTS);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject arrayObject = jsonArray.getJSONObject(i);
                String name = arrayObject.getString(KEY_VIDEO_NAME);
                String key = BASE_YOUTUBE_URL + arrayObject.getString(KEY_VIDEO_KEY);
                result.add(new Trailer(name, key));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<Review> getReviewsFromJSON(JSONObject jsonObject) {
        List<Review> result = new ArrayList<>();
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(KEY_RESULTS);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject arrayObject = jsonArray.getJSONObject(i);
                String author = arrayObject.getString(KEY_AUTHOR);
                String content = arrayObject.getString(KEY_CONTENT);
                result.add(new Review(author, content));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<Movie> getMoviesFromJSON(JSONObject jsonObject) {
        List<Movie> result = new ArrayList<>();
        if (jsonObject == null) return result;
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(KEY_RESULTS);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject objectMovie = jsonArray.getJSONObject(i);
                int id = objectMovie.getInt(KEY_ID);
                int voteCount = objectMovie.getInt(KEY_VOTE_COUNT);
                String title = objectMovie.getString(KEY_TITLE);
                String originalTitle = objectMovie.getString(KEY_ORIGINAL_TITLE);
                String overview = objectMovie.getString(KEY_OVERVIEW);
                String posterPath = BASE_POSTER_URL + SMALL_POSTER_SIZE + objectMovie.getString(KEY_POSTER_PATH);
                String bigPosterPath = BASE_POSTER_URL + BIG_POSTER_SIZE + objectMovie.getString(KEY_POSTER_PATH);
                String backdropPath = objectMovie.getString(KEY_BACKDROP_PATH);
                double voteAverage = objectMovie.getDouble(KEY_VOTE_AVERAGE);
                String releaseDate = objectMovie.getString(KEY_RELEASE_DATE);
                Movie movie = new Movie(id, voteCount, title, originalTitle, overview, posterPath,
                        bigPosterPath, backdropPath, voteAverage, releaseDate);
                result.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}
