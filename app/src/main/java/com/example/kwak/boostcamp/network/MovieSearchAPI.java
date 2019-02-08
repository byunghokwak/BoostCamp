package com.example.kwak.boostcamp.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.kwak.boostcamp.presenter.AsyncCallback;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MovieSearchAPI extends AsyncTask<String, Void, String> {

    ProgressDialog progressDialog;
    Context ParentContext;

    static final String naverClientID = "W313G3JJYdkdVUaAPWXf";
    static final String naverClientSecret = "ds3OreMLFv";
    static final String naverMovieURL = "https://openapi.naver.com/v1/search/movie.json?display=20";

    AsyncCallback mAsyncCallback;

    public MovieSearchAPI(int curSearchNum, Context context, AsyncCallback asyncCallback) {
        this.ParentContext = context;
        mAsyncCallback = asyncCallback;
        if(curSearchNum == 1) {
            progressDialog = new ProgressDialog(context);
        }

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(progressDialog != null) {
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("잠시만요..");
            progressDialog.show();
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        if(s != null) {
            mAsyncCallback.deligate(s);
        }

    }

    @Override
    protected String doInBackground(String... args) {
        String movieName = args[0];
        int curSearchNum = Integer.parseInt(args[1]);
        String responseResult = dateFetch(movieName, curSearchNum);

        return responseResult;
    }

    public String dateFetch(String movieName, int curSearchNum) {

        String responseResult = null;

        try {
            String text = URLEncoder.encode(movieName, "UTF-8");
            String query = "&start=" + curSearchNum + "&query=" + text;

            String apiURL = naverMovieURL + query;
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", naverClientID);
            con.setRequestProperty("X-Naver-Client-Secret", naverClientSecret);
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            responseResult = response.toString();
            Log.d("result", responseResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseResult;
    }
}

