package com.b1610701.tuvantuyensinh.model;

public class User {
    private String id;
    private String fullname;
    private String username;
    private String email;
    private String imageURL;
    public User(String id, String fullname, String username, String email, String imageURL){
        this.id = id;
        this.fullname = fullname;
        this.username = username;
        this.email = email;
        this.imageURL = imageURL;
    }
    public User(){}
    public void setId(String id){
        this.id = id;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public void setFullname(String fullname){
        this.fullname = fullname;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setImageURL(String imageURL){
        this.imageURL = imageURL;
    }
    public String getId(){
        return this.id;
    }
    public String getUsername(){
        return this.username;
    }
    public String getFullname(){
        return this.fullname;
    }
    public String getEmail(){
        return this.email;
    }
    public String getImageURL(){
        return this.imageURL;
    }
}
