package com.example.login_page;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class win_certificate extends AppCompatActivity {

    private EditText answerInput;
    private TextView timerText;
    private Button submitAnswerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_win_certificate);

        // Initialize UI elements
        TextView questionText = findViewById(R.id.questionText);
        answerInput = findViewById(R.id.answerInput);
        timerText = findViewById(R.id.timerText);
        submitAnswerButton = findViewById(R.id.submitAnswerButton);

        // Start the countdown timer
        startCountdown();

        // Submit button logic
        submitAnswerButton.setOnClickListener(v -> {
            String answer = answerInput.getText().toString();
            if (!answer.isEmpty()) {
                Toast.makeText(this, "Answer submitted: " + answer, Toast.LENGTH_SHORT).show();
                finish(); // Go back to the previous screen
            } else {
                Toast.makeText(this, "Please write an answer before submitting", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void startCountdown() {
        new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                timerText.setText("Time Left: " + millisUntilFinished / 1000 + "s");
            }

            public void onFinish() {
                timerText.setText("Time's up!");
                answerInput.setEnabled(false);
                submitAnswerButton.setEnabled(true);
                submitAnswerButton.setVisibility(View.VISIBLE);// Enable the submit button
            }
        }.start();
    }
}