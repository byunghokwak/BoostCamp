package com.example.kwak.boostcamp.presenter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.kwak.boostcamp.adapter.contract.MovieAdapterContract;
import com.example.kwak.boostcamp.data.MovieItem;
import com.example.kwak.boostcamp.network.MovieSearchAPI;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;

public class MainPresenter implements MainContract.Presenter {

    ArrayList<MovieItem> itemList;

    MainContract.View view;
    MovieAdapterContract.Model adapterModel;
    MovieAdapterContract.View adapterView;

    MovieSearchAPI movieSearchAPI;
    private JsonParser jsonParser;
    private Gson gson;

    int curSearchNum;

    @Override
    public void attachView(MainContract.View view) {
        this.view = view;

        jsonParser = new JsonParser();
        gson = new Gson();
    }

    @Override
    public void detachView() {
        this.view = null;
    }


    @Override
    public void loadItems(Activity activitys, int curSearchNum, int curdisplayCnt, String MovieName) {

        this.curSearchNum = curSearchNum;

        if(curSearchNum == 1) {
            adapterModel.cleanItems();
        }

        movieSearchAPI = new MovieSearchAPI(curSearchNum, activitys, new AsyncCallback() {
            @Override
            public void deligate(String result) {
                ParseToJson(result);

                adapterModel.addItems(itemList);
                adapterView.notifyAdapter();
            }
        });
        movieSearchAPI.execute(MovieName, Integer.toString(curSearchNum));
    }

    @Override
    public void setMovieAdapterView(MovieAdapterContract.View adapterView) {
        this.adapterView = adapterView;
    }

    @Override
    public void setMovieAdapterModel(MovieAdapterContract.Model adapterModel) {
        this.adapterModel = adapterModel;
    }

    private void ParseToJson(String json) {
        JsonObject jsonObject = (JsonObject) jsonParser.parse(json);
        JsonElement displayCntJson = jsonObject.get("display");
        int curdisplayCnt = displayCntJson.getAsInt();
        JsonElement movieInfoJson = jsonObject.get("items");

        MovieItem[] getMovieItems = gson.fromJson(movieInfoJson, MovieItem[].class);

        itemList = new ArrayList<MovieItem>();

        Collections.addAll(itemList, getMovieItems);
//        if (curSearchNum == 1) {
//            // asList로 반환된 list는 add, remove 오류 : UnsupportedOperationException
//
//        }

        curSearchNum += curdisplayCnt;

        // 요청한 View로 아이템 누적 결과 갯수 전달
        view.loadItemForResult(curSearchNum, curdisplayCnt);

    }
}
