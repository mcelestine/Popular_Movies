package com.udacity.android.popularmovies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Makina Celestine on 7/28/15.
 */
public class MovieDataParser {

    /**
     * Given a string of the form returned by the api call
     * retrieve the movie data
     */


    /**
     *
     * @param moviesJsonStr
     * @return
     * @throws JSONException
     */
    public List<String> getMoviePosterUrlsFromJson(String moviesJsonStr)
            throws JSONException {

        final String BASE_POSTER_PATH = "http://image.tmdb.org/t/p/";
        final String POSTER_SIZE = "w185";
        final String TMDB_RESULTS = "results";
        final String TMDB_POSTER_PATH = "poster_path";

        // Construct JSONObject out of the parameter string
        JSONObject moviesJson = new JSONObject(moviesJsonStr);

        // Get the movies json array
        JSONArray moviesResultsArray = moviesJson.getJSONArray(TMDB_RESULTS);

        // Parse array to get movie poster path for each movie
        List<String> moviePostersUrls = new ArrayList<>();

        for (int i = 0; i < moviesResultsArray.length(); i++) {
            // Get the poster path for this movie
            JSONObject movie = moviesResultsArray.getJSONObject(i);

            // Add it to our list of movie poster urls
            // TODO: Check if there's a better way to add/create these strings. Instead of concatenation.
            moviePostersUrls.add(BASE_POSTER_PATH + POSTER_SIZE + movie.getString(TMDB_POSTER_PATH));
        }

        return moviePostersUrls;
    }
}
