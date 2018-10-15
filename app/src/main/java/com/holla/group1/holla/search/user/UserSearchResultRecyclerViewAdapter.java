package com.holla.group1.holla.search.user;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.holla.group1.holla.R;
import com.holla.group1.holla.search.user.UserSearchResultFragment.OnListFragmentInteractionListener;
import com.holla.group1.holla.user.User;

import java.util.List;


public class UserSearchResultRecyclerViewAdapter extends RecyclerView.Adapter<UserSearchResultRecyclerViewAdapter.ViewHolder> {

    private List<User> mValues;
    private final OnListFragmentInteractionListener mListener;

    public UserSearchResultRecyclerViewAdapter(List<User> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_locationsearchresult, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
//        holder.mIdView.setText(mValues.get(position).place_id);
        holder.mContentView.setText(mValues.get(position).username);
        holder.mContentView.setTextColor(holder.mView.getResources().getColorStateList(R.color.search_results));


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    public void changeDataList(List<User> items){
        this.mValues = items;
        this.notifyDataSetChanged();

    }
    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public User mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
