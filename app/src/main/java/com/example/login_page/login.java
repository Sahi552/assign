package com.example.login_page;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class login extends AppCompatActivity {

    //initalize variable
    private static final String TAG = "PhoneAuthActivity";
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String phoneNumber;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        //initialize firebase
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        Button verifybtn = findViewById(R.id.verifybtn);
        Button regbtn = findViewById(R.id.regbtn);
        EditText phnumber = findViewById(R.id.phonenumber);

        // Register Button Listener
        regbtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, register.class);
            startActivity(intent);
        });

        // Verify Button Listener
        verifybtn.setOnClickListener(v -> {
            phoneNumber = phnumber.getText().toString().trim();
            if (phoneNumber.isEmpty()) {
                Toast.makeText(this, "Please enter a phone number.", Toast.LENGTH_SHORT).show();
            } else if (phoneNumber.length() == 10) {
                phoneNumber = "+91" + phoneNumber; // Format phone number
                checkPhoneNumberInDatabase(phoneNumber);
            } else {
                Toast.makeText(this, "Please enter a valid 10-digit phone number.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    // Initialize Callbacks
    private void initializeCallbacks() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                // Automatic verification or instant sign-in
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Log.w(TAG, "Invalid phone number.", e);
                    Toast.makeText(login.this, "Invalid phone number.", Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Log.w(TAG, "Quota exceeded.", e);
                    Toast.makeText(login.this, "Too many requests. Try again later.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.w(TAG, "Verification failed.", e);
                    Toast.makeText(login.this, "Verification failed.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // Pass verificationId and token to the OTP activity
                Intent intent = new Intent(login.this, otp.class);
                intent.putExtra("verifyid", verificationId);
                intent.putExtra("token", token);
                intent.putExtra("phnum", phoneNumber); // Ensure phoneNumber is a global variable or pass it explicitly
                startActivity(intent);
            }
        };
    }


    // Check Phone Number in Database
    private void checkPhoneNumberInDatabase(String phoneNumber) {

        databaseReference.orderByChild("phoneNumber").equalTo(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Phone number exists, start OTP verification
                    startPhoneNumberVerification(phoneNumber);
                } else {
                    // Phone number does not exist, redirect to RegisterActivity
                    Toast.makeText(login.this, "Phone number not registered. Please register first.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(login.this, register.class);
                    intent.putExtra("phoneNumber", phoneNumber);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(login.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Phone Number Verification
    private void startPhoneNumberVerification(String phoneNumber) {
        if (mCallbacks == null) {  // Ensure callbacks are initialized
            initializeCallbacks();
        }
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)           // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS)     // Timeout duration
                        .setActivity(this)                    // Activity (for callback binding)
                        .setCallbacks(mCallbacks)             // Pass the initialized callbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


    // Sign-In with Phone Credential
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Sign-in successful.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    } else {
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(this, "Invalid verification code.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
