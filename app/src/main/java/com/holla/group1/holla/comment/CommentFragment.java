package com.holla.group1.holla.comment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.holla.group1.holla.R;

public class CommentFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, parent, false);

        // Grab the comment we passed in
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        // Handle nasty people passing in null
        if (getArguments() != null) {
            String commentString = getArguments().getString("comment");
            if (commentString != null) {
                Comment comment = gson.fromJson(getArguments().getString("comment"), Comment.class);

                // Update the stuff in the fragment
                TextView messageTxt = (TextView) view.findViewById(R.id.message_txt);
                messageTxt.setText(comment.getContent());

                TextView usernameTxt = (TextView) view.findViewById(R.id.username_txt);
                usernameTxt.setText("@" + comment.getUsername());

                TextView timeTxt = (TextView) view.findViewById(R.id.time_txt);
                timeTxt.setText(comment.getTimestampAgo());
            }
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}