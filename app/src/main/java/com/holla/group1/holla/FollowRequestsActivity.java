package com.holla.group1.holla;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.holla.group1.holla.R;
import com.holla.group1.holla.api.RestAPIClient;
import com.holla.group1.holla.user.User;

import java.util.ArrayList;
import java.util.List;

public class FollowRequestsActivity extends AppCompatActivity implements RestAPIClient.OnGetFollowRequestsLoadedListener {
    //private TextView text;
    private List<String> listValues;
    private RestAPIClient apiClient;
    private ListView requestList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_requests);

        Toolbar toolbar = findViewById(R.id.activity_history_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        apiClient = new RestAPIClient(this, null, null);
        apiClient.setOnGetFollowRequestsLoadedListener(this);
        apiClient.getFollowRequests(User.CURRENT_USER_ID);
    }

    @Override
    public void OnGetFollowRequestsLoaded(List<Pair<String,String>> users) {
        FollowRequestAdapter adapter = new FollowRequestAdapter(this, (ArrayList<Pair<String, String>>) users);
        requestList = (ListView) findViewById(R.id.request_list);
        requestList.setAdapter(adapter);
        adapter.setApi(apiClient);
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
