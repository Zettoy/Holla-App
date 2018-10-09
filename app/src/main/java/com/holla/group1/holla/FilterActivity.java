package com.holla.group1.holla;

import android.os.Bundle;
import android.app.Activity;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.holla.group1.holla.api.RestAPIClient;
import com.holla.group1.holla.post.Post;

import java.util.ArrayList;
import java.util.List;

public class FilterActivity extends Activity implements RestAPIClient.OnPostsLoadedListener{

    private ArrayList<String> contents = new ArrayList<>();
    private RestAPIClient mClient;
    private SearchView mSearchView;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        mClient = new RestAPIClient(getApplicationContext(), this, null);
        mClient.loadFakeTweets();
        mSearchView = findViewById(R.id.searchView);
        mListView = findViewById(R.id.listView);
        mListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contents));
        mListView.setTextFilterEnabled(true);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)) {
                    mListView.setFilterText(newText);
                } else {
                    mListView.clearTextFilter();
                }
                return false;
            }
        });
    }

    @Override
    public void onPostsLoaded(List<Post> posts) {
        loadContents(posts);
    }

    private void loadContents(List<Post> posts) {
        for (Post p : posts) {
            contents.add(p.getContent());
        }
    }
}
