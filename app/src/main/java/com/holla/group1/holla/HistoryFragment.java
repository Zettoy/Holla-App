package com.holla.group1.holla;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.holla.group1.holla.post.Post;
import com.holla.group1.holla.post.PostAdapter;
import com.holla.group1.holla.post.PostListFragment;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.List;

public class HistoryFragment extends PostListFragment {

    @Override
    protected String[] onCreateMenuItems() {
        return new String[]{"Share", "Delete"};
    }

    @Override
    protected void onMenuOptionItemSelected(int which) {
        switch (which) {
            case 0: // "Share"
                break;

            case 1: // "Delete"
                final AlertDialog.Builder confirm = new AlertDialog.Builder(getContext());
                confirm.setTitle("Delete");
                confirm.setMessage("Are you sure you want to delete this post?");
                confirm.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "Yes", Toast.LENGTH_LONG).show();
                        //TODO: Delete post
                    }
                });
                confirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                confirm.setCancelable(true);
                confirm.create().show();
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
                        dateTime
                );
                getPosts().add(new_post);
            }

        } catch (Exception e) {
            Log.e("HistoryFragment", e.toString());
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
            Log.e("HistoryFragment", e.toString());
        } finally {
            is.close();
        }
        String jsonString = writer.toString();
        return jsonString;
    }
}
