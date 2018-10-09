package com.holla.group1.holla;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

public class ViewPostActivity extends AppCompatActivity implements OnLikeListener, LikePostRequest.OnLikeResponseListener, RestAPIClient.OnCommentsLoadedListener, RestAPIClient.OnCommentSubmittedListener {
    public static final String BUNDLED_POST_JSON = "post JSON";
    private boolean replaceText = true;
    private RestAPIClient apiClient;
    private Post post;

    private void drawPost(Post post){
        getSupportActionBar().setTitle("Post by " + post.getUsername());
        TextView contentTextView = findViewById(R.id.post_content);
        contentTextView.setMaxLines(Integer.MAX_VALUE);
        contentTextView.setEllipsize(null);
        contentTextView.setText(post.getContent());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_post);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        String post_json = extras.getString(BUNDLED_POST_JSON);
        post = Post.fromJSON(post_json);

        apiClient = new RestAPIClient(this, null, this);
        apiClient.setOnCommentSubmittedListener(this);
        apiClient.getCommentsFromPostID(post.getId());

        drawPost(post);

        LikeButton likeButton = findViewById(R.id.heart_button);
        likeButton.setOnLikeListener(this);

        Button commentSubmitButton = findViewById(R.id.btn_submit);
        commentSubmitButton.setOnClickListener(new CommentSubmitClick());

        EditText commentEditText = findViewById(R.id.edt_comment);
        commentEditText.setOnFocusChangeListener(new CommentEditTextFocusChange());
    }

    private void submitComment(String commentStr) {
        apiClient.createComment(post.getId(), "testgirl", commentStr);
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
    public void onLikeRequestFailure(Exception ex) {

    }

    @Override
    public void onLikeRequestSuccess(String postID) {

    }

    @Override
    public void liked(LikeButton likeButton) {
        LikePostRequest likePostRequest = new LikePostRequest(this);
        likePostRequest.setListener(this);
        likePostRequest.likePost("asdf");
        Toast.makeText(this, "Liked!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void unLiked(LikeButton likeButton) {
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
