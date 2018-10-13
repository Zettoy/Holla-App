package com.holla.group1.holla;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

public class ProfileActivity extends AppCompatActivity {
    private HistoryFragment historyFragment;
    private ListView historyPostListView;
    private String userName;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");
        userID = intent.getStringExtra("userID");

        //Todo: Call backend to see if we need to greyout follow/make it unfollow

        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.activity_history_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        historyFragment = (HistoryFragment) getSupportFragmentManager()
                .findFragmentById(R.id.history_post_list_fragment);
        historyPostListView = historyFragment.getListView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
