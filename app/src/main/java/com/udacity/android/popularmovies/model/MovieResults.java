package com.udacity.android.popularmovies.model;

import android.content.Context;

import java.util.ArrayList;

public class MovieResults {

    private static MovieResults sMovieResults;
    private Context mContext;
    private ArrayList<Movie> mMovies;

    /**
     * A private constructor prevents any other class from instantiating.
     */
    private MovieResults(Context context) {
        mContext = context;
        mMovies = new ArrayList<>();
    }

    public static MovieResults getInstance(Context context) {
        if (sMovieResults == null) {
            sMovieResults = new MovieResults(context.getApplicationContext());
        }
        return sMovieResults;
    }

    // Insert a movie
    public void addMovie(Movie movie) {
        mMovies.add(movie);
    }

    // Get a movie
    public Movie getMovie(int position) {
        return mMovies.get(position);
    }

    // Get movies
    public ArrayList<Movie> getMovies() {
        return mMovies;
    }

    // Replace movies in the list with the one given
    public void replace(ArrayList<Movie> movies) {
        mMovies.clear();
        mMovies.addAll(movies);
    }

    public int size() {
        return mMovies.size();
    }
}
