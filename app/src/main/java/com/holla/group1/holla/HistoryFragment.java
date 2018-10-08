package com.holla.group1.holla;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.holla.group1.holla.post.Post;
import com.holla.group1.holla.post.PostAdapter;
import com.holla.group1.holla.post.PostListFragment;
import com.holla.group1.holla.post.PostLoadTask;

import java.util.List;

public class HistoryFragment extends PostListFragment {

    @Override
    protected PostLoadTask setTask() {
        return new HistoryPostLoadTask(getContext(), getView(), listView, posts);
    }

    private static class HistoryPostLoadTask extends PostLoadTask {

        private HistoryPostLoadTask(Context context, View view, ListView listView, List<Post> posts) {
            super(context, view, listView, posts);
        }

        @Override
        protected PostAdapter setPostAdapter() {
            return new PostAdapter(context.get(), R.layout.post_list_item, posts) {
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
        }
    }
}
