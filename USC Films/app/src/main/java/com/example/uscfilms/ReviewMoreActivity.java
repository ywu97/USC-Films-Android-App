package com.example.uscfilms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ReviewMoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reviewmore_activity);

        Intent intent = getIntent();
        String rate = intent.getStringExtra("rating");
        TextView ratingStar = findViewById(R.id.ratingStar);
        ratingStar.setText(rate);

        String userNameAndDate = intent.getStringExtra("userNameAndDate");
        TextView usernameAndCreateDate = findViewById(R.id.usernameAndCreateDate);
        usernameAndCreateDate.setText(userNameAndDate);

        String reviewText = intent.getStringExtra("reviewText");
        TextView reviewMore = findViewById(R.id.reviewMore);
        reviewMore.setText(reviewText);
    }
}