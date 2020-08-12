package com.example.myapplicationmo1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MyNotesActivity extends AppCompatActivity {


    String fullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_notes);

        Intent intent = getIntent();
        fullName = intent.getStringExtra("full_name");
        getActionBar().setTitle(fullName);
    }
}