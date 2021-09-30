package com.example.loginregister;

public class Post {
    private String title;
    private String body;
    private String postedBy;
    private String id;
    private String color;

    Post(String title, String body, String postedBy, String id, String color){
        this.title = title;
        this.body = body;
        this.postedBy = postedBy;
        this.id = id;
        this.color = color;
    }

    public String getTitle(){
        return title ;
    }

    public String getBody(){
        return body ;
    }

    public String getPostedBy(){
        return postedBy ;
    }

    public String getId(){
        return id ;
    }

    public String getColor(){
        return color ;
    }


}

