package com.example.myapplicationmo1.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplicationmo1.utils.AppConstant
import com.example.myapplicationmo1.utils.PrefConstant
import com.example.myapplicationmo1.R
import com.example.myapplicationmo1.utils.StoreSession

class LoginActivity : AppCompatActivity() {
    lateinit var editTextFullName:EditText
    lateinit var editTextUserName: EditText
    lateinit var buttonLogin: Button
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editer: SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        bindViews()
        setupSharedPreference()
    }

    private fun setupSharedPreference() {
       StoreSession.init(this)
    }

    private fun bindViews() {
        editTextFullName = findViewById(R.id.editTextFullName)
        editTextUserName = findViewById(R.id.editTextUserName)
        buttonLogin = findViewById(R.id.buttonLogin)
        val clickAction = object :  View.OnClickListener{
            override fun onClick(v: View?) {
                val fullName = editTextFullName.text.toString()
                val userName = editTextUserName.text.toString()
                if(fullName.isNotEmpty() && userName.isNotEmpty()){
                   val intent = Intent(this@LoginActivity, MyNotesActivity::class.java)
                    intent.putExtra(AppConstant.Full_Name,fullName)
                    startActivity(intent)
                    saveFullName(fullName)
                    saveLoginState()
                }else{
                      val toast = Toast.makeText(this@LoginActivity,"Full name and User name Required",Toast.LENGTH_SHORT)
                    toast.show()
                }
            }

        }
        buttonLogin.setOnClickListener(clickAction)
    }

    private fun saveLoginState() {
        StoreSession.write(PrefConstant.IS_LOGGED_IN,true)
       // editer = sharedPreferences.edit()
      //  editer.putBoolean(PrefConstant.IS_LOGGED_IN,true)
        //editer.apply()
    }

    private fun saveFullName(fullName: String) {
        StoreSession.write(PrefConstant.FULL_NAME,fullName)
        //editer = sharedPreferences.edit()
       // editer.putString(PrefConstant.FULL_NAME,fullName)
        //editer.apply()
    }
}