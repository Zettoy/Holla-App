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
import com.holla.group1.holla.user.User;

import java.util.List;

public class ProfileActivity extends AppCompatActivity implements RestAPIClient.OnGetFollowingLoadedListener, RestAPIClient.OnGetPrivateStatusListener {
    private HistoryFragment historyFragment;
    private ListView historyPostListView;
    private String userName;
    private String userID;
    private boolean loggedInUser;
    private Button followBtn;
    private boolean alreadyFollowing = false;
    private RestAPIClient apiClient;
    private boolean privateAccount = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");
        userID = intent.getStringExtra("userID");
        //loggedInUser = intent.getBooleanExtra("loggedInUser", false);

        TextView usernameTxt = (TextView) findViewById(R.id.usernameTxt);
        if (userName != null) usernameTxt.setText(userName);

        followBtn = (Button) findViewById(R.id.followBtn);
        followBtn.setOnClickListener(new FollowBtnListener());
        apiClient = new RestAPIClient(this, null, null);

        //Todo: Call backend to see if we need to greyout follow/make it unfollow
        if (userID != null && !userID.equals(User.CURRENT_USER_ID)) {
            apiClient.setOnGetFollowingLoadedListener(this);
            apiClient.getFollowingList();
        }

        apiClient.setGetPrivateStatusListener(this);
        apiClient.getPrivateStatus(userID);

        Toolbar toolbar = findViewById(R.id.activity_history_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        historyFragment = (HistoryFragment) getSupportFragmentManager()
                .findFragmentById(R.id.history_post_list_fragment);
        if(historyFragment != null){
            historyFragment.userID = userID;
            historyPostListView = historyFragment.getListView();
            historyFragment.onRefresh();
        }


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

    @Override
    public void OnGetFollowingLoaded(List<String> users) {
        followBtn.setText("Follow");
        alreadyFollowing = false;

        // Linear search over users
        for (String user : users) {
            if (userID.equals(user)) {
                alreadyFollowing = true;
                followBtn.setText("Unfollow");
                break;
            }
        }
        followBtn.setEnabled(true);
    }

    @Override
    public void OnGetPrivateStatus(boolean status) {
        privateAccount = status;

        if (userID.equals(User.CURRENT_USER_ID)) {
            followBtn.setEnabled(true);
            if (status) {
                followBtn.setText("Unprivate");
            } else {
                followBtn.setText("Private");
            }
        }
    }

    private class FollowBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (userID.equals(User.CURRENT_USER_ID)) {
                if (privateAccount) {
                    apiClient.setPrivate(false);
                    followBtn.setText("Private");
                    privateAccount = false;
                } else {
                    apiClient.setPrivate(true);
                    followBtn.setText("Unprivate");
                    privateAccount = true;
                }
            } else {
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
}
