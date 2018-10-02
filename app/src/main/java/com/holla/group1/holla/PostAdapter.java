package com.holla.group1.holla;

import android.content.Context;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.google.android.gms.maps.model.LatLng;
import org.joda.time.DateTime;

import java.io.IOException;
import java.util.List;

public class PostAdapter extends ArrayAdapter<Post> {
    private int layoutId;
    private Context context;

    public PostAdapter(Context context, int layoutId, List<Post> list) {
        super(context, layoutId, list);
        this.layoutId = layoutId;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Post post = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(layoutId, parent, false);

        TextView content = view.findViewById(R.id.post_history_content);
        content.setText(post.getContent());

        TextView username = view.findViewById(R.id.post_history_username);
        String usernameText = "@" + post.getUsername();
        username.setText(usernameText);

        TextView time = view.findViewById(R.id.post_history_time);
        time.setText(timeToString(post.getCreation_time()));

        TextView commentLike = view.findViewById(R.id.post_history_comment_like);
        String commentLikeText = commentLikeToString(post);
        commentLike.setText(commentLikeText);

        TextView location = view.findViewById(R.id.post_history_location);
        location.setText(/*latlngToAddress(post.getLocation())*/"");


        return view;
    }

    private String commentLikeToString(Post post) {
        return post.getNum_comments() + " comments · " + post.getNum_likes() + " likes";
    }

    private String timeToString(DateTime time) {
        int year = time.getYear();
        int month = time.getMonthOfYear();
        int day = time.getDayOfMonth();

        return " · " + year + "/" + month + "/" + day;
    }

}
