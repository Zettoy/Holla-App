package com.holla.group1.holla;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;
import com.holla.group1.holla.post.Post;
import com.holla.group1.holla.post.PostAdapter;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FeedFragment extends Fragment {
    private List<Post> posts;
    private ReadPostTask task;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, parent, false);

        posts = new ArrayList<>();
        task = new ReadPostTask();
        listView = view.findViewById(R.id.feed_list);

        task.execute(getContext());

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        task.cancel(true);
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

            PostAdapter postAdapter = new FeedPostAdapter(
                    context, R.layout.post_in_list, posts);

            listView.setAdapter(postAdapter);

            if (posts.isEmpty()) {
                listView.setVisibility(View.INVISIBLE);
                getView().findViewById(R.id.feed_empty).setVisibility(View.VISIBLE);
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
                Log.e("FeedFragment", e.toString());
            } finally {
                is.close();
            }
            String jsonString = writer.toString();
            return jsonString;
        }

        private class FeedPostAdapter extends PostAdapter {

            public FeedPostAdapter(Context context, int layoutId, List<Post> posts) {
                super(context, layoutId, posts);
            }

            @Override
            protected void showDialog() {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                String[] items = {"Share"}; // Maybe add more features later
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: // "Share"
                                break;
                        }
                    }
                });
                builder.setCancelable(true);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }
}
