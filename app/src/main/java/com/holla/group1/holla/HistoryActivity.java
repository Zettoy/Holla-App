package com.holla.group1.holla;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import com.google.android.gms.maps.model.LatLng;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends Activity {
    private List<Post> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        initList();

        PostAdapter postAdapter = new PostAdapter(
                HistoryActivity.this, R.layout.post_history, list);

        ListView listView = findViewById(R.id.post_history_list);
        listView.setAdapter(postAdapter);
    }

    private void initList() {
        for (int i = 0; i < 10; i ++) {
            Post p = new Post(
                    new LatLng(33.12345, 130.32342),
                    "I wish this works",
                    "Zettoy",
                    new DateTime()
            );
            list.add(p);
        }
    }
}
