package com.holla.group1.holla.api;


import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
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
import com.holla.group1.holla.notification.Notification;
import com.holla.group1.holla.post.Post;
import com.holla.group1.holla.signin.GoogleAccountSingleton;

import com.holla.group1.holla.user.User;
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
    //private static String SERVER_LOCATION = "https://holla-alpha.herokuapp.com";
    public final static String SERVER_LOCATION = "http://188.166.250.144:3000";
    private Context context;
    private OnPostsLoadedListener mListener;
    private OnCommentsLoadedListener mCommentsListener;
    private OnCommentSubmittedListener mCommentSubmittedListener;
    private OnNotificationsLoadedListener mNotificationsLoadedListener;

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
                if(jsonObject.has("error")){
                    String errorMsg = jsonObject.get("error").getAsString();
                    Log.e(TAG, String.format("Server error: '%s'. Maybe try logging in and out.", errorMsg));
                    return;
                }
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
                Integer score = jsonObject.get("score").getAsInt();
                if (jsonObject.has("author")) {
                    username = jsonObject.get("author").getAsString();
                }
                Post post = new Post(postId, loc, content, username, dateTime, locationStr);
                post.setNum_likes(score);

                Boolean has_liked = jsonObject.get("hasVoted").getAsBoolean();
                post.has_liked = has_liked;
                posts.add(post);
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

    private void parseNotificationResponse(JsonArray response) {
        ArrayList<Notification> notifications = new ArrayList<>();

        for (JsonElement jsonElement : response) {
            try {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                String timestamp_iso8601 = jsonObject.get("date").getAsString();
                String content = jsonObject.get("content").getAsString();
                String from = jsonObject.get("userName").getAsString();
                String postID = jsonObject.get("post").getAsString();

                notifications.add(new Notification(content, timestamp_iso8601, postID, from));
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }

        mNotificationsLoadedListener.onNotificationsLoaded(notifications);
    }

    public void searchPostsByContent(String query) {
        String url = SERVER_LOCATION + "/posts/search/content";
        JsonObject request_body = new JsonObject();
        request_body.addProperty("token", GoogleAccountSingleton.mGoogleSignInAccount.getIdToken());
        request_body.addProperty("query", query);
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
                        Log.e(TAG, error.toString());
                    }
                }

        );
        //remove this once backend search efficiency improves
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        RequestQueueSingleton.getInstance(this.context).addToRequestQueue(request);
    }
    public void getPostsAtLocation(LatLng location, Integer radius_metres) {
        String url = SERVER_LOCATION + "/posts/search/location";
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
        String url = SERVER_LOCATION + "/comments/search/post";
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

    public void getPostsFromUserID(String userID) {
        String url = SERVER_LOCATION + "/posts/search/userid";
        JsonObject request_body = new JsonObject();
        request_body.addProperty("id", userID);
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

    public void createComment(String postId, String content) {
        String url = SERVER_LOCATION + "/comments/create";
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
        String url = SERVER_LOCATION + "/posts/create";
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

    public void getCurrentUserID() {
        String url = SERVER_LOCATION + "/users/search/requestUserID";
        final JsonObject request_body = new JsonObject();

        request_body.addProperty("token", GoogleAccountSingleton.mGoogleSignInAccount.getIdToken());

        Log.d(TAG, request_body.toString());

        MyJsonArrayRequest request = new MyJsonArrayRequest(
                Request.Method.POST,
                url,
                request_body.toString(),
                new Response.Listener<JsonArray>() {
                    @Override
                    public void onResponse(JsonArray response) {
                        try {
                            JsonObject jsonObject = response.get(0).getAsJsonObject();
                            User.CURRENT_USER_ID = jsonObject.get("id").getAsString();

                        } catch (Exception e) {
                            Log.d(TAG, "onResponse: " + e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handling errors OMEGALUL
                        Log.e("getUserID", "onErrorResponse: " + error.toString());
                    }
                }
        );

        RequestQueueSingleton.getInstance(this.context).addToRequestQueue(request);
    }

    public void updateDeviceToken(String token) {
        String url = SERVER_LOCATION + "/users/update/deviceToken";
        JsonObject request_body = new JsonObject();
        request_body.addProperty("deviceToken", token);
        request_body.addProperty("token", GoogleAccountSingleton.mGoogleSignInAccount.getIdToken());

        MyJsonArrayRequest request = new MyJsonArrayRequest(
                Request.Method.POST,
                url,
                request_body.toString(),
                new Response.Listener<JsonArray>() {
                    @Override
                    public void onResponse(JsonArray response) {}
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handling errors OMEGALUL
                        Log.e("updateToken", "onErrorResponse: " + error.toString());
                    }
                }
        );

        RequestQueueSingleton.getInstance(this.context).addToRequestQueue(request);
    }

    public void deletePost(String postID) {
        String url = SERVER_LOCATION + "/posts/delete";
        JsonObject request_body = new JsonObject();
        request_body.addProperty("id", postID);
        request_body.addProperty("token", GoogleAccountSingleton.mGoogleSignInAccount.getIdToken());

        MyJsonArrayRequest request = new MyJsonArrayRequest(
                Request.Method.POST,
                url,
                request_body.toString(),
                new Response.Listener<JsonArray>() {
                    @Override
                    public void onResponse(JsonArray response) {}
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handling errors OMEGALUL
                        Log.e("deletePost", "onErrorResponse: " + error.toString());
                    }
                }
        );

        RequestQueueSingleton.getInstance(this.context).addToRequestQueue(request);
    }

    public void followUser(String userID) {
        String url = SERVER_LOCATION + "/follow/followuser";
        JsonObject request_body = new JsonObject();
        request_body.addProperty("followedUser", userID);
        request_body.addProperty("token", GoogleAccountSingleton.mGoogleSignInAccount.getIdToken());

        MyJsonArrayRequest request = new MyJsonArrayRequest(
                Request.Method.POST,
                url,
                request_body.toString(),
                new Response.Listener<JsonArray>() {
                    @Override
                    public void onResponse(JsonArray response) {}
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

    public void unfollowUser(String userID) {
        String url = SERVER_LOCATION + "/follow/unfollowuser";
        JsonObject request_body = new JsonObject();
        request_body.addProperty("followedUser", userID);
        request_body.addProperty("token", GoogleAccountSingleton.mGoogleSignInAccount.getIdToken());

        MyJsonArrayRequest request = new MyJsonArrayRequest(
                Request.Method.POST,
                url,
                request_body.toString(),
                new Response.Listener<JsonArray>() {
                    @Override
                    public void onResponse(JsonArray response) {}
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

    public void getNotifications() {
        String url = SERVER_LOCATION + "/notifications/search";
        JsonObject request_body = new JsonObject();
        request_body.addProperty("token", GoogleAccountSingleton.mGoogleSignInAccount.getIdToken());

        MyJsonArrayRequest request = new MyJsonArrayRequest(
                Request.Method.POST,
                url,
                request_body.toString(),
                new Response.Listener<JsonArray>() {
                    @Override
                    public void onResponse(JsonArray response) {
                        parseNotificationResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handling errors OMEGALUL
                        Log.e("getNotification", "onErrorResponse: " + error.toString());
                    }
                }
        );

        RequestQueueSingleton.getInstance(this.context).addToRequestQueue(request);
    }

    public void getPostByPostID(String postID) {
        String url = SERVER_LOCATION + "/posts/search/postid";
        JsonObject request_body = new JsonObject();
        request_body.addProperty("id", postID);
        request_body.addProperty("token", GoogleAccountSingleton.mGoogleSignInAccount.getIdToken());

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
                        // Handling errors OMEGALUL
                        Log.e("getNotification", "onErrorResponse: " + error.toString());
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

    public void setOnNotificationLoadedListener(OnNotificationsLoadedListener onNotificationLoadedListener) {
        this.mNotificationsLoadedListener = onNotificationLoadedListener;
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

    public interface OnNotificationsLoadedListener {
        void onNotificationsLoaded(List<Notification> notifications);
    }
}
