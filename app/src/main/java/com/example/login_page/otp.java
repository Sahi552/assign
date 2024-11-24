package com.example.login_page;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class otp extends AppCompatActivity {

    private static final String TAG = "PhoneAuthActivity";
    private FirebaseAuth mAuth;
    private String verification;
    private PhoneAuthProvider.ForceResendingToken token1;
    private String phone_number;
    private EditText otpField;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_otp);

        mAuth = FirebaseAuth.getInstance();

        otpField = findViewById(R.id.otp);
        TextView resendOtp = findViewById(R.id.resendotp);
        Button loginButton = findViewById(R.id.logbtn);

        // Get data from Intent
        phone_number = getIntent().getStringExtra("phnum");
        verification = getIntent().getStringExtra("verifyid");
        token1 = getIntent().getParcelableExtra("token");

        // Initialize callbacks
        initializeCallbacks();

        // Clear OTP field on start
        clearOtpField();

        // Login button click
        loginButton.setOnClickListener(v -> {
            String enteredOtp = otpField.getText().toString();
            if (enteredOtp.isEmpty()) {
                Toast.makeText(this, "Enter OTP", Toast.LENGTH_SHORT).show();
            } else if (enteredOtp.length() == 6) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(
                        Objects.requireNonNull(verification), enteredOtp);
                signInWithPhoneAuthCredential(credential);
            } else {
                Toast.makeText(this, "Enter a valid 6-digit OTP", Toast.LENGTH_SHORT).show();
            }
        });

        // Resend OTP click
        resendOtp.setOnClickListener(v -> {
            resendVerificationCode();
            clearOtpField();
        });
    }

    // Sign-In with Phone Credential
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = task.getResult().getUser();
                        Toast.makeText(this, "Sign-in successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    } else {
                        Exception exception = task.getException();
                        if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Sign-in failed: " + exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
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
                    Toast.makeText(otp.this, "Invalid phone number.", Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Log.w(TAG, "Quota exceeded.", e);
                    Toast.makeText(otp.this, "Too many requests. Try again later.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.w(TAG, "Verification failed.", e);
                    Toast.makeText(otp.this, "Verification failed.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // Update verification ID and token for resending
                verification = verificationId;
                token1 = token;
            }
        };
    }

    // Resend OTP
    private void resendVerificationCode() {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phone_number)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .setForceResendingToken(token1)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    // Clear OTP Field
    private void clearOtpField() {
        otpField.setText("");
    }
}
