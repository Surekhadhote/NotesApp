package com.example.myapplicationmo1

import android.app.Application
import androidx.multidex.MultiDexApplication
import com.androidnetworking.AndroidNetworking
import com.example.myapplicationmo1.db.NotesDatabase

class NotesApp : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        AndroidNetworking.initialize(applicationContext);
    }
fun getNotesDb(): NotesDatabase{
    return NotesDatabase.getInstance(this)
}
}
