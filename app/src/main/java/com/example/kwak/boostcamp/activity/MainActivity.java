package com.example.kwak.boostcamp.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.kwak.boostcamp.R;
import com.example.kwak.boostcamp.adapter.MovieAdapter;
import com.example.kwak.boostcamp.presenter.MainContract;
import com.example.kwak.boostcamp.presenter.MainPresenter;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    static final int SEARCH_COUNT = 20;

    private MainPresenter mainPresenter;

    private RecyclerView mRecyclerView;
    private MovieAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private ProgressBar progressBar;
    private Button mSearchBtn;
    private EditText mSearchText;
    private String inputText;

    InputMethodManager imm;
    Boolean isScrolling = false;

    int currentItems, scrolledOutItems, totalItems;
    int curdisplayCnt, curSearchNum;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchBtn = (Button) findViewById(R.id.search_btn);
        mSearchText = (EditText) findViewById(R.id.search_input);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        progressBar = (ProgressBar) findViewById(R.id.progress);

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        mAdapter = new MovieAdapter(this);

        // Presenter 생성
        mainPresenter = new MainPresenter();
        // setView
        mainPresenter.attachView(this);

        mainPresenter.setMovieAdapterModel(mAdapter);
        mainPresenter.setMovieAdapterView(mAdapter);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(mSearchText.getWindowToken(), 0);

                inputText = mSearchText.getText().toString().trim();
                if(inputText.equals("")) {
                    Toast.makeText(getBaseContext(), "영화 제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    mSearchText.setText("");
                }
                else {
                    curSearchNum = 1;
                    curdisplayCnt = 1;

                    mainPresenter.loadItems(MainActivity.this, curSearchNum, curdisplayCnt, inputText);
                }
            }
        });


        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentItems = mLayoutManager.getChildCount();
                scrolledOutItems = mLayoutManager.findFirstVisibleItemPosition();
                totalItems = mLayoutManager.getItemCount();

                if(isScrolling && (currentItems+scrolledOutItems == totalItems) && (curdisplayCnt == SEARCH_COUNT)) {
                    isScrolling = false;
                    progressBar.setVisibility(View.VISIBLE);
                    mainPresenter.loadItems(MainActivity.this, curSearchNum, curdisplayCnt, inputText);
                }
            }
        });

    }

    @Override
    public void loadItemForResult(int curSearchNum, int curdisplayCnt) {
        if(curSearchNum == 0 && curdisplayCnt == 0) {
            Toast.makeText(getApplicationContext(), "\"" + inputText + "\"" + "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
        }
        else {
            progressBar.setVisibility(View.GONE);
        }

        this.curSearchNum = curSearchNum;
        this.curdisplayCnt = curdisplayCnt;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPresenter.detachView();
    }
}