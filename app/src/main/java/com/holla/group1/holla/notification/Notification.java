package com.holla.group1.holla.notification;

import com.holla.group1.holla.comment.Comment;
import com.holla.group1.holla.post.Post;
import com.holla.group1.holla.user.User;
import com.holla.group1.holla.util.DateTimeFormatter;
import org.joda.time.DateTime;

public class Notification {
    //private String channel;
    private String content;
    private String creationTime;
    private String postID;
    private String from;

//    public Notification(String channel, String content, String creationTime, String postID, User from) {
//        this.channel = channel;
//        this.content = content;
//        this.creationTime = creationTime;
//        this.postID = postID;
//        this.from = from;
//    }

    public Notification(String content, String creationTime, String postID, String from) {
        this.content = content;
        this.creationTime = creationTime;
        this.postID = postID;
        this.from = from;
    }

//    public String getChannel() {
//        return channel;
//    }

    public String getContent() {
        return content;
    }

    public DateTime getCreationTime() {
        return new DateTime(creationTime);
    }

    public String getPostID() {
        return postID;
    }

    public String getFrom() {
        return from;
    }

    public String getTimestampAgo() {
        return DateTimeFormatter.getTimestampAgo(getCreationTime());
    }
}
