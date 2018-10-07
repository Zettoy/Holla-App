package com.holla.group1.holla;

import android.app.Activity;
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

public class ViewPostActivity extends AppCompatActivity implements OnLikeListener{
    public static final String BUNDLED_POST_JSON = "post JSON";
    private boolean replaceText = true;

    private void drawHardcodedComments(){
        List<Comment> testComments = new ArrayList<>();
        String[] testHardedMessages =
            {
                "Yo don't mind me just hardcoding some test strings so we can like test it out and stuff. Make sure to check out my mixtrack on soundcloud though, that stuff's spicy yo.",
                "Like seriously don't mind me, this is going to be deleted in about a day.",
                "Woah, this Arc ad is really cool. I think getting a job in 2019 with over 50 options to choose from that suit my timetable is a really good thing. Now if only I could read the rest of the message so I know where to apply.",
                "Sometimes I wish we had more than 3 posts to comment on. There's not really much to say about them that haven't already been said.",
                "Don't forget to rate, comment, favourite and subscribe!",
                "I need another long message to make sure that the comment textviews actually work how you'd expect but I'm running out of things to ramble on about. This is quite the problem I have got myself into.",
                "Why did it take so long to make the comments part? Like seriously it shouldn't have been this long if everything went as expected (which it didn't spoiler alert).",
                "It'd be pretty cool if one day these hardcoded comments were replaced with real comments from the backend.",
            };

        for (int i = 0; i < 10; i++) {
            TimeZone timeZone = TimeZone.getTimeZone("UTC");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
            dateFormat.setTimeZone(timeZone);
            String isoString = dateFormat.format(new Date());

            Comment testComment = new Comment(testHardedMessages[i % testHardedMessages.length], "TestBoy", isoString);
            testComments.add(testComment);
        }

        CommentsFragment commentsFragment = (CommentsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_comments);
        commentsFragment.addComments(testComments);
    }

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
        Post post = Post.fromJSON(post_json);
        drawPost(post);

        LikeButton likeButton = findViewById(R.id.heart_button);
        likeButton.setOnLikeListener(this);

        Button commentSubmitButton = findViewById(R.id.btn_submit);
        commentSubmitButton.setOnClickListener(new CommentSubmitClick());

        EditText commentEditText = findViewById(R.id.edt_comment);
        commentEditText.setOnFocusChangeListener(new CommentEditTextFocusChange());

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        // Hardcoded test comments (please replace me with real comments)
        drawHardcodedComments();
    }


 //   @Override public void onAnimationEnd(LikeButton likeButton) {
 //       Log.d(TAG, "Animation End for %s" + likeButton);
 //   }

    private void submitComment(String commentStr) {
        // TODO: Send to backend
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
    public void liked(LikeButton likeButton) {
        Toast.makeText(this, "Liked!", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void unLiked(LikeButton likeButton) {
        Toast.makeText(this, "Disliked!", Toast.LENGTH_SHORT).show();
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
