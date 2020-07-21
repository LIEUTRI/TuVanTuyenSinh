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
    DatabaseReference reference;
    public GetUserInfo(){ }
    public GetUserInfo(String path, String username){
        this.path = path;
        this.username = username;
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
                        MainActivity.adminUid = uid;
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
    public void setPath(String path){
        this.path = path;
    }
    public void setUsername(String username){
        this.username = username;
    }
}
