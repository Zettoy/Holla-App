package com.holla.group1.holla;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import com.holla.group1.holla.notification.Notification;
import com.holla.group1.holla.notification.NotificationAdapter;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NotificationActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private List<Notification> notifications;
    private ListView listView;

    private NotificationLoadTask task;
    private SwipeRefreshLayout swipeRefreshLayout;

    private NotificationAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Toolbar toolbar = findViewById(R.id.activity_notification_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        notifications = new ArrayList<>();

        adapter = new NotificationAdapter(
                NotificationActivity.this, R.layout.content_notification, notifications);

        listView = findViewById(R.id.notification_list);
        listView.setAdapter(adapter);

        swipeRefreshLayout = findViewById(R.id.notification_swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);

        task = new NotificationLoadTask();
        task.execute();
    }


    private void sortNotificationsByTime() {
        Collections.sort(notifications, new Comparator<Notification>() {
            @Override
            public int compare(Notification n1, Notification n2) {
                DateTime time1 = n1.getComment().getCreationTime();
                DateTime time2 = n2.getComment().getCreationTime();

                return time2.compareTo(time1);
            }
        });
    }

    private void exchangeViewIfNeeded() {
        if (notifications.isEmpty()) {
            listView.setVisibility(View.INVISIBLE);
            findViewById(R.id.notification_list_empty).setVisibility(View.VISIBLE);

        } else {
            listView.setVisibility(View.VISIBLE);
            findViewById(R.id.notification_list_empty).setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        task.cancel(true);
    }

    @Override
    public void onRefresh() {
        notifications.clear();
        task = new NotificationLoadTask();
        task.execute();
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

    private class NotificationLoadTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            //TODO: load notifications from backend
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            sortNotificationsByTime();
            adapter.notifyDataSetChanged();
            exchangeViewIfNeeded();
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
