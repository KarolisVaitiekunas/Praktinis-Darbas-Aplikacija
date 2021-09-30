package com.example.loginregister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
import java.util.regex.Pattern;

public class Create extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public TextView titleInput, bodyInput;
    public Spinner spinner;
    public String title, _body, color;
    public String url = "http://keris.ro.lt:3001/post/create/";
    public String token;
    public HashMap<String, String> usersDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        //SPINNER
        spinner = findViewById(R.id.colors);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.colors, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        //IDFK
        titleInput = findViewById(R.id.title);
        bodyInput = findViewById(R.id.body);

        //AUTH
        User user = new User(Create.this);
        usersDetails = user.getUsersDetailFromSession();
        token = usersDetails.get(User.TOKEN_NAME);
    }

    public boolean validateInput(String title, String body){
        if(title.length() > 26 || title.length() < 4){
            titleInput.setError("Title must have 4 to 26 chars.");
            return false;
        }

        if(title.isEmpty()){
            titleInput.setError("Title cannot be empty");
            return false;
        }

        if(body.length() > 700 || body.length() < 15){
            bodyInput.setError("Title must have 15 to 26 chars.");
            return false;
        }

        if(body.isEmpty()){
            bodyInput.setError("Body must have 4 to 255 chars.");
            return false;
        }

        return true;
    }

    public void create(View view){
        Context context = getApplicationContext();

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        //create json object body.
        JSONObject body = new JSONObject();
        title = titleInput.getText().toString().trim();
        _body =   bodyInput.getText().toString().trim();

        try {
            body.put("title", titleInput.getText().toString().trim());
            body.put("body", bodyInput.getText().toString().trim());
            body.put("color", color);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //if validations fails return function.
        if(!validateInput(title, _body)){
            return;
        }

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url + token, body, new Response.Listener<JSONObject>() {

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
                    if(error.networkResponse.statusCode == 401 || error.networkResponse.statusCode == 403) {
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        color = adapterView.getItemAtPosition(i).toString();

//        Toast.makeText(adapterView.getContext(), color, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
