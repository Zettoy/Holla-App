package com.holla.group1.holla;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import com.holla.group1.holla.post.Post;
import com.holla.group1.holla.post.PostListFragment;

public class HistoryFragment extends PostListFragment {

    public String userID = "";

    @Override
    protected String[] onCreateMenuItems() {
        return new String[]{"Delete"};
    }

    @Override
    protected void onMenuOptionItemSelected(int which, final Post currentPost) {
        switch (which) {
            case 0: // "Delete"
                final AlertDialog.Builder confirm = new AlertDialog.Builder(getContext());
                confirm.setTitle("Delete");
                confirm.setMessage("Are you sure you want to delete this post?");
                confirm.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deletePost(currentPost);
                        Toast.makeText(getContext(), "Deleted.", Toast.LENGTH_LONG).show();
                    }
                });
                confirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });
                confirm.setCancelable(true);
                confirm.create().show();
                break;
        }
    }

    @Override
    protected void readPostsFromBackend() {
        //getApiClient().getHistoryPosts();
        if (!userID.equals("")) {
            getApiClient().getPostsFromUserID(userID);
        }
    }

    private void deletePost(Post post) {
        getApiClient().deletePost(post.getId());
        getPosts().remove(post);
        getAdapter().notifyDataSetChanged();
        exchangeViewIfNeeded();
    }
}
