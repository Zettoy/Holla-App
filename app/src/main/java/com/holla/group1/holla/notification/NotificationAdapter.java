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
            holder.content = convertView.findViewById(R.id.notification_content);
            holder.from    = convertView.findViewById(R.id.notification_username);
            holder.action  = convertView.findViewById(R.id.notification_action);
            holder.time    = convertView.findViewById(R.id.notification_time);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.content.setText(notification.getContent());
        holder.from.setText("@" + notification.getFrom());
        holder.action.setText(" commented your post:");
        holder.time.setText(notification.getTimestampAgo());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewPostActivity.class);
                intent.putExtra(ViewPostActivity.BUNDLED_POST_ID, notification.getPostID());
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView content;
        TextView from;
        TextView action;
        TextView time;
    }
}
