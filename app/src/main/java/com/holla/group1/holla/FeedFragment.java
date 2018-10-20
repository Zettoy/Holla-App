package com.holla.group1.holla;

import com.holla.group1.holla.post.Post;
import com.holla.group1.holla.post.PostListFragment;

import java.util.List;

public class FeedFragment extends PostListFragment {

    @Override
    public void onResume() {
        super.onResume();
        readPostsFromBackend();
    }

    @Override
    public void onPostsLoaded(List<Post> posts) {
        getPosts().clear();
        super.onPostsLoaded(posts);
    }

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
