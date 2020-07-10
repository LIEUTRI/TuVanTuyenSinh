package com.b1610701.tuvantuyensinh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.b1610701.tuvantuyensinh.model.User;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    CircleImageView profile_image;
    TextView profile_username;
    TextView danhmuc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        profile_image = findViewById(R.id.profile_image);
        profile_username = findViewById(R.id.profile_username);
        danhmuc = findViewById(R.id.textViewHeader);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                danhmuc.setVisibility(View.INVISIBLE);
//                User user = snapshot.getValue(User.class);
//                profile_username.setVisibility(View.VISIBLE);
//                assert user != null;
//                profile_username.setText(user.getUsername());
//                if (user.getImageURL().equals("default")){
//                    profile_image.setImageResource(R.drawable.user);
//                } else {
//                    Glide.with(MainActivity.this).load(user.getImageURL()).into(profile_image);
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}