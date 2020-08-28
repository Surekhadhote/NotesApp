@file:Suppress("DEPRECATION")

package com.example.myapplicationmo1.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.DATA
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.example.myapplicationmo1.BuildConfig
import com.example.myapplicationmo1.R
import com.example.myapplicationmo1.utils.AppConstant
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddNotesActivity : AppCompatActivity() {

    lateinit var editTextTitle: EditText
    lateinit var editTextDescription: EditText
    lateinit var imageViewNotes: ImageView
    lateinit var buttonSubmit: Button
    val requestCodeGallery = 2
    val requestCodeCamera = 1
    var picturePath = ""
    var myPermissionCode = 124
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notes)
        bindViews()
        clickListeners()
    }

    private fun clickListeners() {
       imageViewNotes.setOnClickListener(object : View.OnClickListener{
           override fun onClick(v: View?) {
               if (checkAddRequestPermission()) {
                   setupDialog()
               }
           }

       })
        buttonSubmit.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                val intent = Intent()
                intent.putExtra(AppConstant.TITLE,editTextTitle.text.toString())
                intent.putExtra(AppConstant.DESCRIPTION,editTextDescription.text.toString())
                intent.putExtra(AppConstant.IMAGE_PATH,picturePath)
                setResult(Activity.RESULT_OK,intent)
                finish()
            }

        })
    }

    private fun checkAddRequestPermission(): Boolean {
          val cameraPermission = ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)
        val storagePermission = ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)
        val listPermissionNeeded = ArrayList<String>()
        if (cameraPermission != PackageManager.PERMISSION_GRANTED){
            listPermissionNeeded.add(android.Manifest.permission.CAMERA)
        }
        if (storagePermission != PackageManager.PERMISSION_GRANTED){
            listPermissionNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (listPermissionNeeded.isNotEmpty()){
            ActivityCompat.requestPermissions(this,listPermissionNeeded.toTypedArray<String>(),myPermissionCode)
            return false
        }
        return true

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            myPermissionCode ->{
                if (grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    setupDialog()
                }
            }
        }
    }

    private fun setupDialog() {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_selector,null)
        val textViewCamera:TextView = view.findViewById(R.id.textViewCamera)
        val textViewGallery:TextView = view.findViewById(R.id.textViewGallery)
        val dialog = AlertDialog.Builder(this)
                .setView(view)
                .setCancelable(true)
                .create()
        textViewCamera.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                val photoFile: File? = createImage()
                if (photoFile != null) {
                    val photoURI = FileProvider.getUriForFile(this@AddNotesActivity, BuildConfig.APPLICATION_ID + ".provider", photoFile)
                    picturePath = photoFile.absolutePath
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, requestCodeCamera)
                    dialog.hide()
                }
            }

        })



        textViewGallery.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
              val intent = Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent,requestCodeGallery)
                dialog.hide()
            }

        })
        dialog.show()
    }

    @SuppressLint("SimpleDateFormat")
    private fun createImage(): File? {
      val timeStrap = SimpleDateFormat("yyyy-MM-dd-HH=-mm-ss").format(Date())
        val fileName = "JPEG_"+timeStrap+"_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName,".jpg",storageDir)
    }

    private fun bindViews() {
        editTextTitle = findViewById(R.id.editTextTitle)
        editTextDescription = findViewById(R.id.editTextDescription)
        imageViewNotes = findViewById(R.id.imageViewNotes)
        buttonSubmit = findViewById(R.id.buttonSubmit)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            when(requestCode){

                requestCodeGallery ->{
                   val selectedImage = data?.data
                    val filePath = arrayOf(MediaStore.Images.Media.DATA)
                    val c = contentResolver.query(selectedImage!!,filePath,null,null,null)
                    c?.moveToFirst()
                    val columnIndex = c?.getColumnIndex(filePath[0])
                    picturePath = c!!.getString(columnIndex!!)
                    c.close()
                    Log.d("AddNotesActivity",picturePath)
                    Glide.with(this).load(picturePath).into(imageViewNotes)
                }

                requestCodeCamera ->{
                    Glide.with(this).load(picturePath).into(imageViewNotes)
                }
            }
        }
    }
}