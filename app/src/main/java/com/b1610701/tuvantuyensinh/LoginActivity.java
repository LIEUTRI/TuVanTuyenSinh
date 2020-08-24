package com.b1610701.tuvantuyensinh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.b1610701.tuvantuyensinh.fragments.UserFragment;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    EditText et_email, et_password;
    Button btn_login;
    TextView txt_gotoregister, txt_gotohome, btn_forgot_password;
    Boolean QA;
    String UID;
    LoginButton loginButton;

    CallbackManager callbackManager;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference reference;

    private static final String EMAIL = "email";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_email = findViewById(R.id.email);
        et_password = findViewById(R.id.password);
        btn_login = findViewById(R.id.btn_login);
        txt_gotoregister = findViewById(R.id.gotoregister);
        txt_gotohome = findViewById(R.id.gotohome);
        btn_forgot_password = findViewById(R.id.forgot_password);

        btn_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });

        try {
            Intent intent;
            intent = getIntent();
            UID = intent.getStringExtra("UID");
            QA = intent.getBooleanExtra("QA", false);
        } catch (Exception e){
            Log.d("GETUEXTRAERROR", ""+e);
        }

        mAuth = FirebaseAuth.getInstance();

        txt_gotoregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        txt_gotohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_login.setEnabled(false);
                final String email = et_email.getText().toString();
                String password = et_password.getText().toString();
                if (TextUtils.isEmpty(email)){
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.enteryouremail), Toast.LENGTH_SHORT).show();
                    btn_login.setEnabled(true);
                } else if (TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.enteryourpassword), Toast.LENGTH_SHORT).show();
                    btn_login.setEnabled(true);
                } else {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        UID = mAuth.getUid();
                                        Intent GotoMainActivity = new Intent(LoginActivity.this, MainActivity.class);
                                        Intent GotoQAActivity = new Intent(LoginActivity.this, QAActivity.class);
                                        if (email.contains("@ctu.edu.vn") || email.contains("@cit.ctu.edu.vn")){
                                            GotoMainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(GotoMainActivity);
                                        } else {
                                            GotoQAActivity.putExtra("UID", UID);
                                            GotoMainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            if (QA){
                                                startActivity(GotoQAActivity);
                                            } else {
                                                startActivity(GotoMainActivity);
                                                finish();
                                            }
                                        }
                                    } else {
                                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.authfailed), Toast.LENGTH_LONG).show();
                                        btn_login.setEnabled(true);
                                    }
                                }
                            });
                }
            }
        });
        et_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                btn_login.setEnabled(true);
            }
        });
        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                btn_login.setEnabled(true);
            }
        });

        /////////////////////////////////////////////////////
        // Initialize Facebook Login button
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.fb_login);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d("TAG", "onError: "+exception.getMessage());
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("TAG", "handleFacebookAccessToken:" + token);
        Toast.makeText(LoginActivity.this, getResources().getString(R.string.pleasewait), Toast.LENGTH_SHORT).show();
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Hello, "+user.getDisplayName(), Toast.LENGTH_SHORT).show();
                            reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", user.getUid());
                            hashMap.put("username", "facebookuser");
                            hashMap.put("email", user.getEmail());
                            hashMap.put("imageURL", user.getPhotoUrl().toString());
                            hashMap.put("fullname", user.getDisplayName());
                            final FirebaseUser u = user;
                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        updateUI(u);
                                    } else {
                                        Toast.makeText(LoginActivity.this, "ERROR!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.\n"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void updateUI(FirebaseUser user) {
        if (user != null){
            Intent GotoMainActivity = new Intent(LoginActivity.this, MainActivity.class);
            GotoMainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(GotoMainActivity);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}