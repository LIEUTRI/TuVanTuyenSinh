package com.b1610701.tuvantuyensinh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    EditText et_fullname, et_username, et_password, et_re_password, et_email;
    Button btn_register;
    TextView txt_backtologin;

    private FirebaseAuth mAuth;
    private DatabaseReference reference;

    String TAG = "firebase";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_fullname = findViewById(R.id.fullname);
        et_username = findViewById(R.id.username);
        et_email    = findViewById(R.id.email);
        et_password = findViewById(R.id.password);
        et_re_password  = findViewById(R.id.re_password);
        btn_register    = findViewById(R.id.btn_register);
        txt_backtologin = findViewById(R.id.backtologin);

        mAuth = FirebaseAuth.getInstance();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullname = et_fullname.getText().toString();
                String username = et_username.getText().toString();
                String email = et_email.getText().toString();
                String password = et_password.getText().toString();
                String re_password = et_re_password.getText().toString();

                if (TextUtils.isEmpty(fullname)){
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.enteryourfullname), Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(username)){
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.enteryourusername), Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(email)){
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.enteryouremail), Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password)){
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.enteryourpassword), Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(re_password)){
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.enterpasswordagain), Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6){
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.passwordtooshort), Toast.LENGTH_SHORT).show();
                } else if (!password.equals(re_password)){
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.passwordnotmatch), Toast.LENGTH_SHORT).show();
                } else {
                    register(fullname, username, email, password);
                }
            }
        });
        txt_backtologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void register(final String fullname, final String username, final String email, String password){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            assert user != null;
                            String userid = user.getUid();
                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username", username);
                            hashMap.put("email", email);
                            hashMap.put("imageURL", "default");
                            hashMap.put("fullname", fullname);
                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "ERROR!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            if (Objects.equals(Objects.requireNonNull(task.getException()).getMessage(), "The email address is already in use by another account.")){
                                Toast.makeText(RegisterActivity.this, getResources().getString(R.string.account_exist), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Đăng ký thất bại!\nERROR: "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser currentUser) {
        FirebaseAuth.getInstance().signOut();
    }
}