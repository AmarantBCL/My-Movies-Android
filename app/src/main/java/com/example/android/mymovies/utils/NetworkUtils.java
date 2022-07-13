package com.example.android.mymovies.utils;

import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class NetworkUtils {
    // URLs
    private static final String BASE_URL = "https://api.themoviedb.org/3/discover/movie";
    private static final String BASE_VIDEOS_URL = "https://api.themoviedb.org/3/movie/%s/videos";
    private static final String BASE_REVIEWS_URL = "https://api.themoviedb.org/3/movie/%s/reviews";

    // Query params
    private static final String PARAMS_API_KEY = "api_key";
    private static final String PARAMS_LANGUAGE = "language";
    private static final String PARAMS_SORT_BY = "sort_by";
    private static final String PARAMS_PAGE = "page";

    // Settings
    private static final String API_KEY = "692a162b1d95c63d2cca4046b3e7f851";
    private static final String LANGUAGE_VALUE = "ru-RU";
    private static final String SORT_BY_POPULARITY = "popularity.desc";
    private static final String SORT_BY_TOP_RATED = "vote_average.desc";

    // Sorting
    public static final int POPULARITY = 0;
    public static final int TOP_RATED = 1;

    public static JSONObject getJSONForVideos(int id) {
        JSONObject result = null;
        URL url = buildTrailersUrl(id);
        try {
            result = new JSONLoadTask().execute(url).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static JSONObject getJSONForReviews(int id) {
        JSONObject result = null;
        URL url = buildReviewsUrl(id);
        try {
            result = new JSONLoadTask().execute(url).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static JSONObject getJSONFromNetwork(int sortBy, int page) {
        JSONObject result = null;
        URL url = buildMovieUrl(sortBy, page);
        try {
            result = new JSONLoadTask().execute(url).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static URL buildTrailersUrl(int id) {
        URL result = null;
        Uri uri = Uri.parse(String.format(BASE_VIDEOS_URL, id)).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY, API_KEY)
                .appendQueryParameter(PARAMS_LANGUAGE, LANGUAGE_VALUE)
                .build();
        try {
            result = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static URL buildReviewsUrl(int id) {
        URL result = null;
        String page = "1";
        Uri uri = Uri.parse(String.format(BASE_REVIEWS_URL, id)).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY, API_KEY)
                .appendQueryParameter(PARAMS_LANGUAGE, LANGUAGE_VALUE)
                .appendQueryParameter(PARAMS_PAGE, page)
                .build();
        try {
            result = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static URL buildMovieUrl(int sortBy, int page) {
        URL result = null;
        String sortMethod = sortBy == POPULARITY ? SORT_BY_POPULARITY : SORT_BY_TOP_RATED;
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY, API_KEY)
                .appendQueryParameter(PARAMS_LANGUAGE, LANGUAGE_VALUE)
                .appendQueryParameter(PARAMS_SORT_BY, sortMethod)
                .appendQueryParameter(PARAMS_PAGE, Integer.toString(page))
                .build();
        try {
            result = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static class JSONLoadTask extends AsyncTask<URL, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(URL... urls) {
            JSONObject result = null;
            if (urls == null || urls.length == 0) return null;
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) urls[0].openConnection();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()))) {
                    StringBuilder sb = new StringBuilder();
                    String line = reader.readLine();
                    while (line != null) {
                        sb.append(line);
                        line = reader.readLine();
                    }
                    result = new JSONObject(sb.toString());
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return result;
        }
    }
}
