package com.udacity.android.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Makina Celestine on 7/22/15.
 */
public class MovieAdapter extends BaseAdapter {
    private final String LOG_TAG = MovieAdapter.class.getSimpleName();

    private Context mContext;
    private ArrayList<String> mMoviePosterUrls;
//    private MovieResults mMovieResults;

    public MovieAdapter(Context context) {
        mContext = context;
//        Collections.addAll(mMoviePosterUrls);
        mMoviePosterUrls = new ArrayList<>();
        //mMovies = MovieResults.getInstance(context).getMovies();
//        mMovieResults = MovieResults.getInstance(context);
    }

    @Override
    public int getCount() {
        return mMoviePosterUrls.size();
        //return mMovies.size();
//        return mMovieResults.size();
    }

    @Override
    public String getItem(int position) {
        return mMoviePosterUrls.get(position);
        //return mMovies.get(position);
//        return mMovieResults.getMovie(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            // if it's not recycled, inflate a new layout
            convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item_layout, null);
            holder = new ViewHolder();

            holder.posterImageView = (ImageView) convertView.findViewById(R.id.posterImageView);

            //imageView.setLayoutParams(new GridView.LayoutParams(100, 100));
            //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //holder.posterImageView = imageView;
            convertView.setTag(holder);
        } else {
            //imageView = (ImageView) convertView;
            holder = (ViewHolder) convertView.getTag();
        }

        // Get poster image
//        String posterPath = getItem(position).getPosterPath();
        String posterPath = getItem(position);

        Picasso.with(mContext).load(posterPath).into(holder.posterImageView);

        //imageView.setImageResource(mThumbIds[position]);
        //holder.posterImageView.setImageResource(mThumbIds[position]);
        //return imageView;
        return convertView;
    }

    public void setGridData(ArrayList<String> urls) {
        mMoviePosterUrls.clear();
        mMoviePosterUrls.addAll(urls);
//        mMovies.clear();
//        mMovies.addAll(movies);
        //mMovies = movies;
//        mMovieResults.replace(movies);
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        ImageView posterImageView;
    }
}
