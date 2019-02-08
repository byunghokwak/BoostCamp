package com.example.kwak.boostcamp.adapter;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.kwak.boostcamp.R;
import com.example.kwak.boostcamp.activity.MovieLinkActivity;
import com.example.kwak.boostcamp.adapter.contract.MovieAdapterContract;
import com.example.kwak.boostcamp.data.MovieItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> implements MovieAdapterContract.Model, MovieAdapterContract.View {

    private ArrayList<MovieItem> mMovieSet;
    private Context context;

    ActivityManager activityManager;
    ActivityManager.MemoryInfo memoryInfo;

    public MovieAdapter(Context context) {
        this.context = context;
        mMovieSet = new ArrayList<MovieItem>();
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_view, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        activityManager = (ActivityManager) parent.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        memoryInfo = new ActivityManager.MemoryInfo();

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        String image = mMovieSet.get(position).getImage();
        String title = mMovieSet.get(position).getTitle();
        String actor = mMovieSet.get(position).getActor();
        String director = mMovieSet.get(position).getDirector();
        String date = mMovieSet.get(position).getPubDate().toString();

        if(!image.equals("")) {
            Picasso.with(context).load(image).into(holder.mImage);

            activityManager.getMemoryInfo(memoryInfo);

            int totalMem = (int) memoryInfo.totalMem/(1024*1024);
            int availMem = (int) memoryInfo.availMem/(1024*1024);
            boolean isLowMemory = memoryInfo.lowMemory;

            Log.d("CommentAdapter", "totalMem : " + totalMem);
            Log.d("CommentAdapter", "availMem : " + availMem);
            Log.d("CommentAdapter", "isLowMemory : " + isLowMemory);
        }

        if(!title.equals("")) {
            holder.mTitle.setText(Html.fromHtml(title));
        }

        if(!actor.equals("")) {
            holder.mActor.setText(actor);
        }

        if(!director.equals("")) {
            holder.mDirector.setText(mMovieSet.get(position).getDirector());
        }

        if(!date.equals("")) {
            holder.mDate.setText(date);
        }

        holder.mRating.setRating(mMovieSet.get(position).getUserRating()/2);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MovieLinkActivity.class);
                intent.putExtra("link", mMovieSet.get(position).getLink());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMovieSet.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mImage;
        TextView mTitle;
        RatingBar mRating;
        TextView mDate;
        TextView mDirector;
        TextView mActor;

        View mView;

        public ViewHolder(View itemView) {

            super(itemView);

            mView = itemView;

            mImage = (ImageView)itemView.findViewById(R.id.movie_image);
            mImage.setScaleType(ImageView.ScaleType.FIT_START);

            mTitle = (TextView)itemView.findViewById(R.id.movie_title);
            mRating = (RatingBar) itemView.findViewById(R.id.movie_rating);
            mDate = (TextView) itemView.findViewById(R.id.movie_date);
            mDirector = (TextView) itemView.findViewById(R.id.movie_director);
            mActor = (TextView) itemView.findViewById(R.id.movie_actor);

        }
    }


    // MovieAdapterContract.View
    @Override
    public void notifyAdapter() {
        notifyDataSetChanged();
    }


    // MovieAdapterContract.Model
    @Override
    public void cleanItems() {
        if(mMovieSet != null) {
            mMovieSet.clear();
        }
    }

    @Override
    public void addItems(ArrayList<MovieItem> itemList) {

        for (int i = 0; i < itemList.size(); i++) {
            mMovieSet.add(itemList.get(i));
        }
    }
}
