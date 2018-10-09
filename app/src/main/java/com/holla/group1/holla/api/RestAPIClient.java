package com.holla.group1.holla.api;


import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.holla.group1.holla.R;
import com.holla.group1.holla.comment.Comment;
import com.holla.group1.holla.post.Post;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
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

public class RestAPIClient {
    public static String TAG = "RestAPIClient";
    private Context context;
    private OnPostsLoadedListener mListener;
    private OnCommentsLoadedListener mCommentsListener;

    public RestAPIClient(Context ctx, OnPostsLoadedListener listener, OnCommentsLoadedListener commentsListener) {
        this.context = ctx;
        this.mListener = listener;
        this.mCommentsListener = commentsListener;
    }

    private void parsePostsResponse(JsonArray response) {
        ArrayList<Post> posts = new ArrayList<>();
        for (JsonElement jsonElement : response) {
            try {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
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
                String postId = jsonObject.get("id").getAsString();
                String username = "default_username";
                if (jsonObject.has("author")) {
                    username = jsonObject.get("author").getAsString();
                }
                posts.add(
                        new Post(postId, loc, content, username, dateTime)
                );
            } catch (Exception e) {
                Log.e(TAG, e.toString());

            }
        }
        mListener.onPostsLoaded(posts);


    }

    private void parseCommentsResponse(JsonArray response) {
        ArrayList<Comment> comments = new ArrayList<>();

        for (JsonElement jsonElement : response) {
            try {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                String timestamp_iso8601 = jsonObject.get("date").getAsString();
                String content = jsonObject.get("content").getAsString();
                String username = "default_username";
                if (jsonObject.has("author")) {
                    username = jsonObject.get("author").getAsString();
                }

                comments.add(new Comment(content, username, timestamp_iso8601));
            } catch (Exception e) {
                Log.e(TAG, e.toString());

            }
        }

        mCommentsListener.onCommentsLoaded(comments);
    }


    public void getPostsAtLocation(LatLng location, Integer radius_metres) {
        String url = "https://holla-alpha.herokuapp.com/posts/search/location";
        JsonObject request_body = new JsonObject();
        JsonObject location_obj = new JsonObject();
        JsonArray coords = new JsonArray();
        coords.add(location.latitude);
        coords.add(location.longitude);
        location_obj.addProperty("type", "Point");
        location_obj.add("coordinates", coords);
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

    public void getCommentsFromPostID(String postID) {
        String url = "https://holla-alpha.herokuapp.com/comments/search/post";
        JsonObject request_body = new JsonObject();
        request_body.addProperty("post", postID);

        Log.d(TAG, request_body.toString());

        MyJsonArrayRequest request = new MyJsonArrayRequest(
                Request.Method.POST,
                url,
                request_body.toString(),
                new Response.Listener<JsonArray>() {
                    @Override
                    public void onResponse(JsonArray response) {
                        parseCommentsResponse(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        RequestQueueSingleton.getInstance(this.context).addToRequestQueue(request);
    }

    public void createComment() {
        String url = "https://holla-alpha.herokuapp.com/comments/create";
        JsonObject request_body = new JsonObject();
        request_body.addProperty("post", "5bba12f6053a101f009c7c11");
        request_body.addProperty("user", "5bba12f6053a101f009c7c11");
        request_body.addProperty("content", "What a lovely post. I very much enjoyed it. I especially liked the part with the words and the place it was posted. The time it was made was also very superb.");

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

        //RequestQueueSingleton.getInstance(this.context).addToRequestQueue(request);
    }

    public void loadFakeTweets() {
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
                        Post new_post = new Post("testid",
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
        Handler mainHandler = new Handler(this.context.getMainLooper());
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

    public interface OnCommentsLoadedListener {
        void onCommentsLoaded(List<Comment> comments);
    }
}
