package com.holla.group1.holla;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ViewPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        List<Comment> testComments = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            TimeZone timeZone = TimeZone.getTimeZone("UTC");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
            dateFormat.setTimeZone(timeZone);
            String isoString = dateFormat.format(new Date());

            Comment testComment = new Comment("Here is some test content, it is really cool ain't it.", "TestBoy", isoString);
            testComments.add(testComment);

        }

        CommentsFragment commentsFragment = (CommentsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_comments);
        commentsFragment.addComments(testComments);
    }

}
