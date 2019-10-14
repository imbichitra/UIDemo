package com.asiczen.uidemo;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VolleyJsonObjectRequest {
    private static final String TAG = "VolleyJsonObjectRequest";
    private static VolleyResponse mInterFace;

    public static void jsonObjectRequest(final Context context, String url, JSONObject postparams, int put, VolleyResponse interFace){
        mInterFace = interFace;
        JsonObjectRequest postRequest = new JsonObjectRequest(put, url, postparams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mInterFace.VolleyObjectResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mInterFace.Volleyerror(error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                /*String credentials = "bichi"+":"+"pradhan";
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(),
                        Base64.NO_WRAP);
                headers.put("Authorization", auth);*/
                headers.put("Content-Type","application/json");
                return headers;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,//1 minute
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(context).addToRequestQueue(postRequest);
    }

}
