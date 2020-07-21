package com.b1610701.tuvantuyensinh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ResetPasswordActivity extends AppCompatActivity {

    TextView txt_email;
    Button btn_reset;

    FirebaseAuth firebaseAuth;

    private ProgressDialog progDailog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        txt_email = findViewById(R.id.et_email);
        btn_reset = findViewById(R.id.btn_reset);

        firebaseAuth = FirebaseAuth.getInstance();

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txt_email.getText().toString();
                if (email.equals("")){
                    Toast.makeText(ResetPasswordActivity.this, getResources().getString(R.string.enteryouremail), Toast.LENGTH_SHORT).show();
                } else {
                    progDailog = ProgressDialog.show(ResetPasswordActivity.this, "Reset your password",getResources().getString(R.string.pleasewait), true);
                    progDailog.setCancelable(false);
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(ResetPasswordActivity.this, getResources().getString(R.string.checkyouremail), Toast.LENGTH_LONG).show();
                                startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
                            } else {
                                String error = Objects.requireNonNull(task.getException()).getMessage();
                                Toast.makeText(ResetPasswordActivity.this, error, Toast.LENGTH_LONG).show();
                            }
                            progDailog.dismiss();
                        }
                    });
                }
            }
        });
    }
}