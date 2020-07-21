package com.b1610701.tuvantuyensinh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.b1610701.tuvantuyensinh.Adapter.MessageAdapter;
import com.b1610701.tuvantuyensinh.model.Chat;
import com.b1610701.tuvantuyensinh.model.User;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.Api;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView profile_username;
    ImageButton btn_send;
    EditText et_message;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    MessageAdapter messageAdapter;
    List<Chat> chats;
    RecyclerView recyclerView;

    String UID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        profile_image = findViewById(R.id.profile_image);
        profile_username = findViewById(R.id.profile_username);
        btn_send = findViewById(R.id.btn_send);
        et_message = findViewById(R.id.et_message);

        Intent intent = getIntent();
        UID = intent.getStringExtra("UID");

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = et_message.getText().toString();
                if (!msg.equals("")){
                    SendMessage(firebaseUser.getUid(), UID, msg);
                } else {
                    Toast.makeText(MessageActivity.this, "Type a message!", Toast.LENGTH_SHORT).show();
                }
                et_message.setText("");
            }
        });

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(UID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    profile_username.setText(user.getUsername());
                    if (user.getImageURL().equals("default")){
                        profile_image.setImageResource(R.drawable.user);
                    } else {
                        Glide.with(MessageActivity.this).load(user.getImageURL()).into(profile_image);
                    }
                    readMessage(firebaseUser.getUid(), UID, user.getImageURL());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void SendMessage(String sender, String receiver, String message){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        databaseReference.child("Chats").push().setValue(hashMap);
    }
    private void readMessage(final String myid, final String userid, final String imgURL){
        chats = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chats.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    assert chat != null;
                    if (chat.getReceiver().equals(userid) && chat.getSender().equals(myid) ||
                            chat.getReceiver().equals(myid) && chat.getSender().equals(userid)){
                        chats.add(chat);
                    }
                    messageAdapter = new MessageAdapter(MessageActivity.this, chats, imgURL);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}