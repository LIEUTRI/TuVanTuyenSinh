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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class LoginActivity extends AppCompatActivity {

    EditText et_email, et_password;
    Button btn_login;
    TextView txt_gotoregister, txt_gotohome;
    Boolean QA;
    String UID;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_email = findViewById(R.id.email);
        et_password = findViewById(R.id.password);
        btn_login = findViewById(R.id.btn_login);
        txt_gotoregister = findViewById(R.id.gotoregister);
        txt_gotohome = findViewById(R.id.gotohome);

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
//        show_password.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (passShown){
//                    et_password.setTransformationMethod(new PasswordTransformationMethod());
//                    passShown = false;
//                } else {
//                    et_password.setTransformationMethod(null);
//                    passShown = true;
//                }
//            }
//        });
    }
}