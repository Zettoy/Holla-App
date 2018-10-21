package com.holla.group1.holla.search.post;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.holla.group1.holla.Config;
import com.holla.group1.holla.post.Post;
import com.holla.group1.holla.post.PostListFragment;

import java.util.ArrayList;
import java.util.List;

public class PostSearchFragment extends PostListFragment {

    private LatLng location = Config.STARTING_LOCATION;

//    @Override
//    public void onResume() {
//        super.onResume();
//
//        // reload posts after returning from viewing one
//        readPostsFromBackend();
//    }


    private String query = "";
    public void search(String query){
        this.query = query;
        show_filtered_posts();
//        this.readPostsFromBackend();
    }
    @Override
    protected void readPostsFromBackend() {

//        getApiClient().searchPostsByContent(this.query);
        getApiClient().getPostsAtLocation(this.location);
    }

    private void show_filtered_posts(){
        List<Post> filtered = new ArrayList<>();
        if(all_posts!=null){

            for(Post post : all_posts){
                if(post.getContent().toLowerCase().contains(this.query.toLowerCase())){
                    filtered.add(post);
                }
            }
        }
        this.getPosts().clear();
        this.getPosts().addAll(filtered);
        this.sortPostsByTime();
        getAdapter().notifyDataSetChanged();
    }
    private List<Post> all_posts;
    @Override
    public void onPostsLoaded(List<Post> posts) {
        if(posts!=null){
            all_posts = posts;
            show_filtered_posts();
        }
        exchangeViewIfNeeded();
        swipeRefreshLayout.setRefreshing(false);
    }


    @Override
    protected String[] onCreateMenuItems() {
        return new String[0];
    }

    @Override
    protected void onMenuOptionItemSelected(int which, Post currentPost) {

    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
        readPostsFromBackend();
    }
}
