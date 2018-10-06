package com.holla.group1.holla.post;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class PostSerializer  implements JsonSerializer<Post> {
    public JsonElement serialize(Post post, Type typeOfSrc, JsonSerializationContext context){
        JsonObject j = new JsonObject();
//        j.addProperty("id", 42);
        j.addProperty("created_at", post.getCreation_time().getMillis() / 1000);
        j.addProperty("content", post.getContent());
        j.addProperty("username", post.getUsername());
        JsonObject location = new JsonObject();
        location.addProperty("latitude", post.getLocation().latitude);
        location.addProperty("longitude", post.getLocation().longitude);
        j.add("coordinates", location);
        return j;
    }
}
