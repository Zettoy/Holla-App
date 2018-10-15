package com.holla.group1.holla.user;

public class User {
    public static String CURRENT_USER_ID;

    public String id;
    public String username;
    public User(String id, String username){
        this.id = id;
        this.username = username;
    }
}
