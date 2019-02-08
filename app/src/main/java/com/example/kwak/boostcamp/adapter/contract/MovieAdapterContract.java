package com.example.kwak.boostcamp.adapter.contract;

import com.example.kwak.boostcamp.data.MovieItem;

import java.util.ArrayList;

public interface MovieAdapterContract {

    interface View {
        void notifyAdapter();
    }

    interface Model{

        void cleanItems();

        void addItems(ArrayList<MovieItem> itemList);

    }
}
