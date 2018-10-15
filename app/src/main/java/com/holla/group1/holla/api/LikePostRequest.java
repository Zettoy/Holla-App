package com.holla.group1.holla.api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.holla.group1.holla.signin.GoogleAccountSingleton;

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

    public void sendLikeOrUnlikeRequest(final String postID, Boolean wantsVote) {
        String url = "https://holla-alpha.herokuapp.com/posts/vote";

        JSONObject request_body = new JSONObject();

        try {
            request_body.put("token", GoogleAccountSingleton.mGoogleSignInAccount.getIdToken());
            request_body.put("id", postID);
            request_body.put("wantsVote", wantsVote);

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
                            listener.onVoteRequestSuccess(postID);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(listener!=null){
                            listener.onVoteRequestFailure(new Exception(error.toString()));
                        }
                    }
                }
        );
        RequestQueueSingleton.getInstance(this.context).addToRequestQueue(request);
    }

    public interface OnLikeResponseListener {
        void onVoteRequestSuccess(String postID);
        void onVoteRequestFailure(Exception ex);
    }
}
