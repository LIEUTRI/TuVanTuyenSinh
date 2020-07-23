package com.b1610701.tuvantuyensinh;


import androidx.annotation.NonNull;

import com.b1610701.tuvantuyensinh.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GetUserInfo implements Runnable {
    private String uid;
    private String path;
    private String username;
    private String imageURL;
    DatabaseReference reference;
    public GetUserInfo(){ }
    public GetUserInfo(String path, String username, String imageURL){
        this.path = path;
        this.username = username;
        this.imageURL = imageURL;
    }

    @Override
    public void run() {
        reference = FirebaseDatabase.getInstance().getReference(path);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null && user.getUsername().equals(username)) {
                        uid = user.getId();
                        imageURL = user.getImageURL();
                        MainActivity.adminUid = uid;
                        MainActivity.adminImageUrl = imageURL;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public String getUid(){
        return this.uid;
    }
    public String getImageURL(){
        return this.imageURL;
    }
    public void setPath(String path){
        this.path = path;
    }
    public void setImageURL(String imageURL){
        this.imageURL = imageURL;
    }
    public void setUsername(String username){
        this.username = username;
    }
}
