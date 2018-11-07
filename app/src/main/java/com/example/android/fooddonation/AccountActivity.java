package com.example.android.fooddonation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
    private EditText editTextEmail, editTextFirstName, editTextLastName, editTextPhone;
    private Button buttonUpdate;
    private TextView textViewLocation, textViewChangePass;
    private ImageView imageLocation;
    private String coordinates;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = databaseReference.child(firebaseAuth.getUid());
        editTextEmail = findViewById(R.id.editText_account_email);
        editTextFirstName = findViewById(R.id.editText_account_firstName);
        editTextLastName = findViewById(R.id.editText_account_lastName);
        editTextPhone = findViewById(R.id.editText_account_phone);
        buttonUpdate = findViewById(R.id.button_account_update);
        textViewLocation = findViewById(R.id.textView_account_location);
        imageLocation = findViewById(R.id.imageView_account_location);
        progressBar = findViewById(R.id.progressBar_account);
        progressBar.setVisibility(View.GONE);
        textViewChangePass = findViewById(R.id.textView_account_changePass);
        imageLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                intentBuilder.setLatLngBounds(new LatLngBounds(new LatLng(-6.369028, 34.888822), new LatLng(4.038296, 41.832181)));
                try {
                    startActivityForResult(intentBuilder.build(AccountActivity.this), 0);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String firstNameText = editTextFirstName.getText().toString();
                final String lastName = editTextLastName.getText().toString();
                final String location = textViewLocation.getText().toString();
                final String phone = editTextPhone.getText().toString();
                String display = "";
                if (TextUtils.isEmpty(firstNameText)) {
                    display = "Please enter a valid first name";
                    editTextFirstName.requestFocus();
                } else if (TextUtils.isEmpty(lastName)) {
                    display = "Please enter a valid last name";
                    editTextLastName.requestFocus();
                } else if (TextUtils.isEmpty(location)) {
                    display = "Please select a valid location";
                    imageLocation.performClick();
                } else if (TextUtils.isEmpty(phone) || phone.length() < 10) {
                    display = "Please enter a valid phone number";
                    editTextPhone.requestFocus();
                } else {
                    gone(true);
                    firebaseAuth.getCurrentUser().updateEmail(editTextEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(AccountActivity.this, "Successfully Updated", Toast.LENGTH_LONG).show();
                                databaseReference.child("lastName").setValue(lastName);
                                databaseReference.child("location").setValue(location);
                                databaseReference.child("coordinates").setValue(coordinates);
                                databaseReference.child("firstName").setValue(firstNameText);
                                databaseReference.child("contact").setValue(phone);
                            } else {
                                Toast.makeText(AccountActivity.this, "There was an error while updating", Toast.LENGTH_LONG).show();

                            }
                            gone(false);
                        }
                    });
                }
                if (!display.equals(""))
                    Toast.makeText(AccountActivity.this, display, Toast.LENGTH_SHORT).show();
            }
        });
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
    protected void onStart() {
        super.onStart();
        editTextEmail.setText(firebaseAuth.getCurrentUser().getEmail());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                editTextFirstName.setText(user.getFirstName());
                editTextLastName.setText(user.getLastName());
                editTextPhone.setText(user.getContact());
                textViewLocation.setText(user.getLocation());
                coordinates = user.getCoordinates();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void gone(boolean b) {
        progressBar.setVisibility(b ? View.VISIBLE : View.GONE);
        editTextEmail.setEnabled(!b);
        buttonUpdate.setEnabled(!b);
        imageLocation.setEnabled(!b);
        textViewChangePass.setEnabled(!b);
    }
}
