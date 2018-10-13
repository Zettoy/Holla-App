package com.holla.group1.holla.notification;

import com.holla.group1.holla.comment.Comment;
import com.holla.group1.holla.post.Post;

public class Notification {
    private String channel;
    private Post post;
    private Comment comment;

    public Notification(String channel, Post post, Comment comment) {
        this.channel = channel;
        this.post    = post;
        this.comment = comment;
    }


    public String getChannel() {
        return channel;
    }

    public Post getPost() {
        return post;
    }

    public Comment getComment() {
        return comment;
    }
}
