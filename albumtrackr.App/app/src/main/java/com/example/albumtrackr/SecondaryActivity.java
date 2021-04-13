package com.example.albumtrackr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class SecondaryActivity extends AppCompatActivity {
    Integer id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondary);
        Intent intent = getIntent();
        id = intent.getIntExtra("albumListID", 0);

        Toast.makeText(getApplicationContext(), id + " is selected!", Toast.LENGTH_SHORT).show();
    }





}