package com.example.android.fooddonation;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;
    private TextView textViewForgot, textViewSign;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        setTitle("Log In");
        setContentView(R.layout.activity_login);
        editTextEmail = findViewById(R.id.editText_login_email);
        editTextPassword = findViewById(R.id.editText_login_password);
        buttonLogin = findViewById(R.id.button_login_login);
        textViewForgot = findViewById(R.id.textView_login_forgot);
        textViewSign = findViewById(R.id.textView_login_register);
        progressBar = findViewById(R.id.progressBar_login);
        progressBar.setVisibility(View.GONE);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connectivityManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
                if (connectivityManager.getActiveNetworkInfo()!=null && networkInfo.isConnectedOrConnecting()) {
                    String email = editTextEmail.getText().toString().trim();
                    String password = editTextPassword.getText().toString().trim();
                    String display = "";
                    if (TextUtils.isEmpty(email)) {
                        editTextEmail.requestFocus();
                        display = "Enter your email address";
                    } else if (TextUtils.isEmpty(password)) {
                        editTextPassword.requestFocus();
                        display = "Enter your password";
                    } else {
                        gone(true);
                        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Wrong username or password", Toast.LENGTH_SHORT).show();
                                    Log.e("Unknown user", task.getException().getMessage());
                                    gone(false);

                                }
                            }
                        });
                    }
                    if (!display.equals(""))
                        Toast.makeText(LoginActivity.this, display, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(LoginActivity.this, "Please connect to the internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
        textViewSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                finish();
            }
        });

        textViewForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetActivity.class));
            }
        });

    }

    private void gone(boolean gone) {
        progressBar.setVisibility(gone ? View.VISIBLE : View.GONE);
        editTextEmail.setEnabled(!gone);
        editTextPassword.setEnabled(!gone);
        buttonLogin.setEnabled(!gone);
        textViewForgot.setVisibility(gone ? View.GONE : View.VISIBLE);
        textViewSign.setVisibility(gone ? View.GONE : View.VISIBLE);
    }
}
