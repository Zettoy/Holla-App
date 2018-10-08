package com.holla.group1.holla.api;

import android.support.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.io.UnsupportedEncodingException;

public class MyJsonArrayRequest extends JsonRequest<JsonArray> {

    public MyJsonArrayRequest(
            int method,
            String url,
            @Nullable String requestString,
            Response.Listener<JsonArray> listener,
            @Nullable Response.ErrorListener errorListener) {
        super(
                method,
                url,
                requestString,
                listener,
                errorListener);
    }
    @Override
    protected Response<JsonArray> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString =
                    new String(
                            response.data,
                            HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
            return Response.success(
                    new JsonParser().parse(jsonString).getAsJsonArray(), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonParseException je) {
            return Response.error(new ParseError(je));
        }
    }
}
