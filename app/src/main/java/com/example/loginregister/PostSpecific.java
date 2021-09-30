package com.example.loginregister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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
import java.util.HashMap;

public class PostSpecific extends AppCompatActivity {
    public TextView titleTextView, bodyTextView, postedByTextView, idTextView, btnDelete, btnUpdate;
    public Intent intent;
    public String name;

    public String url = "http://keris.ro.lt:3001/post/delete/";
    public String token;
    public HashMap<String, String> usersDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_specific);

        intent = getIntent();

        titleTextView = findViewById(R.id.title);
        bodyTextView = findViewById(R.id.body);
        postedByTextView = findViewById(R.id.postedBy);
        idTextView = findViewById(R.id.id);

        titleTextView.setText(intent.getStringExtra("title"));
        bodyTextView.setText(intent.getStringExtra("body"));
        postedByTextView.setText(intent.getStringExtra("postedBy"));
        idTextView.setText(intent.getStringExtra("id"));

        //AUTH
        User user = new User(PostSpecific.this);
        usersDetails = user.getUsersDetailFromSession();
        token = usersDetails.get(User.TOKEN_NAME);

        btnDelete = findViewById(R.id.btnDelete);
        btnUpdate = findViewById(R.id.btnUpdate);
        name = usersDetails.get(User.KEY_NAME);

        if(!name.equals(intent.getStringExtra("postedBy"))){
            btnDelete.setVisibility(View.INVISIBLE);
            btnUpdate.setVisibility(View.INVISIBLE);
        }

    }

    public void changeActivityUpdate(View view){
        Intent i = new Intent(this, Update.class);
        i.putExtra("title", intent.getStringExtra("title"));
        i.putExtra("body", intent.getStringExtra("body"));
        i.putExtra("id", intent.getStringExtra("id"));
        i.putExtra("color", intent.getStringExtra("color"));
        startActivity(i);
    }

    public void delete(View view){
        Context context = getApplicationContext();

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        //create json object body.
        JSONObject body = new JSONObject();
        String id = intent.getStringExtra("id");

        try {
            body.put("_id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url + id + "/" + token, body, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    String bodyMessage = response.getString("message");
                    Toast.makeText(context, bodyMessage, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
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
                    if(error.networkResponse.statusCode == 401 || error.networkResponse.statusCode == 404) {
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

// Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

}