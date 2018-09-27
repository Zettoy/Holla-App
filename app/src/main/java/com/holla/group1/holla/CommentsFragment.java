package com.holla.group1.holla;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

public class CommentsFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View view = inflater.inflate(R.layout.fragment_comments, parent, false);
        return view;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get the comments list that was passed in
        /*GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        List<Comment> comments = gson.fromJson(getArguments().getString("comments"), new TypeToken<List<Comment>>(){}.getType());

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        for (Comment comment : comments) {
            CommentFragment commentFragment = new CommentFragment();
            Bundle arguments = new Bundle();
            //Gson commentGson = builder.create();
            arguments.putString("comment", gson.toJson(comment));
            commentFragment.setArguments(arguments);
            ft.add(R.id.comment_frag_container, commentFragment);
        }

        ft.commit();*/



        //Test time (now)
        /*TimeZone timeZone = TimeZone.getTimeZone("UTC");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        dateFormat.setTimeZone(timeZone);
        String isoString = dateFormat.format(new Date());

        Comment testComment = new Comment("Here is some test content, it is really cool ain't it.", "TestBoy", isoString);

        Bundle arguments = new Bundle();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        arguments.putString("comment", gson.toJson(testComment));
        fragTwo.setArguments(arguments);

        ft.add(R.id.comment_frag_container, fragTwo);
        ft.commit();*/
    }

    public void addComments(List<Comment> comments) {
        FragmentManager fm = getChildFragmentManager();

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        // This is kinda hacky but it's the only thing that would work (thanks stack overflow) without
        // moving all of this logic into the ViewPostActivity file.
        LinearLayout layout = (LinearLayout) getView();

        FragmentTransaction ft = fm.beginTransaction();
        int i = 0;
        for (Comment comment : comments) {
            CommentFragment commentFragment = new CommentFragment();
            Bundle arguments = new Bundle();
            arguments.putString("comment", gson.toJson(comment));
            commentFragment.setArguments(arguments);
            FrameLayout frame = new FrameLayout(getContext());
            frame.setId(i);
            layout.addView(frame);
            ft.add(i, commentFragment);
            i++;
        }
        ft.commit();
    }
}