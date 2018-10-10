package com.holla.group1.holla;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.holla.group1.holla.post.Post;
import com.holla.group1.holla.post.PostAdapter;
import com.holla.group1.holla.post.PostListFragment;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.List;

public class FeedFragment extends PostListFragment {

    @Override
    protected String[] onCreateMenuItems() {
        return new String[]{"Share"};
    }

    @Override
    protected void onMenuOptionItemSelected(int which) {
        switch (which) {
            case 0: // "Share"
                break;
        }
    }

    @Override
    protected void readPostsFromBackend() {
        //TODO: backend
        try {
            String raw_json = readFile();
            JSONObject obj = new JSONObject(raw_json);
            JSONArray arr = obj.getJSONArray("posts");

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
                        post_obj.getString("location_name")
                );
                getPosts().add(new_post);
            }

        } catch (Exception e) {
            Log.e("FeedFragment", e.toString());
        }
    }

    private String readFile() throws IOException {
        Context context = getContext();
        InputStream is = context.getResources().openRawResource(R.raw.unsw_6);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (Exception e) {
            Log.e("FeedFragment", e.toString());
        } finally {
            is.close();
        }
        String jsonString = writer.toString();
        return jsonString;
    }
}
