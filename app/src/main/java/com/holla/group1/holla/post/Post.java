package com.holla.group1.holla.post;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.holla.group1.holla.util.DateTimeFormatter;

import org.joda.time.DateTime;

public class Post {
    private String id;
    private String content;
    private LatLng location;
    private String username;
    private DateTime creation_time;
    private Integer num_comments = 0;
    private Integer num_likes = 0;
    private String locationStr;

    public Boolean has_liked = false;

    private static GsonBuilder getGSONBuilder() {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Post.class, new PostSerializer());
        gsonBuilder.registerTypeAdapter(Post.class, new PostDeserializer());
        return gsonBuilder;

    }

    public static Post fromJSON(String json) {

        Gson gson = Post.getGSONBuilder().create();
        return gson.fromJson(json, Post.class);
    }

    public String toJSON() {
        Gson gson = Post.getGSONBuilder().create();
        String json = gson.toJson(this);
        return json;
    }

    public Post(String id, LatLng location,
                String content, String username,
                DateTime creation_time, String locationStr){
//                Integer num_likes) {
        this.content = content;
        this.location = location;
        this.creation_time = creation_time;
        this.username = username;
        this.id = id;
        this.locationStr = locationStr;
//        this.num_likes = num_likes;
    }

    public String getId() {
        return id;
    }

    public Integer getNum_comments() {
        return num_comments;
    }

    public void setNum_likes(Integer num_likes) {
        this.num_likes = num_likes;
    }

    public Integer getNum_likes() {
        return num_likes;
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

    public String getLocationStr() {
        return locationStr;
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

    public String commentLikeToString() {
        return getNum_comments() + " comments · " + getNum_likes() + " likes";
    }

    public String getFormattedUsername() {
        return "@" + getUsername();
    }

    public String getFormattedTimestampAgo() {
        return " · " + get_timestamp_ago();
    }
}
