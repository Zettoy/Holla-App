package com.holla.group1.holla;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
        TextView textView = view.findViewById(R.id.post_content);
        textView.setText(post.getContent());

        return view;
    }

}
