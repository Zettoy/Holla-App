package com.holla.group1.holla;

import com.google.android.gms.maps.model.LatLng;

import org.joda.time.DateTime;

public class Post {
    private String content;
    private LatLng location;
    private String username;
    private DateTime creation_time;
    private Integer num_comments = 0;
    private Integer num_likes = 0;

    public Post(LatLng location, String content, String username, DateTime creation_time) {
        this.content = content;
        this.location = location;
        this.creation_time = creation_time;
        this.username = username;

    }

    public Integer getNum_comments() {

        return num_comments;
    }

    public void setNum_comments(Integer num_comments) {
        this.num_comments = num_comments;
    }

    public Integer getNum_likes() {
        return num_likes;
    }

    public void setNum_likes(Integer num_likes) {
        this.num_likes = num_likes;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String get_timestamp_ago() {
        return DateTimeFormatter.getTimestampAgo(this.creation_time);
    }

    public DateTime getCreation_time() {
        return creation_time;
    }

    public void setCreation_time(DateTime creation_time) {
        this.creation_time = creation_time;
    }
}
