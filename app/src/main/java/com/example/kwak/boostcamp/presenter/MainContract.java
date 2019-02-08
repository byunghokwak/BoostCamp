package com.example.kwak.boostcamp.presenter;

import android.app.Activity;
import android.content.Context;

import com.example.kwak.boostcamp.adapter.contract.MovieAdapterContract;

public interface MainContract {

    interface View {
        void loadItemForResult(int curSearchNum, int curdisplayCnt);

    }

    interface Presenter {

        void attachView(View view);

        void detachView();

        void loadItems(Activity activitys, int curSearchNum, int curdisplayCnt, String movieName);

        void setMovieAdapterView(MovieAdapterContract.View adapterView);

        void setMovieAdapterModel(MovieAdapterContract.Model adapterModel);

    }
}
