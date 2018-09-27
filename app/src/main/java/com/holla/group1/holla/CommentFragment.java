package com.holla.group1.holla;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CommentFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, parent, false);

        // Grab the comment we passed in
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        if (getArguments() != null) {
            String commentString = getArguments().getString("comment");
            if (commentString != null) {
                Comment comment = gson.fromJson(getArguments().getString("comment"), Comment.class);
                TextView messageTxt = (TextView) view.findViewById(R.id.message_txt);
                messageTxt.setText(comment.getContent());
            }
        }

        return view;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
    }

}