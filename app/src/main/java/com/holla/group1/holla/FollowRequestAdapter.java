package com.holla.group1.holla;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.holla.group1.holla.api.RestAPIClient;

import java.util.ArrayList;

public class FollowRequestAdapter extends ArrayAdapter<Pair<String, String>> {
    private Pair<String, String> user;
    private RestAPIClient apiClient;

    public FollowRequestAdapter(Context context, ArrayList<Pair<String, String>> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Pair<String, String> user = getItem(position);
        this.user = user;
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_layout_follow_request, parent, false);
        }

        TextView txtUsername = (TextView) convertView.findViewById(R.id.request_username_txt);
        Button btnAccept = (Button) convertView.findViewById(R.id.btn_accept);
        Button btnDecline = (Button) convertView.findViewById(R.id.btn_decline);
        btnAccept.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                remove(getItem(position));
                notifyDataSetChanged();
                
            }
        });
        btnDecline.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                remove(getItem(position));
                notifyDataSetChanged();
            }
        });

        txtUsername.setText(user.first);

        return convertView;
    }

    public void setApi(RestAPIClient apiClient) {
        this.apiClient = apiClient;
    }
}