package com.holla.group1.holla;

import com.holla.group1.holla.post.Post;
import com.holla.group1.holla.post.PostListFragment;

public class FeedFragment extends PostListFragment {

    @Override
    protected String[] onCreateMenuItems() {
        return new String[]{"Share"};
    }

    @Override
    protected void onMenuOptionItemSelected(int which, Post currentPost) {
        switch (which) {
            case 0: // "Share"
                break;
        }
    }

    @Override
    protected void readPostsFromBackend() {
        getApiClient().getFeedPosts();
    }
}
