package com.holla.group1.holla;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import com.google.android.gms.maps.model.LatLng;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private List<Post> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        initList();

        PostAdapter postAdapter = new PostAdapter(
                HistoryActivity.this, R.layout.post_history, posts);

        ListView listView = findViewById(R.id.post_history_list);
        listView.setAdapter(postAdapter);

        if (posts.isEmpty()) {
            listView.setVisibility(View.INVISIBLE);
            findViewById(R.id.post_history_empty).setVisibility(View.VISIBLE);
        }
    }

    public void finish(View view) {
        this.finish();
    }

    private void initList() {
        posts = new ArrayList<>();

        String shortContent = "Short post";
        String longContent =
                "Long post Long post Long post " +
                "Long post Long post Long post " +
                "Long post Long post Long post " +
                "Long post Long post Long post " +
                "Long post Long post Long post " +
                "Long post Long post Long post ";

        for (int i = 0; i < 10; i ++) {
            String content = shortContent;
            if (i % 2 == 0) content = longContent;

            Post p = new Post(
                    new LatLng(33.12345, 130.32342),
                    content, "User", new DateTime());

            posts.add(p);
        }

    }
}
