package com.example.loginregister;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Posts extends AppCompatActivity implements RecyclerAdapter.ViewHolder.OnNoteListener {
    private static final String TAG =  "YOUR MESSAGE SIRRR";
    //api
    private final String getPostsUrl = "http://keris.ro.lt:3001/post/getPosts/";
    private final String getPostUrl = "http://keris.ro.lt:3001/post/getPost/";
    RequestQueue queue;
    User user;
    JSONObject body;
    String token;

    //recycler
    private LinearLayoutManager layoutManager;
    private List<Post>postList;
    private RecyclerView.Adapter adapter;

    //search view
     private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        //get inital data
        user = new User(Posts.this);
        queue = Volley.newRequestQueue(this);
        HashMap<String, String> usersDetails = user.getUsersDetailFromSession();
        token = usersDetails.get(User.TOKEN_NAME);
        initData();

        //search bar
        searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String text) {
//                getSearchPosts(text);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getSearchPosts(newText);
                return false;
            }
        });

    }

    private void getSearchPosts(String input){
        postList = new ArrayList<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, getPostUrl + input + "/" + token, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            postList = new ArrayList<>();
                            JSONArray data = response.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject post = data.getJSONObject(i);

                                String title = post.getString("title");
                                String body = post.getString("body");
                                String postedBy = post.getJSONObject("postedBy").getString("username");
                                String id = post.getString("_id");
                                String color = post.getString("color");

                                postList.add(new Post(title, body, postedBy,id, color));
                            }
                            initRecyclerView();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {}
                });
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }



    private void initData() {
        postList = new ArrayList<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, getPostsUrl + token, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray data = response.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject post = data.getJSONObject(i);

                                String title = post.getString("title");
                                String body = post.getString("body");
                                String postedBy = post.getJSONObject("postedBy").getString("username");
                                String id = post.getString("_id");
                                String color = post.getString("color");

                                postList.add(new Post(title, body, postedBy,id,color));
                            }
                            initRecyclerView();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);

    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter= new RecyclerAdapter(postList, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onNoteClick(int position) {
        postList.get(position);
        Intent intent = new Intent(this, PostSpecific.class);
        intent.putExtra("title", postList.get(position).getTitle());
        intent.putExtra("body", postList.get(position).getBody());
        intent.putExtra("postedBy", postList.get(position).getPostedBy());
        intent.putExtra("id", postList.get(position).getId());
        intent.putExtra("color", postList.get(position).getColor());
        startActivity(intent);
    }
}