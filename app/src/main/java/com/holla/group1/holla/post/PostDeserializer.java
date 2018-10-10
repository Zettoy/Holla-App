package com.holla.group1.holla.post;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.joda.time.DateTime;

import java.lang.reflect.Type;

public class PostDeserializer implements JsonDeserializer<Post> {

    public Post deserialize(JsonElement postJSON, Type typeOfSrc, JsonDeserializationContext context)
    {
        try
        {
            JsonObject obj = postJSON.getAsJsonObject();
            JsonElement loc_elem = obj.get("coordinates");
            Double longitude = loc_elem.getAsJsonObject().get("longitude").getAsDouble();
            Double latitude = loc_elem.getAsJsonObject().get("latitude").getAsDouble();
            LatLng location = new LatLng(latitude, longitude );
            String content = obj.get("content").getAsString();
            String username = obj.get("username").getAsString();
            Long creation_epoch = obj.get("created_at").getAsLong();
            String postId = obj.get("id").getAsString();
            String locationStr = obj.get("locationStr").getAsString();

            return new Post(postId, location, content, username, new DateTime(creation_epoch * 1000L), locationStr);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

}
