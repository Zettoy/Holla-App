package com.holla.group1.holla;


import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class RestAPIClient {
    public static String TAG = "RestAPIClient";
    private Context context;
    private OnTweetsLoadedListener mListener;
    public RestAPIClient(Context ctx, OnTweetsLoadedListener listener) {
        this.context = ctx;
        this.mListener = listener;
    }
    public static void loadTweets(Context ctx){
        String url = "https://holla-alpha.herokuapp.com/posts/search/location";
//        String url = "https://jsonplaceholder.typicode.com/users";
        JsonArrayRequest request = new JsonArrayRequest(
            Request.Method.GET, url, null,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Log.d(TAG, response.toString());

                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, error.toString());

                }
            }
        );
        RequestQueueSingleton.getInstance(ctx).addToRequestQueue(request);
    }

    public void loadFakeTweets(){
        new Thread() {
            @Override
            public void run() {
                try {
                    this.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ArrayList<String> tweets = new ArrayList<String>();
                tweets.add("A");
                tweets.add("B");
                tweets.add("C");
                mListener.onTweetsLoaded(tweets);

            }
        }.start();

    }
    public interface OnTweetsLoadedListener {
        void onTweetsLoaded(List<String> tweets);
    }
}
