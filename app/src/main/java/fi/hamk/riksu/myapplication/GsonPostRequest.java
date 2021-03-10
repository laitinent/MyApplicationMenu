package fi.hamk.riksu.myapplication;

/**
 * Created by tlaitinen on 4.12.2016.
 */

import android.util.Base64;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by tlaitinen on 2.12.2016.
 */

public class GsonPostRequest<T> extends Request<T> {
    private final Gson gson = new Gson();
    private final Class<T> clazz;
    private final Response.Listener<T> listener;
    //JSONObject jsonBody = new JSONObject();
    private String mRequestBody;

    /**
     * Make a POST request and return a parsed object from JSON.
     *
     * @param url URL of the request to make
     * @param clazz Relevant class object, for Gson's reflection
     //* @param headers Map of request headers
     * @param jsonBody JSONObject of request
     */
    public GsonPostRequest(String url, Class<T> clazz, JSONObject jsonBody/*Map<String, String> headers*/,
                           Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        this.clazz = clazz;
        Map<String, String> headers = null;
        this.listener = listener;

//        try
        {
//            jsonBody.put("endDate", "2016-12-31T00:30");
            mRequestBody = jsonBody.toString();
        }
        //catch(JSONException ex){            System.err.println(ex.getMessage());        }
    }

    public void setBody(JSONObject jsonBody)
    {
        mRequestBody = jsonBody.toString();
    }

    @Override
    public byte[] getBody() {
        return mRequestBody == null ? null : mRequestBody.getBytes(StandardCharsets.UTF_8);
    }

    //
    @Override
    public Map<String, String> getHeaders() {
        HashMap<String, String> params = new HashMap<>();
        String creds = String.format("%s:%s","NQFytJqel1hXIaeqsh4D",""); // KÄYTTÄJÄTUNNUS
        String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
        params.put("Authorization", auth);
        return params;
    }
    //
/*
    @Override
    protected Map<String, String> getParams()
    {
        Map<String, String>  params = new HashMap<>();
        //params.put("name", "Alif");
        //params.put("domain", "http://itsalif.info");

        return params;
    }*/

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        String json;
        try {
            json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(
                    gson.fromJson(json, clazz),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException | JsonSyntaxException e) {
            System.err.println(e.getMessage());
            return Response.error(new ParseError(e));
        }
    }
}



