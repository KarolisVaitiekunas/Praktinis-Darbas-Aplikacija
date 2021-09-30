package com.example.loginregister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    public TextView nameTextView, btnLogin, btnRegister, btnLogout, btnPosts, btnCreate;
    public String name;
    private Boolean isLogged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        User user = new User(MainActivity.this);

        isLogged = user.checkLogin();

        if(isLogged){
            nameTextView = findViewById(R.id.TextViewMainName);
            nameTextView.setVisibility(View.VISIBLE);
            btnLogout = findViewById(R.id.btnLogout);
            btnLogout.setVisibility(View.VISIBLE);
            btnPosts = findViewById(R.id.btnPosts);
            btnPosts.setVisibility(View.VISIBLE);
            btnCreate = findViewById(R.id.btnCreate);
            btnCreate.setVisibility(View.VISIBLE);

            HashMap<String, String> usersDetails = user.getUsersDetailFromSession();
            name = usersDetails.get(User.KEY_NAME);
            nameTextView.setText("Your name is: " + name);
            return;
        }

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setVisibility(View.VISIBLE);
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setVisibility(View.VISIBLE);


    }

    public void changeActivityLogin(View view){
        startActivity(new Intent(this, Login.class));
    }
    public void changeActivitySignUp(View view){
        startActivity(new Intent(this, SignUp.class));
    }
    public void changeActivityPosts(View view){
        startActivity(new Intent(this, Posts.class));
    }
    public void changeActivityCreate(View view){
        startActivity(new Intent(this, Create.class));
    }
    public void logoutUser(View view) {
        User user = new User(this);
        user.logoutUserSession();
        startActivity(new Intent(new Intent(this, Login.class)));
    }
}