package com.holla.group1.holla;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Toast;

import com.holla.group1.holla.R;
import com.holla.group1.holla.post.Post;
import com.holla.group1.holla.post.PostAdapter;
import com.holla.group1.holla.post.PostListFragment;
import com.holla.group1.holla.post.PostLoadTask;
import org.joda.time.DateTime;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HistoryFragment extends PostListFragment {

    @Override
    protected void initTask() {
        task = new HistoryPostLoadTask(getContext(), posts);
        task.execute();
    }

    private class HistoryPostLoadTask extends PostLoadTask {

        public HistoryPostLoadTask(Context context, List<Post> posts) {
            super(context, posts);
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

            PostAdapter postAdapter = new PostAdapter(getContext(), R.layout.post_list_item, posts) {
                @Override
                protected String[] setItems() {
                    return new String[]{"Share", "Delete"};
                }

                @Override
                protected void onOptionItemSelected(int which) {
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
            };
            listView.setAdapter(postAdapter);

            if (posts.isEmpty()) {
                listView.setVisibility(View.INVISIBLE);
                getView().findViewById(R.id.post_list_empty).setVisibility(View.VISIBLE);
            }
        }
    }
}
