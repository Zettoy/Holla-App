package com.holla.group1.holla.api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class LikePostRequest {
    private Context context;
    private OnLikeResponseListener listener;
    public LikePostRequest(Context context){
        this.context = context;
    }


    public void setListener(OnLikeResponseListener listener){
        this.listener = listener;
    }
    public void likePost(final String postID) {
        String url = "https://reqres.in/api/register";
        JSONObject request_body = new JSONObject();
        try {
//            request_body.put("post_ID", postID);
            request_body.put("email", "lol@gmail.com");
            request_body.put("password", "asdf");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, url, request_body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //TODO:
                        if(listener!=null){
                            listener.onLikeRequestSuccess(postID);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(listener!=null){
                            listener.onLikeRequestFailure(new Exception(error.toString()));
                        }
                    }
                }
        );
        RequestQueueSingleton.getInstance(this.context).addToRequestQueue(request);
    }
    public interface OnLikeResponseListener {
        void onLikeRequestSuccess(String postID);
        void onLikeRequestFailure(Exception ex);
    }
}
