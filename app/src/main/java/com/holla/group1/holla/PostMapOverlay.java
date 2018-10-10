package com.holla.group1.holla;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.holla.group1.holla.post.Post;
import com.holla.group1.holla.util.Utils;

public class PostMapOverlay extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.post_map_overlay_fragment, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
    }

    public void showPost(Post post) {

        TextView contentTextView = getView().findViewById(R.id.post_content);
        TextView usernameTextView = getView().findViewById(R.id.post_username);
        TextView dateTextView = getView().findViewById(R.id.post_time);
        TextView commentsLikesTextView = getView().findViewById(R.id.post_comment_like);
        TextView locationTextView = getView().findViewById(R.id.post_location);
        contentTextView.setText(post.getContent().trim());
        usernameTextView.setText("@" + post.getUsername());
        dateTextView.setText(" · " + post.get_timestamp_ago());
        commentsLikesTextView.setText(String.format("%d comments · %d likes", post.getNum_comments(), post.getNum_likes()));
        locationTextView.setText(post.getLocationStr());
        Boolean isTruncated = Utils.isTextViewTruncated(contentTextView);
        TextView see_more = getView().findViewById(R.id.see_more);
        if (isTruncated) {
            see_more.setVisibility(View.VISIBLE);
        } else {
            see_more.setVisibility(View.GONE);

        }
    }

}
