package com.holla.group1.holla;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import com.holla.group1.holla.api.RestAPIClient;
import com.holla.group1.holla.post.Post;
import com.holla.group1.holla.post.PostListFragment;

import java.util.List;

public class HistoryFragment extends PostListFragment implements RestAPIClient.OnPostsLoadedListener {

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
        RestAPIClient apiClient = new RestAPIClient(getContext(), this, null);
        apiClient.getPostsFromUserID(/* TODO: userid */"");
    }

    @Override
    public void onPostsLoaded(List<Post> posts) {
        getPosts().addAll(posts);
    }
}
