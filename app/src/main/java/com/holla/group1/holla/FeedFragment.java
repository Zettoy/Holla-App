package com.holla.group1.holla;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

import com.holla.group1.holla.post.Post;
import com.holla.group1.holla.post.PostAdapter;
import com.holla.group1.holla.post.PostListFragment;
import com.holla.group1.holla.post.PostLoadTask;

import java.util.List;

public class FeedFragment extends PostListFragment {

    @Override
    protected PostLoadTask setTask() {
        return new FeedPostLoadTask(getContext(), getView(), listView, posts);
    }

    private static class FeedPostLoadTask extends PostLoadTask {

        private FeedPostLoadTask(Context context, View view, ListView listView, List<Post> posts) {
            super(context, view, listView, posts);
        }

        @Override
        protected PostAdapter setPostAdapter() {
            return new PostAdapter(context.get(), R.layout.post_list_item, posts) {
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
        }
    }
}
