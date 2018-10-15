package com.holla.group1.holla.api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.holla.group1.holla.signin.GoogleAccountSingleton;
import com.holla.group1.holla.user.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchUsersRequest {
    private Context context;
    private ResponseListener listener;

    public SearchUsersRequest(Context context) {
        this.context = context;
    }


    public void setListener(ResponseListener listener) {
        this.listener = listener;
    }

    public void getUsersByUsername(String query) {
        String url = RestAPIClient.SERVER_LOCATION + "/users/search/username";

        JSONObject request_body = new JSONObject();

        try {
            request_body.put("token", GoogleAccountSingleton.mGoogleSignInAccount.getIdToken());
            request_body.put("query", query);
//            request_body.put("query", query);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String request_body_str = request_body.toString();
        MyJsonArrayRequest request = new MyJsonArrayRequest(
                Request.Method.POST,
                url,
                request_body_str,
                new Response.Listener<JsonArray>() {
                    @Override
                    public void onResponse(JsonArray response) {
                        //TODO:
                        if (listener != null) {
//                            listener.onSearchUsersResponse();
                            ArrayList<User> users= new ArrayList<>();
                            for(JsonElement element : response){
                                JsonObject jsonObject = element.getAsJsonObject();
                                String user_id = jsonObject.get("id").getAsString();
                                String username = jsonObject.get("username").getAsString();
                                users.add(new User(
                                    user_id,
                                        username
                                ));
                            }
                            listener.onSearchUsersResponse(users);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (listener != null) {
                            listener.onSearchUsersError(new Exception(error.toString()));
                        }
                    }
                }
        );
        RequestQueueSingleton.getInstance(this.context).addToRequestQueue(request);
    }

    public interface ResponseListener {
        void onSearchUsersResponse(List<User> users);
        void onSearchUsersError(Exception ex);
    }
}
