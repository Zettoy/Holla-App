package com.holla.group1.holla.api;


import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

import com.google.android.gms.maps.model.LatLng;
import com.holla.group1.holla.R;
import com.holla.group1.holla.post.Post;

import org.joda.time.DateTime;
import org.json.JSONArray;

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

    private void parsePostsResponse(JsonArray response){
        ArrayList<Post> posts = new ArrayList<>();
        for (JsonElement jsonElement : response) {
            try{
//                JsonObject post_obj = response.getJsonObject(i);
                JsonObject jsonObject =  jsonElement.getAsJsonObject();
                String timestamp_iso8601 = jsonObject.get("date").getAsString();
                DateTime dateTime = new DateTime(timestamp_iso8601);
                JsonArray coords = jsonObject.get("location").getAsJsonObject().get("coordinates").getAsJsonArray();
                // change this back when backend is fixed to return [latitude, longitude]
//                LatLng loc = new LatLng(
//                        coords.get(0).getAsDouble(),
//                        coords.get(1).getAsDouble()
//                );
                LatLng loc = new LatLng(
                        coords.get(1).getAsDouble(),
                        coords.get(0).getAsDouble()
                );
                String content = jsonObject.get("content").getAsString();
                String username = "default_username";
                if(jsonObject.has("author")){
                    username = jsonObject.get("author").getAsString();
                }
                posts.add(
                        new Post(loc, content,username, dateTime)
                );
            }catch (Exception e){
                Log.e(TAG, e.toString());

            }
        }
        mListener.onPostsLoaded(posts);


    }

    public void getPostsAtLocation(LatLng location, Integer radius_metres) {
        String url = "https://holla-alpha.herokuapp.com/posts/search/location";
        JsonObject request_body = new JsonObject();
        JsonObject location_obj = new JsonObject();
        JsonArray coords = new JsonArray();
        coords.add(location.latitude);
        coords.add(location.longitude);
        location_obj.addProperty("type", "Point");
        location_obj.add("coordinates", coords );
        request_body.add("location", location_obj);
        Log.d(TAG, request_body.toString());
        MyJsonArrayRequest request = new MyJsonArrayRequest(
                Request.Method.POST,
                url,
                request_body.toString(),
                new Response.Listener<JsonArray>() {
                    @Override
                    public void onResponse(JsonArray response) {
                        parsePostsResponse(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }

        );
//        JsonObjectRequest request = new JsonObjectRequest(
//                Request.Method.GET, url, null,
//                new Response.Listener<JsonObject>() {
//                    @Override
//                    public void onResponse(JsonObject response) {
//                        parsePostsResponse(response);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.d(TAG, error.toString());
//
//                    }
//                }
//        );
        RequestQueueSingleton.getInstance(this.context).addToRequestQueue(request);
    }

    public void loadFakeTweets() {
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    //fake network latency
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    String raw_json = readFile(context, R.raw.unsw_6);
//                    JsonObject obj = new JsonObject(raw_json);
//                    JSONArray arr = obj.getJSONArray("posts");
//                    ArrayList<Post> posts = new ArrayList<>();
//                    for (int i = 0; i < arr.length(); i++) {
//                        JsonObject post_obj = arr.getJsonObject(i);
//                        Integer epoch_timestamp = post_obj.getInt("created_at");
//                        DateTime dateTime = new DateTime(epoch_timestamp * 1000L);
//                        Post new_post = new Post(
//                                new LatLng(
//                                        post_obj.getJsonObject("coordinates").getDouble("latitude"),
//                                        post_obj.getJsonObject("coordinates").getDouble("longitude")
//                                ),
//                                post_obj.getString("content"),
//                                post_obj.getString("author"),
//                                dateTime
//                        );
//                        posts.add(new_post);
//                    }
//                    mListener.onPostsLoaded(posts);
//                } catch (Exception e) {
//                    Log.e(TAG, e.toString());
//                }
//
//
//            }
//        };
//        Handler mainHandler = new Handler(this.context.getMainLooper());
//        mainHandler.post(runnable);

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
