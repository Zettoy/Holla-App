package com.holla.group1.holla;

import com.google.android.gms.maps.model.LatLng;

import org.joda.time.DateTime;

public class Post {
    private String content;
    private LatLng location;
    private String username;
    private DateTime creation_time;

    public Post(LatLng location, String content, String username, DateTime creation_time) {
        this.content = content;
        this.location = location;
        this.creation_time = creation_time;
        this.username = username;

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

    public DateTime getCreation_time() {
        return creation_time;
    }

    public void setCreation_time(DateTime creation_time) {
        this.creation_time = creation_time;
    }
}
