package com.example.login_page;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class register extends AppCompatActivity {

    private EditText Username,Userage,Usernumber;
    private Button Userregister;
    private CheckBox Userpremiem;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        //initialize variables
        initializer();

        //user register button clicked
        Userregister.setOnClickListener(v -> {

            //get values from edit text
            String Name = Username.getText().toString().trim();
            String Age = Userage.getText().toString().trim();
            String Mobile = Usernumber.getText().toString().trim();
            boolean Premiem = Userpremiem.callOnClick();

            // Validate input
            if (Name.isEmpty() || Age.isEmpty() || Mobile.isEmpty()) {
                Toast.makeText(register.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }else {

                //mobile number
                String MobileNumber = "+91" + Mobile;

                //helper class
                helperclass userdata = new helperclass(Name, Age, MobileNumber, Premiem);

                //store data to database
                databaseReference.child(Name).setValue(userdata).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(register.this, "Data upload failed", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(register.this, "Data upload successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(register.this, login.class);
                            startActivity(intent);
                        }
                    }
                });

            }
        });


    }

    private void initializer(){
        Username = findViewById(R.id.editname);
        Userage = findViewById(R.id.editage);
        Usernumber = findViewById(R.id.editphone);
        Userregister = findViewById(R.id.buttonregister);
        Userpremiem = findViewById(R.id.premiemStatus);

        //intialize database
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");


    }

}
