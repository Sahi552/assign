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
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class login extends AppCompatActivity {

    private EditText Usernumber;
    private Button Userregister,Userverify;
    private FirebaseAuth firebaseAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private static final String TAG = "PhoneAuthActivity";
    private String receivedotp;
    private PhoneAuthProvider.ForceResendingToken receivedtoken;
    private String Mobile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        //initialize variables
        initializer();

        //register button clicked
        Userregister.setOnClickListener(v -> {
            Intent intent = new Intent(login.this,register.class);
            startActivity(intent);
        });

        //verify button clicked
        Userverify.setOnClickListener(v -> {
            //getvalues
            Mobile = Usernumber.getText().toString().trim();

            if (Mobile.isEmpty()) {
                Toast.makeText(login.this,"Enter Phone Number",Toast.LENGTH_SHORT).show();
            }else if (Mobile.length() == 10){
                String Mobilenumber = "+91" + Mobile;
                verifyUserDetails(Mobilenumber);
            }
        });

    }

    private void verifyUserDetails(String mobilenumber) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        Query checkUserDatabse = reference.orderByChild("phonenumber").equalTo(mobilenumber);

        checkUserDatabse.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(login.this,"Verified successful",Toast.LENGTH_SHORT).show();
                    sendotp(mobilenumber);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(login.this,"Verified failed",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializer(){
        Usernumber = findViewById(R.id.editphone);
        Userregister = findViewById(R.id.buttonregister);
        Userverify = findViewById(R.id.buttonverify);

        firebaseAuth = FirebaseAuth.getInstance();
        authcallbacks();
    }

    private void authcallbacks(){
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(login.this,"Invalid Credential",Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Toast.makeText(login.this,"Frequent Credential",Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseAuthMissingActivityForRecaptchaException) {
                    Toast.makeText(login.this,"login failed",Toast.LENGTH_SHORT).show();
                }

            }


            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent:" + verificationId);
                receivedotp = verificationId;
                receivedtoken = token;
                Intent intent = new Intent(login.this,otp.class);
                intent.putExtra("phonenumber",Mobile);
                intent.putExtra("verifyotp",receivedotp);
                intent.putExtra("verifytoken",receivedtoken);
                startActivity(intent);

            }
        };

    }

    private void sendotp(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // (optional) Activity for callback binding
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

}

