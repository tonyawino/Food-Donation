package com.example.android.fooddonation;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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

public class SignUpActivity extends AppCompatActivity {


    private FirebaseAuth auth;
    private EditText editTextEmail, editTextPassword, editTextConfirmPassword;
    private Button buttonRegister;
    private TextView textViewForgot, textViewLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        auth=FirebaseAuth.getInstance();
        editTextEmail=findViewById(R.id.editText_sign_email);
        editTextPassword=findViewById(R.id.editText_sign_password);
        editTextConfirmPassword=findViewById(R.id.editText_sign_password_confirm);
        buttonRegister=findViewById(R.id.button_sign_register);
        textViewForgot=findViewById(R.id.textView_sign_forgot);
        textViewLogin=findViewById(R.id.textView_sign_login);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=editTextEmail.getText().toString().trim();
                String password=editTextPassword.getText().toString().trim();
                String passwordConfirm=editTextConfirmPassword.getText().toString().trim();
                String display="";
                if (TextUtils.isEmpty(email)){
                    display="Please enter an e-mail address";
                    editTextEmail.requestFocus();
                }
                else if(TextUtils.isEmpty(password) || TextUtils.isEmpty(passwordConfirm)){
                    display="Please enter a password";
                    editTextPassword.requestFocus();
                }
                else if (!password.equals(passwordConfirm)){
                    display="The two passwords do not match";
                    editTextConfirmPassword.requestFocus();
                }
                else {
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                                Toast.makeText(SignUpActivity.this,"Successfully created user", Toast.LENGTH_LONG).show();
                            else
                                Log.e("User not created", task.getException().getMessage());
                        }
                    });
                }
                if (!display.equals(""))
                    Toast.makeText(SignUpActivity.this, display, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
