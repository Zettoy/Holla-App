package com.holla.group1.holla.post;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;
import com.holla.group1.holla.R;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class PostLoadTask extends AsyncTask<Void, Void, Void> {
    protected WeakReference<Context> context;
    protected WeakReference<View> view;
    protected WeakReference<ListView> listView;
    protected List<Post> posts;

    public PostLoadTask(Context context, View view, ListView listView, List<Post> posts) {
        this.context = new WeakReference<>(context);
        this.view = new WeakReference<>(view);
        this.listView = new WeakReference<>(listView);
        this.posts = posts;
    }

    @Override
    protected Void doInBackground(Void... voids) {
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
                        dateTime
                );
                posts.add(new_post);
            }

        } catch (Exception e) {
            Log.e("HistoryActivity", e.toString());
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        View view = this.view.get();
        ListView listView = this.listView.get();

        Collections.sort(posts, new Comparator<Post>() {
            @Override
            public int compare(Post p1, Post p2) {
                DateTime time1 = p1.getCreation_time();
                DateTime time2 = p2.getCreation_time();

                return time2.compareTo(time1);
            }
        });

        if (posts.isEmpty()) {
            listView.setVisibility(View.INVISIBLE);
            view.findViewById(R.id.post_list_empty).setVisibility(View.VISIBLE);

        } else {
            listView.setAdapter(setPostAdapter());
        }
    }

    protected abstract PostAdapter setPostAdapter();

    private String readFile() throws IOException {
        Context context = this.context.get();
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
