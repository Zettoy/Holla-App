package com.holla.group1.holla;


import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.maps.model.LatLng;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import android.os.Handler;

public class RestAPIClient {
    public static String TAG = "RestAPIClient";
    private Context context;
    private OnPostsLoadedListener mListener;

    public RestAPIClient(Context ctx, OnPostsLoadedListener listener) {
        this.context = ctx;
        this.mListener = listener;
    }

    public static void loadTweets(Context ctx) {
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

    public void loadFakeTweets(final Context context) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    //fake network latency
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    String raw_json = readFile(context, R.raw.unsw_6);
                    JSONObject obj = new JSONObject(raw_json);
                    JSONArray arr = obj.getJSONArray("posts");
                    ArrayList<Post> posts = new ArrayList<>();
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject post_obj = arr.getJSONObject(i);
                        Integer epoch_timestamp = post_obj.getInt("created_at");
                        DateTime dateTime = new DateTime(epoch_timestamp * 1000L);
                        Post new_post = new Post(
                                new LatLng(
                                        post_obj.getJSONObject("coordinates").getDouble("latitude"),
                                        post_obj.getJSONObject("coordinates").getDouble("longitude")
                                ),
                                post_obj.getString("content"),
                                post_obj.getString("author"),
                                dateTime
                        );
                        posts.add(new_post);
                    }
                    mListener.onPostsLoaded(posts);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }


            }
        };
        Handler mainHandler = new Handler(context.getMainLooper());
        mainHandler.post(runnable);

    }

    private String readFile(Context context, int file_id) throws IOException {
        InputStream is = context.getResources().openRawResource(file_id);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            is.close();
        }
        String jsonString = writer.toString();
        return jsonString;
    }

    public interface OnPostsLoadedListener {
        void onPostsLoaded(List<Post> posts);
    }
}
