package com.holla.group1.holla;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.holla.group1.holla.post.Post;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private List<Post> posts;
    private ReadPostTask task;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        posts = new ArrayList<>();
        task = new ReadPostTask();
        listView = findViewById(R.id.post_history_list);

        Toolbar toolbar = findViewById(R.id.activity_history_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        task.execute(HistoryActivity.this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        task.cancel(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class ReadPostTask extends AsyncTask<Context, Void, Void> {
        private Context context;

        @Override
        protected Void doInBackground(Context... contexts) {
            context = contexts[0];

            //TODO: backend
            try {
                String raw_json = readFile(context);
                JSONObject obj = new JSONObject(raw_json);
                JSONArray arr = obj.getJSONArray("posts");

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

            } catch (Exception e) {
                Log.e("HistoryActivity", e.toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Collections.sort(posts, new Comparator<Post>() {
                @Override
                public int compare(Post p1, Post p2) {
                    DateTime time1 = p1.getCreation_time();
                    DateTime time2 = p2.getCreation_time();

                    return time2.compareTo(time1);
                }
            });

            PostAdapter postAdapter = new PostAdapter(
                    HistoryActivity.this, R.layout.post_history, posts);

            listView.setAdapter(postAdapter);

            if (posts.isEmpty()) {
                listView.setVisibility(View.INVISIBLE);
                findViewById(R.id.post_history_empty).setVisibility(View.VISIBLE);
            }
        }

        private String readFile(Context context) throws IOException {
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
                Log.e("HistoryActivity", e.toString());
            } finally {
                is.close();
            }
            String jsonString = writer.toString();
            return jsonString;
        }

    }
}
