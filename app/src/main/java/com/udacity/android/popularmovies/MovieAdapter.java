package com.udacity.android.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends BaseAdapter {
    private final String LOG_TAG = MovieAdapter.class.getSimpleName();

    private Context mContext;
    private ArrayList<String> mMoviePosterUrls;

    public MovieAdapter(Context context) {
        mContext = context;
        mMoviePosterUrls = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mMoviePosterUrls.size();
    }

    @Override
    public String getItem(int position) {
        return mMoviePosterUrls.get(position);
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

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Get poster image
        String posterPath = getItem(position);

        Picasso.with(mContext).load(posterPath).into(holder.posterImageView);

        return convertView;
    }

    public void setGridData(ArrayList<String> urls) {
        mMoviePosterUrls.clear();
        mMoviePosterUrls.addAll(urls);
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        ImageView posterImageView;
    }
}
