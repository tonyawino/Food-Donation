package com.example.android.fooddonation;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private Button buttonReset;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        firebaseAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.editText_reset_email);
        buttonReset = findViewById(R.id.button_reset);
        progressBar = findViewById(R.id.progressBar_reset);
        progressBar.setVisibility(View.GONE);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connectivityManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
                if (connectivityManager.getActiveNetworkInfo()!=null && networkInfo.isConnectedOrConnecting()) {
                    String email = editTextEmail.getText().toString().trim();
                    if (TextUtils.isEmpty(email)) {
                        Toast.makeText(ResetActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                        editTextEmail.requestFocus();
                    } else {
                        progressBar.setVisibility(View.VISIBLE);
                        editTextEmail.setEnabled(false);
                        buttonReset.setEnabled(false);
                        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ResetActivity.this, "Password reset email sent. View your email for instructions", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(ResetActivity.this, LoginActivity.class));
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    editTextEmail.setEnabled(true);
                                    buttonReset.setEnabled(true);
                                    Toast.makeText(ResetActivity.this, "Unable to reset password. Ensure email is correct", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
                else {
                    Toast.makeText(ResetActivity.this, "Please connect to the internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
