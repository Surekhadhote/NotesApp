package com.example.myapplicationmo1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    EditText editTextFullName,editTextUserName;
    Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextFullName = findViewById(R.id.editTextFullName);
        editTextUserName = findViewById(R.id.editTextUserName);
        buttonLogin = findViewById(R.id.buttonLogin);
       View.OnClickListener clickAction = new View.OnClickListener(){
          @Override
          public void onClick(View view){

              String fullName = editTextFullName.getText().toString();

              Intent intent = new Intent(LoginActivity.this,MyNotesActivity.class);
              intent.putExtra("full_name",fullName);
              startActivity(intent);
          }
       };
           buttonLogin.setOnClickListener(clickAction);
    }
}