package com.example.myapplicationmo1.clickListener

import com.example.myapplicationmo1.db.Notes

interface ItemClickListener {
    fun onClick(notes: Notes)
    fun onUpdate(notes: Notes)
}