package com.holla.group1.holla.comment;

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
import com.holla.group1.holla.R;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.generateViewId;

public class CommentsFragment extends Fragment {

    private List<CommentFragment> commentFragments;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View view = inflater.inflate(R.layout.fragment_comments, parent, false);
        commentFragments = new ArrayList<CommentFragment>();
        return view;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void setComments(List<Comment> comments) {
        FragmentManager fm = getChildFragmentManager();

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        // This is kinda hacky but it's the only thing that would work (thanks stack overflow) without
        // moving all of this logic into the ViewPostActivity file.
        LinearLayout layout = (LinearLayout) getView();

        FragmentTransaction ft = fm.beginTransaction();
        for (CommentFragment commentFragment : commentFragments) {
            ft.remove(commentFragment);
        }
        ft.commit();

        ft = fm.beginTransaction();
        for (Comment comment : comments) {
            CommentFragment commentFragment = new CommentFragment();
            Bundle arguments = new Bundle();
            arguments.putString("comment", gson.toJson(comment));
            commentFragment.setArguments(arguments);
            FrameLayout frame = new FrameLayout(getContext());
            int viewId = generateViewId();
            frame.setId(viewId);
            layout.addView(frame);
            ft.add(viewId, commentFragment);
            commentFragments.add(commentFragment);
        }
        ft.commit();
    }
}