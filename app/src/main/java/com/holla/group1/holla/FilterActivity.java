package com.holla.group1.holla;

import android.os.Bundle;
import android.app.Activity;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

public class FilterActivity extends Activity {

    private String[] tempStrs = {"Free ice cream", "bus delayed", "Society event", "4920 Lecture", "!!! Breaking News !!!"};
    private SearchView mSearchView;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        mSearchView = findViewById(R.id.searchView);
        mListView = findViewById(R.id.listView);
        mListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tempStrs));
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
}
