package com.holla.group1.holla;

import android.content.Context;
import android.view.View;

import com.holla.group1.holla.post.Post;
import com.holla.group1.holla.post.PostAdapter;
import com.holla.group1.holla.post.PostListFragment;
import com.holla.group1.holla.post.PostLoadTask;
import org.joda.time.DateTime;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FeedFragment extends PostListFragment {

    @Override
    protected void initTask() {
        task = new FeedPostLoadTask(getContext(), posts);
        task.execute();
    }

    private class FeedPostLoadTask extends PostLoadTask {

        public FeedPostLoadTask(Context context, List<Post> posts) {
            super(context, posts);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Collections.sort(posts, new Comparator<Post>() {
                @Override
                public int compare(Post p1, Post p2) {
                    DateTime time1 = p1.getCreation_time();
                    DateTime time2 = p2.getCreation_time();

                    return time2.compareTo(time1);
                }
            });

            PostAdapter postAdapter = new PostAdapter(getContext(), R.layout.post_list_item, posts) {
                @Override
                protected String[] setItems() {
                    return new String[]{"Share"};
                }

                @Override
                protected void onOptionItemSelected(int which) {
                    switch (which) {
                        case 0: // "Share"
                            break;
                    }
                }
            };
            listView.setAdapter(postAdapter);

            if (posts.isEmpty()) {
                listView.setVisibility(View.INVISIBLE);
                getView().findViewById(R.id.post_list_empty).setVisibility(View.VISIBLE);
            }
        }
    }
}
