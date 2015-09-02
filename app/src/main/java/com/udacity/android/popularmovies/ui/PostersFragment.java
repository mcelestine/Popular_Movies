package com.udacity.android.popularmovies.ui;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.udacity.android.popularmovies.MovieAdapter;
import com.udacity.android.popularmovies.R;
import com.udacity.android.popularmovies.model.Movie;
import com.udacity.android.popularmovies.model.MovieResults;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class PostersFragment extends Fragment {

    private static final String EXTRA_MOVIE_OBJECT = "movie";

    private MovieAdapter mMovieAdapter;
    private ArrayList<String> mPosterPaths;
    private ArrayList<Movie> mMovies;

    public PostersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (isNetworkAvailable()) {
            updateMovies();
        } else {
            Toast.makeText(getActivity(), "Network is unavailable!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.postersfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // Refresh button is for debugging purposes only
        // Will be removed
        // Should execute the task in onCreateView
        if (id == R.id.action_refresh) {
            updateMovies();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateMovies() {
        FetchMoviesTask moviesTask = new FetchMoviesTask();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String sortOrder = prefs.getString(getString(R.string.pref_sort_order_key),
                getString(R.string.pref_sort_order_most_popular));

        moviesTask.execute(sortOrder);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }

        return isAvailable;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_posters, container, false);

        GridView gridView = (GridView) rootView.findViewById(R.id.posterGrid);

        mMovieAdapter = new MovieAdapter(getActivity());
        gridView.setAdapter(mMovieAdapter);

        TextView emptyTextView = (TextView) rootView.findViewById(android.R.id.empty);
        gridView.setEmptyView(emptyTextView);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Movie movie = MovieResults.getInstance(getActivity()).getMovie(position);

                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(EXTRA_MOVIE_OBJECT, movie);
                startActivity(intent);
            }
        });

        return rootView;
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<String>> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected ArrayList<String> doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviesJsonStr = null;

            String sortOrder;

            if (params.length == 0) {
                // Fetch most popular by default
                sortOrder = "popularity";
            } else {
                sortOrder = params[0];
            }

            try {
                // Construct the URL for theMovieDB query
                final String MOVIES_BASE_URL =
                        "http://api.themoviedb.org/3/discover/movie?";
                final String QUERY_PARAM = "sort_by";
                //final String ORDER_OPTION_PARAM = "popularity.desc";
                final String ORDER_OPTION_PARAM = sortOrder + "." + "desc";
                final String API_KEY = "api_key";

                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, ORDER_OPTION_PARAM)
                        .appendQueryParameter(API_KEY, "4ee9982eaf1fc27a602da4936c07cc36")
                        .build();

                URL url = new URL(builtUri.toString());

                // Create the request to themoviedb, and open the connnection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line = "";
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a lot easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty. No point in parsing
                    return null;
                }

                moviesJsonStr = buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error", e);
                // If code didn't successfully get the movie data, there's no point in attempting
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }

                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            // Do JSON parsing
            try {
                return getMovieDataFromJSON(moviesJsonStr);

            } catch (JSONException e) {
                Log.e(LOG_TAG, "JSON Error", e);
                e.printStackTrace();
            }
            return null;
        }

        private ArrayList<String> getMovieDataFromJSON(String moviesJsonStr)
                throws JSONException {

            final String BASE_POSTER_PATH = "http://image.tmdb.org/t/p/";
            final String POSTER_SIZE = "w185";

            final String MOVIE_POSTER_PATH = "poster_path";
            final String MOVIE_ORIGINAL_TITLE = "original_title";
            final String MOVIE_ID = "id";
            final String MOVIE_SYNOPSIS = "overview";
            final String MOVIE_RATING = "vote_average";
            final String MOVIE_RELEASE_DATE = "release_date";

            // Construct JSONObject out of the parameter string
            JSONObject moviesJson = new JSONObject(moviesJsonStr);

            JSONArray moviesResultsArray = moviesJson.getJSONArray("results");

            // Parse array to gather poster urls and other data for each movie
            mPosterPaths = new ArrayList<>();
            mMovies = new ArrayList<>();

            for (int i = 0; i < moviesResultsArray.length(); i++) {

                JSONObject jsonMovie = moviesResultsArray.getJSONObject(i);
                // Create a Movie object
                Movie movie = new Movie();
                movie.setId(jsonMovie.getInt(MOVIE_ID));
                movie.setOriginalTitle(jsonMovie.getString(MOVIE_ORIGINAL_TITLE));
                movie.setSynopsis(jsonMovie.getString(MOVIE_SYNOPSIS));
                movie.setUserRating(jsonMovie.getDouble(MOVIE_RATING));
                movie.setReleaseDate(jsonMovie.getString(MOVIE_RELEASE_DATE));
                movie.setPosterPath(BASE_POSTER_PATH + POSTER_SIZE + jsonMovie.getString(MOVIE_POSTER_PATH));

                mMovies.add(movie);
                // Get the poster path for this movie and add it to our list of movie poster urls
                // TODO: Check if there's a better way to add/create these strings. Instead of concatenation.
                mPosterPaths.add(BASE_POSTER_PATH + POSTER_SIZE + jsonMovie.getString(MOVIE_POSTER_PATH));
            }
            return mPosterPaths;
        }

        @Override
        protected void onPostExecute(ArrayList<String> movies) {
            mMovieAdapter.setGridData(movies);
            MovieResults.getInstance(getActivity()).replace(mMovies);
        }
    }
}
