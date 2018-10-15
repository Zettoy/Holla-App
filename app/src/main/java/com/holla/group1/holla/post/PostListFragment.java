package com.holla.group1.holla.post;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.holla.group1.holla.R;
import com.holla.group1.holla.api.RestAPIClient;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class PostListFragment extends Fragment implements
        SwipeRefreshLayout.OnRefreshListener,
        RestAPIClient.OnPostsLoadedListener {
    private List<Post> posts;
    private ListView listView;
    private PostAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private RestAPIClient apiClient;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_list, parent, false);

        apiClient = new RestAPIClient(getContext(), this, null);

        posts = new ArrayList<>();
        readPostsFromBackend();

        listView = view.findViewById(R.id.post_list);

        adapter = new PostAdapter(getContext(), R.layout.post_list_item, posts) {
            @Override
            protected String[] onCreateMenuItems() {
                return PostListFragment.this.onCreateMenuItems();
            }

            @Override
            protected void onMenuOptionItemSelected(int which, Post currentPost) {
                PostListFragment.this.onMenuOptionItemSelected(which, currentPost);
            }
        };
        listView.setAdapter(adapter);

        swipeRefreshLayout = view.findViewById(R.id.post_list_swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);

        return view;
    }

    @Override
    public void onRefresh() {
        posts.clear();
        readPostsFromBackend();
    }

    protected void exchangeViewIfNeeded() {
        if (posts.isEmpty()) {
            listView.setVisibility(View.INVISIBLE);
            getView().findViewById(R.id.post_list_empty).setVisibility(View.VISIBLE);

        } else {
            listView.setVisibility(View.VISIBLE);
            getView().findViewById(R.id.post_list_empty).setVisibility(View.INVISIBLE);
        }
    }

    private void sortPostsByTime() {
        Collections.sort(posts, new Comparator<Post>() {
            @Override
            public int compare(Post p1, Post p2) {
                DateTime time1 = p1.getCreation_time();
                DateTime time2 = p2.getCreation_time();

                return time2.compareTo(time1);
            }
        });
    }

    public List<Post> getPosts() {
        return posts;
    }

    public ListView getListView() {
        return listView;
    }

    public PostAdapter getAdapter() {
        return adapter;
    }

    public RestAPIClient getApiClient() {
        return apiClient;
    }

    protected abstract void readPostsFromBackend();

    protected abstract String[] onCreateMenuItems();

    protected abstract void onMenuOptionItemSelected(int which, Post currentPost);

    @Override
    public void onPostsLoaded(List<Post> posts) {
        this.posts.addAll(posts);

        sortPostsByTime();
        adapter.notifyDataSetChanged();
        exchangeViewIfNeeded();
        swipeRefreshLayout.setRefreshing(false);
    }
}
