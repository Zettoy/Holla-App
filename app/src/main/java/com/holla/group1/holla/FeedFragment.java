package com.holla.group1.holla;

import android.widget.Toast;
import com.holla.group1.holla.post.Post;
import com.holla.group1.holla.post.PostListFragment;

public class FeedFragment extends PostListFragment {

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
//        readPostsFromBackend();
    }


    @Override
    protected String[] onCreateMenuItems() {
        return new String[]{"Report"};
    }

    @Override
    protected void onMenuOptionItemSelected(int which, Post currentPost) {
        switch (which) {
            case 0: // "Report"
                Toast.makeText(getContext(), "Post has been reported.", Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    protected void readPostsFromBackend() {
        getApiClient().getFeedPosts();
    }
}
