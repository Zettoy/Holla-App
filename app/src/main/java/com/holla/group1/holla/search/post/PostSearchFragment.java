package com.holla.group1.holla.search.post;

import com.holla.group1.holla.post.Post;
import com.holla.group1.holla.post.PostListFragment;

import java.util.List;

public class PostSearchFragment extends PostListFragment {
    @Override
    public void onResume() {
        super.onResume();
        
        // reload posts after returning from viewing one
        readPostsFromBackend();
    }

    @Override
    protected void exchangeViewIfNeeded() {
        //PostListFragment.exchangeViewIfNeeded seems to throw an exception...
//        super.exchangeViewIfNeeded();
    }

    private String query = "";
    public void search(String query){
        this.query = query;
        this.readPostsFromBackend();
    }
    @Override
    protected void readPostsFromBackend() {

        if(this.query.length() > 0) {
            getApiClient().searchPostsByContent(this.query);
        }
    }

    @Override
    public void onPostsLoaded(List<Post> posts) {
        this.getPosts().clear();
        super.onPostsLoaded(posts);
    }

    @Override
    protected String[] onCreateMenuItems() {
        return new String[0];
    }

    @Override
    protected void onMenuOptionItemSelected(int which, Post currentPost) {

    }
}
