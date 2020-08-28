package com.example.myapplicationmo1.onboarding

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.example.myapplicationmo1.R
import com.example.myapplicationmo1.utils.PrefConstant
import com.example.myapplicationmo1.view.LoginActivity

class OnBoardingActivity : AppCompatActivity() ,OnBoardingOneFragment.OnNextClick,OnBoardingTwoFragment.OnOptionClick{
    lateinit var viewPager:ViewPager
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_board)
        bindView()
        setupSharedPreference()
    }

    private fun setupSharedPreference() {
        sharedPreferences = getSharedPreferences(PrefConstant.SHARED_PREFERENCE_NAME, MODE_PRIVATE)
    }

    fun bindView(){
        viewPager = findViewById(R.id.viewPager)
        val adapter = FragmentAdapter(supportFragmentManager)
        viewPager.adapter = adapter
    }

    override fun onClick() {
        viewPager.currentItem = 1
    }

    override fun onOptionDone() {
        editor = sharedPreferences.edit()
        editor.putBoolean(PrefConstant.ON_BOARDED_SUCCESSFULLY,true)
        editor.apply()
        val intent = Intent(this@OnBoardingActivity,LoginActivity::class.java)
        startActivity(intent)
    }

    override fun onOptionBack() {
        viewPager.currentItem = 0
    }
}