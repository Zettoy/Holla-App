package com.holla.group1.holla;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import org.joda.time.DateTime;

import java.util.List;

public class PostAdapter extends ArrayAdapter<Post> {
    private int layoutId;

    public PostAdapter(Context context, int layoutId, List<Post> list) {
        super(context, layoutId, list);
        this.layoutId = layoutId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Post post = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(layoutId, parent, false);

        TextView content = view.findViewById(R.id.post_history_content);
        content.setText(post.getContent());

        TextView location = view.findViewById(R.id.post_history_location);
        location.setText(post.getLocation().toString());

        TextView time = view.findViewById(R.id.post_history_time);
        time.setText(timeToString(post.getCreation_time()));

        return view;
    }

    private String timeToString(DateTime time) {
        String month = "";

        switch (time.getMonthOfYear()) {
            case 1:
                month = "Jan";
                break;
            case 2:
                month = "Feb";
                break;
            case 3:
                month = "Mar";
                break;
            case 4:
                month = "Apr";
                break;
            case 5:
                month = "May";
                break;
            case 6:
                month = "June";
                break;
            case 7:
                month = "July";
                break;
            case 8:
                month = "Aug";
                break;
            case 9:
                month = "Sept";
                break;
            case 10:
                month = "Oct";
                break;
            case 11:
                month = "Nov";
                break;
            case 12:
                month = "Dec";
                break;
        }

        return time.getDayOfMonth() + " " + month;
    }
}
