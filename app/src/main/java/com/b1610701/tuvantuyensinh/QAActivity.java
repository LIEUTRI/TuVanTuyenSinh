package com.b1610701.tuvantuyensinh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Contacts;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.b1610701.tuvantuyensinh.Adapter.MessageAdapter;
import com.b1610701.tuvantuyensinh.model.Chat;
import com.b1610701.tuvantuyensinh.model.User;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
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

public class QAActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView profile_fullname;
    ImageButton btn_send;
    EditText et_message;

    String fullname;
    String UID;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    FirebaseAuth mAuth;
    String adminUID = "qqMO8UwdW1YSyIwQrjqroTWZn6s2";

    MessageAdapter messageAdapter;
    List<Chat> chats;
    RecyclerView recyclerView;

    Intent intent;
    boolean isGuest = false;

    @Override
    protected void onStart() {
        super.onStart();

        if (isGuest){

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_a);

        mAuth = FirebaseAuth.getInstance();

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
        profile_fullname = findViewById(R.id.profile_fullname);
        btn_send = findViewById(R.id.btn_send);
        et_message = findViewById(R.id.et_message);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                UID = null;
            } else {
                UID = extras.getString("UID");
            }
        } else {
            UID = (String) savedInstanceState.getSerializable("UID");
        }

        if (UID == null){
            createGuestAccount();
            intent = getIntent();
            fullname = intent.getStringExtra("FULLNAME");
            profile_fullname.setText(fullname);
            profile_image.setImageResource(R.drawable.user);
            isGuest = true;
        } else {
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            reference = FirebaseDatabase.getInstance().getReference("Users").child(UID);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    if (user != null){
                        fullname = user.getFullname();
                        profile_fullname.setText(fullname);
                        if (user.getImageURL().equals("default")){
                            profile_image.setImageResource(R.drawable.user);
                        } else {
                            if (!QAActivity.this.isDestroyed()){
                                Glide.with(QAActivity.this)
                                        .load(user.getImageURL())
                                        .into(profile_image);
                            }
                        }
                        readMessage(UID, adminUID, user.getImageURL());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = et_message.getText().toString();
                if (!msg.equals("")){
                    SendMessage(UID, adminUID, msg);
                } else {
                    Toast.makeText(QAActivity.this, "Type a message!", Toast.LENGTH_SHORT).show();
                }
                et_message.setText("");
            }
        });
    }

    private void createGuestAccount() {
        mAuth.createUserWithEmailAndPassword(MainActivity.guestemail, MainActivity.guestpassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            assert user != null;
                            final String userid = user.getUid();
                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username", MainActivity.guestusername);
                            hashMap.put("email", MainActivity.guestemail);
                            hashMap.put("imageURL", "default");
                            hashMap.put("fullname", fullname);
                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        UID = userid;
                                        Toast.makeText(QAActivity.this, "You're logged in as Guest!", Toast.LENGTH_LONG).show();
                                        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                        assert firebaseUser != null;
                                        UID = firebaseUser.getUid();
                                        reference = FirebaseDatabase.getInstance().getReference("Users").child(UID);
                                        reference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                User user = snapshot.getValue(User.class);
                                                if (user != null){
                                                    profile_fullname.setText(user.getFullname());
                                                    if (user.getImageURL().equals("default")){
                                                        profile_image.setImageResource(R.drawable.user);
                                                    } else {
                                                        Glide.with(QAActivity.this).load(user.getImageURL()).into(profile_image);
                                                    }
                                                    readMessage(UID, adminUID, user.getImageURL());
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    } else {
                                        Toast.makeText(QAActivity.this, "ERROR!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(QAActivity.this, getResources().getString(R.string.account_exist), Toast.LENGTH_SHORT).show();
                        }
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
                if (Objects.requireNonNull(snapshot.getValue(Chat.class)).getMessage() == null){
                    Chat chat = new Chat();
                    chat.setSender(adminUID);
                    chat.setReceiver(userid);
                    chat.setMessage("Chào "+fullname+", em thắc mắc về vấn đề gì?");
                    chats.add(chat);
                    Log.d("TAG", adminUID +", "+myid+" | "+ chat.getMessage());
                }
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Chat chat = dataSnapshot.getValue(Chat.class);

                    if (chat.getReceiver().equals(userid) && chat.getSender().equals(myid) ||
                            chat.getReceiver().equals(myid) && chat.getSender().equals(userid)){
                        chats.add(chat);
                    }
                    messageAdapter = new MessageAdapter(QAActivity.this, chats, imgURL);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getUserID(String email, String password){
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            adminUID = auth.getUid();
                        } else {
                            Toast.makeText(QAActivity.this, getResources().getString(R.string.authfailed), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}