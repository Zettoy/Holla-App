package com.holla.group1.holla.post;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.holla.group1.holla.R;

import java.util.ArrayList;
import java.util.List;

public abstract class PostListFragment extends Fragment {
    protected List<Post> posts;
    protected ListView listView;

    private PostLoadTask task;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_list, parent, false);

        posts = new ArrayList<>();
        listView = view.findViewById(R.id.post_list);
        task = setTask();
        swipeRefreshLayout = view.findViewById(R.id.post_list_swipe_refresh);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //TODO: backend
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        task.execute();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        task.cancel(true);
    }

    protected abstract PostLoadTask setTask();
}
