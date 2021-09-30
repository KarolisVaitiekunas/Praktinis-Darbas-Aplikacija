package com.example.loginregister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class ResetPassword extends AppCompatActivity {
    private TextView passwordInput;
    private JSONObject body;
    private final String url ="http://keris.ro.lt:3001/auth/resetpassword/";
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();

        token =data.getQueryParameter("token");
        passwordInput = findViewById(R.id.password);
    }

    public void reset(View view){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        //Context
        Context context = getApplicationContext();
        //body
        try {
            body = new JSONObject();
            body.put("password", passwordInput.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url + token, body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), Login.class));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //if server is down
                if(error.networkResponse != null){
                    //if server is not down, but entered wrong values.
                    if(error.networkResponse.statusCode == 401 || error.networkResponse.statusCode == 403 || error.networkResponse.statusCode == 404) {
                        try {
                            String bodyString = new String(error.networkResponse.data,"UTF-8");
                            JSONObject body = new JSONObject(bodyString);
                            String bodyMessage = body.getString("message");

                            Toast.makeText(context, bodyMessage, Toast.LENGTH_SHORT).show();

                            error.printStackTrace();
                        } catch (UnsupportedEncodingException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    Toast.makeText(context, "There has been an error with our servers. Please try again later.", Toast.LENGTH_SHORT).show();
                }
                error.printStackTrace();
            }
        });

        queue.add(jsonObjectRequest);
    }
}
