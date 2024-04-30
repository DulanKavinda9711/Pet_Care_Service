package com.example.happytailhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";
    EditText signupName, signupEmail, signupUsername, signupPassword;
    TextView loginRedirectText;
    Button signupButton;
    RadioGroup radioGroup;
    RadioButton radioCustomer, radioCaregiver;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signupName = findViewById(R.id.signup_name);
        signupEmail = findViewById(R.id.signup_email);
        signupUsername = findViewById(R.id.signup_username);
        signupPassword = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);
        radioGroup = findViewById(R.id.radioGroup);
        radioCustomer = findViewById(R.id.radioCustomer);
        radioCaregiver = findViewById(R.id.radioCaregiver);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");

                final String name = signupName.getText().toString().trim();
                String email = signupEmail.getText().toString().trim();
                String username = signupUsername.getText().toString().trim();
                String password = signupPassword.getText().toString().trim();

                if (name.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return; // Stop further execution
                }

                // Check for spaces in each input
                if (name.contains(" ") || email.contains(" ") || username.contains(" ") || password.contains(" ")) {
                    Toast.makeText(SignupActivity.this, "No spaces allowed in fields", Toast.LENGTH_SHORT).show();
                    return; // Stop further execution
                }

                int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                if (selectedRadioButtonId == -1) {
                    // No radio button selected
                    Toast.makeText(SignupActivity.this, "Please select Customer or Caregiver", Toast.LENGTH_SHORT).show();
                    return; // Stop further execution
                }

                String userType;
                if (selectedRadioButtonId == R.id.radioCustomer) {
                    userType = "Customer";
                } else {
                    userType = "Caregiver";
                }

                reference.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Toast.makeText(SignupActivity.this, "User with the same name already exists", Toast.LENGTH_SHORT).show();
                        }else {
                            HelperClass helperClass = new HelperClass(name, email, username, password, userType);
                            reference.child(userType).child(username).setValue(helperClass);
                            Toast.makeText(SignupActivity.this, "You have signed up successfully!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(SignupActivity.this, "Error checking user existence", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: Cleanup or stop ongoing processes here");

        // Clean up resources or stop ongoing processes

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: Cleanup or stop ongoing processes here");

        // Clean up resources or stop ongoing processes

        super.onDestroy();
    }
}