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

        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(layoutId, parent, false);
            holder = new ViewHolder();
            holder.content     = convertView.findViewById(R.id.post_history_content);
            holder.username    = convertView.findViewById(R.id.post_history_username);
            holder.time        = convertView.findViewById(R.id.post_history_time);
            holder.commentLike = convertView.findViewById(R.id.post_history_comment_like);
            holder.location    = convertView.findViewById(R.id.post_history_location);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.content.setText(post.getContent());

        String usernameText = "@" + post.getUsername();
        holder.username.setText(usernameText);

        holder.time.setText(timeToString(post.getCreation_time()));

        String commentLikeText = commentLikeToString(post);
        holder.commentLike.setText(commentLikeText);

        holder.location.setText(/*latlngToAddress(post.getLocation())*/"");

        return convertView;
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

    private static class ViewHolder {
        TextView content;
        TextView username;
        TextView time;
        TextView commentLike;
        TextView location;
    }
}
