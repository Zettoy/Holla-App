package com.holla.group1.holla;

import com.google.android.gms.maps.model.LatLng;

import org.joda.time.DateTime;

public class Post {
    private String content;
    private LatLng location;
    private String username;
    private DateTime creation_time;
    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public Post(String content, LatLng location){
        this.content = content;
        this.location = location;

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
