package com.holla.group1.holla.notification;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.holla.group1.holla.R;
import com.holla.group1.holla.ViewPostActivity;
import com.holla.group1.holla.comment.Comment;
import com.holla.group1.holla.post.Post;

import java.util.List;

public class NotificationAdapter extends ArrayAdapter<Notification> {
    private Context context;
    private int layoutId;
    private List<Notification> notifications;

    public NotificationAdapter(Context context, int layoutId, List<Notification> notifications) {
        super(context, layoutId, notifications);
        this.context = context;
        this.layoutId = layoutId;
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Notification notification = notifications.get(position);

        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(layoutId, parent, false);
            holder = new ViewHolder();
            holder.content  = convertView.findViewById(R.id.notification_content);
            holder.username = convertView.findViewById(R.id.notification_username);
            holder.action   = convertView.findViewById(R.id.notification_action);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Comment comment = notification.getComment();

        holder.content.setText(comment.getContent());
        holder.username.setText(comment.getUsername());
        holder.action.setText(notification.getChannel());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewPostActivity.class);
                intent.putExtra(ViewPostActivity.BUNDLED_POST_JSON, notification.getPost().toJSON());
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView content;
        TextView username;
        TextView action;
    }
}
