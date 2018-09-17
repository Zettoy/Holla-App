package com.holla.group1.holla;


import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import org.json.JSONArray;

import java.util.List;

public class RestAPIClient {
    public static String TAG = "RestAPIClient";
    public static void loadTweets(Context ctx){
        String url = "https://holla-.herokuapp.com/posts/search/location";
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

    }
    public interface OnTweetsLoadedListener {
        void onTweetsLoaded(List<String> tweets);
    }
}
