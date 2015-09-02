package com.udacity.android.popularmovies.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.android.popularmovies.R;
import com.udacity.android.popularmovies.model.Movie;

public class DetailActivity extends AppCompatActivity {
    private static final String EXTRA_MOVIE_OBJECT = "movie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            Movie movie = getIntent().getParcelableExtra(EXTRA_MOVIE_OBJECT);

//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.container, new DetailFragment())
//                    .commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.container,
                    DetailFragment.newInstance(movie)).commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class DetailFragment extends Fragment {

        private static final String EXTRA_MOVIE_OBJECT = "movie";

        private ImageView mPosterImageView;
        private TextView mOriginalTitleTextView;
        private TextView mUserRatingTextView;
        private TextView mReleaseDate;
        private TextView mPlotSynopsis;

        private Movie mMovie;

        public DetailFragment() {
        }

        public static DetailFragment newInstance(Movie movie) {

            Bundle args = new Bundle();
            args.putParcelable(EXTRA_MOVIE_OBJECT, movie);

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Get the arguments
            mMovie = getArguments().getParcelable(EXTRA_MOVIE_OBJECT);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

            mPosterImageView = (ImageView) rootView.findViewById(R.id.movie_poster_imageview);
            mOriginalTitleTextView = (TextView) rootView.findViewById(R.id.original_title_textview);
            mUserRatingTextView = (TextView) rootView.findViewById(R.id.user_rating_textview);
            mReleaseDate = (TextView) rootView.findViewById(R.id.release_date_textview);
            mPlotSynopsis = (TextView) rootView.findViewById(R.id.synopsis_textview);

            if (mMovie != null) {
                Picasso.with(getActivity()).load(mMovie.getPosterPath()).into(mPosterImageView);
                mOriginalTitleTextView.setText(mMovie.getOriginalTitle());
                mUserRatingTextView.setText(mMovie.getUserRating() + "");
                mReleaseDate.setText(mMovie.getReleaseDate());
                mPlotSynopsis.setText(mMovie.getSynopsis());
            }

            return rootView;
        }
    }

}
