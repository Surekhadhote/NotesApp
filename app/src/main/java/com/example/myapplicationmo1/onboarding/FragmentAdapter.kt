package com.example.myapplicationmo1.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class FragmentAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager){
    override fun getCount(): Int {
     return 2
    }

    override fun getItem(position: Int): Fragment {
    //0,1
        if (position == 0){
            return OnBoardingOneFragment()
        }
        else if (position == 1){
            return OnBoardingTwoFragment()
        }else{
            return null!!
        }
    }

}