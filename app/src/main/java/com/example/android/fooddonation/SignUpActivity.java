package com.example.android.fooddonation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {


    private FirebaseAuth auth;
    private EditText editTextEmail, editTextPassword, editTextConfirmPassword, editTextFirstName, editTextLastName, editTextPhone;
    private Button buttonRegister;
    private TextView textViewForgot, textViewLogin, textViewLocation;
    private ImageView imageLocation;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
    private String coordinates;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        auth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.editText_sign_email);
        editTextPassword = findViewById(R.id.editText_sign_password);
        editTextConfirmPassword = findViewById(R.id.editText_sign_password_confirm);
        buttonRegister = findViewById(R.id.button_sign_register);
        textViewForgot = findViewById(R.id.textView_sign_forgot);
        textViewLogin = findViewById(R.id.textView_sign_login);
        editTextFirstName = findViewById(R.id.editText_sign_firstName);
        editTextLastName = findViewById(R.id.editText_sign_lastName);
        editTextPhone = findViewById(R.id.editText_sign_phone);
        textViewLocation = findViewById(R.id.textView_sign_location);
        imageLocation = findViewById(R.id.imageView_sign_location);
        progressBar = findViewById(R.id.progressBar_sign);
        progressBar.setVisibility(View.GONE);
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            }
        });
        textViewForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, ResetActivity.class));
            }
        });
        imageLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                intentBuilder.setLatLngBounds(new LatLngBounds(new LatLng(-6.369028, 34.888822), new LatLng(4.038296, 41.832181)));
                try {
                    startActivityForResult(intentBuilder.build(SignUpActivity.this), 0);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (connectivityManager.getActiveNetworkInfo() != null && networkInfo.isConnectedOrConnecting()) {
                    String email = editTextEmail.getText().toString().trim();
                    String password = editTextPassword.getText().toString().trim();
                    String passwordConfirm = editTextConfirmPassword.getText().toString().trim();
                    final String firstName = editTextFirstName.getText().toString().trim();
                    final String lastName = editTextLastName.getText().toString().trim();
                    final String phone = editTextPhone.getText().toString().trim();
                    final String location = textViewLocation.getText().toString();
                    String display = "";
                    if (TextUtils.isEmpty(firstName)) {
                        display = "Please enter first name";
                        editTextFirstName.requestFocus();
                    } else if (TextUtils.isEmpty(lastName)) {
                        display = "Please enter last name";
                        editTextLastName.requestFocus();
                    } else if (TextUtils.isEmpty(phone)) {
                        display = "Please enter a phone number";
                        editTextPhone.requestFocus();
                    } else if (TextUtils.isEmpty(location)) {
                        display = "Please select a valid location";
                        textViewLocation.requestFocus();
                    } else if (TextUtils.isEmpty(email)) {
                        display = "Please enter an e-mail address";
                        editTextEmail.requestFocus();
                    } else if (TextUtils.isEmpty(password) || TextUtils.isEmpty(passwordConfirm)) {
                        display = "Please enter a password";
                        editTextPassword.requestFocus();
                    } else if (!password.equals(passwordConfirm)) {
                        display = "The two passwords do not match";
                        editTextConfirmPassword.requestFocus();
                    } else if (phone.length() < 10) {
                        display = "Enter a valid phone number";
                        editTextPhone.requestFocus();
                    } else if (password.length() < 6) {
                        display = "Password must be 6 or more characters";
                        editTextPassword.requestFocus();
                    } else {
                        gone(true);
                        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    User user = new User(firstName, lastName, location, phone, coordinates);
                                    String userId = auth.getCurrentUser().getUid();
                                    databaseReference.child(userId).setValue(user);
                                    Toast.makeText(SignUpActivity.this, "Successfully created user", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                    finish();
                                } else {
                                    Log.e("User not created", task.getException().getMessage());
                                    Toast.makeText(SignUpActivity.this, "Unable to create user", Toast.LENGTH_SHORT).show();
                                    gone(false);
                                }
                            }
                        });
                    }
                    if (!display.equals(""))
                        Toast.makeText(SignUpActivity.this, display, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(SignUpActivity.this, "Please connect to the internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void gone(boolean b) {
        progressBar.setVisibility(b ? View.VISIBLE : View.GONE);
        editTextEmail.setEnabled(!b);
        editTextPassword.setEnabled(!b);
        editTextConfirmPassword.setEnabled(!b);
        buttonRegister.setEnabled(!b);
        textViewForgot.setVisibility(b ? View.GONE : View.VISIBLE);
        textViewLogin.setVisibility(b ? View.GONE : View.VISIBLE);
        imageLocation.setEnabled(!b);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                textViewLocation.setText(place.getName());
                coordinates = place.getLatLng().latitude + ", " + place.getLatLng().longitude;

            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
