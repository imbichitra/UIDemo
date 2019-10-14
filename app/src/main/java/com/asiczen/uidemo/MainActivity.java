package com.asiczen.uidemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    String s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            String s = encrypt("My secret message 1234","hello");
            Log.d("MainActivity", "onCreate: "+s);
            Log.d(TAG, "onCreate: "+decrypt(s,"hello"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setKey(View view){
        JSONObject object = new JSONObject();
        try {
            object.put("name","bichi");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = "https://marketzoo.000webhostapp.com/secret.php";
        VolleyJsonObjectRequest.jsonObjectRequest(this, url, object, Request.Method.POST, new VolleyResponse() {
            @Override
            public void Volleyerror(VolleyError error) {
                Log.d(TAG, "Volleyerror: "+error.getMessage());
                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void VolleyObjectResponse(JSONObject response) {
                try {
                    s= response.getString("status");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
            }
        });
    }
 public void getKey(View view){
        JSONObject object = new JSONObject();
        try {
            object.put("sess_id",s);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = "https://marketzoo.000webhostapp.com/getsecret.php";
        VolleyJsonObjectRequest.jsonObjectRequest(this, url, object, Request.Method.POST, new VolleyResponse() {
            @Override
            public void Volleyerror(VolleyError error) {
                Log.d(TAG, "Volleyerror: "+error.getMessage());
                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void VolleyObjectResponse(JSONObject response) {
                Log.d(TAG, "VolleyObjectResponse: "+response.toString());
                try {
                    Toast.makeText(MainActivity.this, response.getString("status"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }



    public static String encrypt(String data,String secretKey)throws Exception{
        SecretKeySpec key = generateKey(secretKey);
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(data.getBytes());
        String encryptedValue = Base64.encodeToString(encVal, Base64.DEFAULT);
        return encryptedValue;
    }

    private static SecretKeySpec generateKey(String password) throws Exception{
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes();
        //byte[] bytes1 = new byte[32];
        digest.update(bytes,0,bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key,"AES");
        return secretKeySpec;
    }
    public static String decrypt(String text,String secretKey) throws Exception{
        SecretKeySpec key = generateKey(secretKey);
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decodeValue = Base64.decode(text,Base64.DEFAULT);
        byte[] decValue = c.doFinal(decodeValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }
}
