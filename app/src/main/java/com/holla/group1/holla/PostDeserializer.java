package com.holla.group1.holla;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;
import java.text.ParseException;

public class PostDeserializer implements JsonDeserializer<Post> {
    public Post deserialize(JsonElement dateStr, Type typeOfSrc, JsonDeserializationContext context)
    {
        try
        {
            return null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
