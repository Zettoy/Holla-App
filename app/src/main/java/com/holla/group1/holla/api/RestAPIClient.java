package com.holla.group1.holla.api;


import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.holla.group1.holla.R;
import com.holla.group1.holla.comment.Comment;
import com.holla.group1.holla.post.Post;
import com.holla.group1.holla.signin.GoogleAccountSingleton;

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
    private OnCommentSubmittedListener mCommentSubmittedListener;

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
                LatLng loc = new LatLng(
                        coords.get(0).getAsDouble(),
                        coords.get(1).getAsDouble()
                );
                String content = jsonObject.get("content").getAsString();
                String postId = jsonObject.get("id").getAsString();
                String username = "default_username";
                String locationStr = jsonObject.get("location_name").getAsString();
                if (jsonObject.has("author")) {
                    username = jsonObject.get("author").getAsString();
                }

                posts.add(new Post(postId, loc, content, username, dateTime, locationStr));
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
        request_body.addProperty("token", GoogleAccountSingleton.mGoogleSignInAccount.getIdToken());
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
        RequestQueueSingleton.getInstance(this.context).addToRequestQueue(request);
    }

    public void getCommentsFromPostID(String postID) {
        String url = "https://holla-alpha.herokuapp.com/comments/search/post";
        JsonObject request_body = new JsonObject();
        request_body.addProperty("post", postID);
        request_body.addProperty("token", GoogleAccountSingleton.mGoogleSignInAccount.getIdToken());

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

    public void createComment(String postId, String content) {
        String url = "https://holla-alpha.herokuapp.com/comments/create";
        JsonObject request_body = new JsonObject();
        request_body.addProperty("post", postId);
        request_body.addProperty("content", content);
        request_body.addProperty("token", GoogleAccountSingleton.mGoogleSignInAccount.getIdToken());

        Log.d(TAG, request_body.toString());

        MyJsonArrayRequest request = new MyJsonArrayRequest(
                Request.Method.POST,
                url,
                request_body.toString(),
                new Response.Listener<JsonArray>() {
                    @Override
                    public void onResponse(JsonArray response) {
                        // Just assume that like we are good boys and it worked
                        if (mCommentSubmittedListener != null) {
                            mCommentSubmittedListener.onCommentSubmitted();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handling errors OMEGALUL
                    }
                }

        );

        RequestQueueSingleton.getInstance(this.context).addToRequestQueue(request);
    }

    public void createPost(LatLng location, String content) {
        String url = "https://holla-alpha.herokuapp.com/posts/create";
        JsonObject request_body = new JsonObject();

        JsonObject location_obj = new JsonObject();
        JsonArray coords = new JsonArray();
        coords.add(location.latitude);
        coords.add(location.longitude);
        location_obj.addProperty("type", "Point");
        location_obj.add("coordinates", coords);
        request_body.add("location", location_obj);
        request_body.addProperty("content", content);
        request_body.addProperty("token", GoogleAccountSingleton.mGoogleSignInAccount.getIdToken());

        Log.d(TAG, request_body.toString());

        MyJsonArrayRequest request = new MyJsonArrayRequest(
                Request.Method.POST,
                url,
                request_body.toString(),
                new Response.Listener<JsonArray>() {
                    @Override
                    public void onResponse(JsonArray response) {                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handling errors OMEGALUL
                    }
                }
        );

        RequestQueueSingleton.getInstance(this.context).addToRequestQueue(request);
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
                            dateTime,
                            "location"
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

    public void setOnCommentSubmittedListener(OnCommentSubmittedListener onCommentSubmittedListener) {
        this.mCommentSubmittedListener = onCommentSubmittedListener;
    }

    public interface OnPostsLoadedListener {
        void onPostsLoaded(List<Post> posts);
    }

    public interface OnCommentsLoadedListener {
        void onCommentsLoaded(List<Comment> comments);
    }

    public interface OnCommentSubmittedListener {
        void onCommentSubmitted();
    }
}
