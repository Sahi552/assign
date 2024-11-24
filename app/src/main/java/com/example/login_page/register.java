package com.example.login_page;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private EditText name,age,phone;
    private CheckBox pro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        //intialize database
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        //assigning ids
        name = findViewById(R.id.usrname);
        age = findViewById(R.id.usrage);
        Button registration = findViewById(R.id.regbtn);
        pro = findViewById(R.id.premiumStatus);
        phone = findViewById(R.id.phonenumber);

        registration.setOnClickListener(v -> {
            String phoneNumber = phone.getText().toString().trim();
            String username = name.getText().toString().trim();
            String userage = age.getText().toString().trim();
            boolean isPremium = pro.isChecked();

            if (validateInputs(phoneNumber, username, userage)) {
                registerUser(phoneNumber, username, userage, isPremium);
            }
        });
    }

    private boolean validateInputs(String phoneNumber, String username, String userage) {
        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() != 10) {
            Toast.makeText(this, "Enter a valid 10-digit phone number.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Enter your name.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(userage) || !TextUtils.isDigitsOnly(userage)) {
            Toast.makeText(this, "Enter a valid age.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
    private void registerUser(String phoneNumber, String name, String age, boolean isPremium) {
        // Generate a unique user ID
        String userId = databaseReference.push().getKey();

        // User data map
        Map<String, Object> userData = new HashMap<>();
        userData.put("phoneNumber","+91"+ phoneNumber); // Format the phone number
        userData.put("name", name);
        userData.put("age", age);
        userData.put("isPremium", isPremium);

        // Save data to Firebase Realtime Database
        assert userId != null;
        databaseReference.child(userId).setValue(userData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "User registered successfully!", Toast.LENGTH_SHORT).show();
                        clearInputs();
                        Intent i = new Intent(this, login.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(this, "Failed to register user. Try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void clearInputs() {
        phone.setText("");
        name.setText("");
        age.setText("");
        pro.setChecked(false);
    }

}
