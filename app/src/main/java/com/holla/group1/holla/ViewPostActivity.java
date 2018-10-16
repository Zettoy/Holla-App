package com.holla.group1.holla;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.holla.group1.holla.api.LikePostRequest;
import com.holla.group1.holla.api.RestAPIClient;
import com.holla.group1.holla.comment.Comment;
import com.holla.group1.holla.comment.CommentsFragment;
import com.holla.group1.holla.post.Post;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ViewPostActivity extends AppCompatActivity implements OnLikeListener, LikePostRequest.OnLikeResponseListener, RestAPIClient.OnCommentsLoadedListener, RestAPIClient.OnCommentSubmittedListener, RestAPIClient.OnPostsLoadedListener {
    public static final String BUNDLED_POST_JSON = "post JSON";
    public static final String BUNDLED_POST_ID   = "post ID";
    private boolean replaceText = true;
    private RestAPIClient apiClient;
    private Post post;
    private LikeButton likeButton;
    private void drawPost(Post post){
        getSupportActionBar().setTitle("Post by " + post.getUsername());
        TextView contentTextView = findViewById(R.id.post_content);
        contentTextView.setMaxLines(Integer.MAX_VALUE);
        contentTextView.setEllipsize(null);
        contentTextView.setText(post.getContent());

        TextView usernameTxt = findViewById(R.id.post_username);
        usernameTxt.setText(post.getFormattedUsername());
        TextView timeTxt = findViewById(R.id.post_time);
        timeTxt.setText(post.getFormattedTimestampAgo());
        TextView commentLikeTxt = findViewById(R.id.post_comment_like);
        commentLikeTxt.setText(post.commentLikeToString());
        TextView locationTxt = findViewById(R.id.post_location);
        locationTxt.setText(post.getLocationStr());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_post);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        apiClient = new RestAPIClient(this, this, this);
        apiClient.setOnCommentSubmittedListener(this);

        likeButton = findViewById(R.id.heart_button);
        likeButton.setOnLikeListener(this);

        Bundle extras = getIntent().getExtras();
        String postID = extras.getString(BUNDLED_POST_ID);

        if (postID != null) {
            getSupportActionBar().setTitle("Loading...");

            apiClient.getPostByPostID(postID);
            apiClient.getCommentsFromPostID(postID);

            findViewById(R.id.view_post_activity_main).setVisibility(View.INVISIBLE);
            findViewById(R.id.view_post_activity_mask).setVisibility(View.VISIBLE);

        } else {
            String post_json = extras.getString(BUNDLED_POST_JSON);
            post = Post.fromJSON(post_json);

            apiClient.getCommentsFromPostID(post.getId());

            drawPost(post);

            if(post.has_liked) likeButton.setLiked(true);
        }

        Button commentSubmitButton = findViewById(R.id.btn_submit);
        commentSubmitButton.setOnClickListener(new CommentSubmitClick());

        EditText commentEditText = findViewById(R.id.edt_comment);
        commentEditText.setOnFocusChangeListener(new CommentEditTextFocusChange());
    }

    private void submitComment(String commentStr) {
        apiClient.createComment(post.getId(), commentStr);
        // TODO: Handle errors
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onVoteRequestFailure(Exception ex) {

    }

    @Override
    public void onVoteRequestSuccess(String postID) {

    }

    private void toggleLike(){
        if(post != null){
            LikePostRequest likePostRequest = new LikePostRequest(this);
            likePostRequest.setListener(this);
//            likePostRequest.likePost(post.getId());
            likePostRequest.sendLikeOrUnlikeRequest(post.getId(), !post.has_liked);
            if(post.has_liked){
                post.setNum_likes(post.getNum_likes()-1);
                likeButton.setLiked(false);

            }else{
                post.setNum_likes(post.getNum_likes()+1);
                likeButton.setLiked(true);
            }
            post.has_liked = !post.has_liked;

            drawPost(post);
        }
    }

    @Override
    public void liked(LikeButton likeButton) {
        toggleLike();
        Toast.makeText(this, "Liked!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void unLiked(LikeButton likeButton) {
        toggleLike();
        Toast.makeText(this, "Disliked!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCommentsLoaded(List<Comment> comments) {
        CommentsFragment commentsFragment = (CommentsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_comments);
        commentsFragment.setComments(comments);
    }

    @Override
    public void onCommentSubmitted() {
        Toast.makeText(this, "Comment submitted.", Toast.LENGTH_SHORT).show();

        // Literally just restart the activity cause we're lazy
        /*Intent intent = new Intent(this, ViewPostActivity.class);
        intent.putExtra(ViewPostActivity.BUNDLED_POST_JSON, post.toJSON());
        startActivity(intent);
        finish();*/
        apiClient.getCommentsFromPostID(post.getId());
    }

    @Override
    public void onPostsLoaded(List<Post> posts) {
        if (posts == null) {
            findViewById(R.id.view_post_activity_progressbar).setVisibility(View.INVISIBLE);
            findViewById(R.id.view_post_activity_deleted).setVisibility(View.VISIBLE);
            getSupportActionBar().setTitle("Post Not Found");

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() { finish(); }
            },1000);

        } else {
            post = posts.get(0);
            drawPost(post);
            if (post.has_liked) likeButton.setLiked(true);

            findViewById(R.id.view_post_activity_main).setVisibility(View.VISIBLE);
            findViewById(R.id.view_post_activity_mask).setVisibility(View.INVISIBLE);
        }
    }

    class CommentSubmitClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (!replaceText) {
                Button commentSubmitButton = findViewById(R.id.btn_submit);
                EditText commentEditText = findViewById(R.id.edt_comment);
                submitComment(commentEditText.getText().toString());
                commentEditText.setText("");
                hideKeyboard(v);
                commentEditText.clearFocus();
            }
        }
    }

    class CommentEditTextFocusChange implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                hideKeyboard(v);
            } else if (replaceText) {
                // Clear the text for the first time
                EditText commentEditText = findViewById(R.id.edt_comment);
                commentEditText.setText("");
                replaceText = false;
            }
        }
    }
}
