package com.asiczen.uidemo;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface VolleyResponse {
    void Volleyerror(VolleyError error);
    void VolleyObjectResponse(JSONObject response);
}
