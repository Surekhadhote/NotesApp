package com.example.myapplicationmo1.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.example.myapplicationmo1.R
import com.example.myapplicationmo1.adapter.BlogAdapter
import com.example.myapplicationmo1.model.JsonResponse


class BlogActivity : AppCompatActivity() {
    lateinit var recyclerViewBlogs: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blog)
        bindViews()
        getBlogs()
    }

    private fun getBlogs() {
        AndroidNetworking.get("https://fierce-cove-29863.herokuapp.com/getAllUsers/{pageNumber}")
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(JsonResponse::class.java,object : ParsedRequestListener<JsonResponse>{
                    override fun onResponse(response: JsonResponse?) {
                        setupRecyclerView(response)

                    }

                    override fun onError(anError: ANError?) {
                        Log.d("BlogActivity",anError?.localizedMessage)
                    }

                })
    }

    private fun bindViews() {
        recyclerViewBlogs = findViewById(R.id.recyclerViewBlogs)
    }
    private fun setupRecyclerView(response: JsonResponse?) {
        val blogAdapter = BlogAdapter(response!!.data)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        recyclerViewBlogs.layoutManager = linearLayoutManager
        recyclerViewBlogs.adapter = blogAdapter
    }
}