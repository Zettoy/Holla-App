package com.holla.group1.holla;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.holla.group1.holla.api.RestAPIClient;

public class ProfileActivity extends AppCompatActivity  {
    private HistoryFragment historyFragment;
    private ListView historyPostListView;
    private String userName;
    private String userID;
    private Button followBtn;
    private boolean alreadyFollowing = false;
    private RestAPIClient apiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");
        userID = intent.getStringExtra("userID");

        TextView usernameTxt = (TextView) findViewById(R.id.usernameTxt);
        if (userName != null) usernameTxt.setText(userName);

        followBtn = (Button) findViewById(R.id.followBtn);
        followBtn.setOnClickListener(new FollowBtnListener());
        apiClient = new RestAPIClient(this, null, null);

        //Todo: Call backend to see if we need to greyout follow/make it unfollow

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

    private class FollowBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (alreadyFollowing) {
                followBtn.setText("Follow");
                apiClient.unfollowUser(userID);
                alreadyFollowing = false;
            } else {
                followBtn.setText("Unfollow");
                apiClient.followUser(userID);
                alreadyFollowing = true;
            }
        }
    }
}
