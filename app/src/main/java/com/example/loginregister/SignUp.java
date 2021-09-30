package com.example.loginregister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {
    private TextView usernameInput, emailInput, passwordInput;
    private String username, email,password;
    private final String url ="http://keris.ro.lt:3001/auth/register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //initialize variables.
        usernameInput = findViewById(R.id.username);
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
    }

    public void changeActivitySignIn(View view){ startActivity( new Intent(this, Login.class)); }

    public boolean validateInput(String username, String email, String password){
        //email
        String EMAIL_PATTERN = "^(.+)@(\\S+)$";
        Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);
        //username
        String USERNAME_PATTERN = "^[A-Za-z0-9]\\w{3,32}$";
        Pattern usernamePattern = Pattern.compile(USERNAME_PATTERN);

        if (!emailPattern.matcher(email).matches()) {
            emailInput.setError("Invalid Email address.");
            return false;
        }

        if (!usernamePattern.matcher(username).matches()) {
            usernameInput.setError("Username must have 4 to 32 chars and can not contain special chars.");
            return false;
        }

        if(password.isEmpty()){
            passwordInput.setError("Password cannot be empty");
            return false;
        }

        if(password.length() < 4 || password.length() > 255){
            passwordInput.setError("Password must have 4 to 255 chars.");
            return false;
        }

        return true;
    }


    public void signUp(View view){
        Context context = getApplicationContext();

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        //create json object body.
        JSONObject body = new JSONObject();
         username = usernameInput.getText().toString().trim();
         email =   emailInput.getText().toString().trim();
         password = passwordInput.getText().toString().trim();
        try {
            body.put("username", username);
            body.put("email", email);
            body.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //if validations fails return function.
        if(!validateInput(username,email,password)){
            return;
        }

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, body, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    Toast toast = Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT);
                    toast.show();
                    startActivity(new Intent(getApplicationContext(), Login.class));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //if server is down.
                if(error.networkResponse != null){
                    //if server is not down, but entered wrong values.
                    if(error.networkResponse.statusCode == 403) {
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

        // Add the request to the RequestQueue (send it).
        queue.add(jsonObjectRequest);
    }
}



















