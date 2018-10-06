package com.holla.group1.holla;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.*;
import android.widget.*;

import com.holla.group1.holla.post.Post;

import org.joda.time.DateTime;
import java.util.List;

public class PostAdapter extends ArrayAdapter<Post> {
    private int layoutId;
    private Context context;
    private List<Post> posts;

    public PostAdapter(Context context, int layoutId, List<Post> posts) {
        super(context, layoutId, posts);
        this.layoutId = layoutId;
        this.context = context;
        this.posts = posts;
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
            holder.menuButton  = convertView.findViewById(R.id.post_history_menu_button);
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

        holder.menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        final int id = position;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewPostActivity.class);
                intent.putExtra(
                        ViewPostActivity.BUNDLED_POST_JSON, posts.get(id).toJSON());
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        String[] items = {"Share", "Delete"}; // Maybe add more features later
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // "Share"
                        break;

                    case 1: // "Delete"
                        final AlertDialog.Builder confirm = new AlertDialog.Builder(context);
                        confirm.setTitle("Delete");
                        confirm.setMessage("Are you sure you want to delete this post?");
                        confirm.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(context, "Yes", Toast.LENGTH_LONG).show();
                                //TODO: Delete post
                            }
                        });
                        confirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        confirm.setCancelable(true);
                        confirm.create().show();
                        break;
                }
            }
        });
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
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
        ImageButton menuButton;
    }
}
