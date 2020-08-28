package com.example.myapplicationmo1.view


import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.multidex.MultiDexApplication
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.myapplicationmo1.NotesApp
import com.example.myapplicationmo1.R
import com.example.myapplicationmo1.adapter.NotesAdapter
import com.example.myapplicationmo1.clickListener.ItemClickListener
import com.example.myapplicationmo1.db.Notes
import com.example.myapplicationmo1.utils.AppConstant
import com.example.myapplicationmo1.utils.PrefConstant
import com.example.myapplicationmo1.utils.StoreSession
import com.example.myapplicationmo1.view.workmanager.MyWorker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_my_notes.*
import java.util.concurrent.TimeUnit

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
public class MyNotesActivity : AppCompatActivity() {
    var fullName = ""
    val ADD_NOTES_CODE = 100
    lateinit var buttonAddNotes:FloatingActionButton
    val TAG = "MyNotesActivity"
    lateinit var sharedPreferences: SharedPreferences
    lateinit var recyclerView: RecyclerView
    var notesList = ArrayList<Notes>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_notes)
        setupSharedferences()
        bindViews()
        getIntentData()
        getDataFromDatabase()
        setupToolbarText()
        clickListeners()
        setupRecyclerView()
        setupWorkManager()
    }

    private fun clickListeners() {
        buttonAddNotes.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                //setupDialogBox()
                val intent = Intent(this@MyNotesActivity,AddNotesActivity::class.java)
                startActivityForResult(intent,ADD_NOTES_CODE)
            }

        })
    }

    private fun setupWorkManager() {
        val constraint = Constraints.Builder()
                .build()
        val request = PeriodicWorkRequest
                .Builder(MyWorker::class.java,15,TimeUnit.MINUTES)
                .setConstraints(constraint)
                .build()
        WorkManager.getInstance().enqueue(request)


    }

    private fun getIntentData() {
        val intent = intent
        fullName = if(intent.hasExtra(AppConstant.Full_Name)) {
            intent.getStringExtra(AppConstant.Full_Name)
        }
        else {
            StoreSession.readString(PrefConstant.FULL_NAME).toString()
        }
    }

    private fun getDataFromDatabase() {
        val notesApp = applicationContext as NotesApp
        val notesDao = notesApp.getNotesDb().notesDao()
        val listNotes = notesDao.getAll()
        notesList.addAll(notesDao.getAll())
    }

    private fun setupRecyclerView() {
        val itemClickListener = object :ItemClickListener{


            override fun onUpdate(notes: Notes) {
                val notesApp = applicationContext as NotesApp
                val noteDao = notesApp.getNotesDb().notesDao()
                noteDao.updateNotes(notes)
            }


            override fun onClick(notes: Notes) {
                val intent =Intent(this@MyNotesActivity, DetailActivity::class.java)
                intent.putExtra(AppConstant.TITLE,notes.title)
                intent.putExtra(AppConstant.DESCRIPTION,notes.description)
                startActivity(intent)
            }





        }
        val notesAdapter = NotesAdapter(notesList,itemClickListener)
        val linearLayoutManager = LinearLayoutManager(this@MyNotesActivity)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = notesAdapter
    }

    private fun bindViews() {
        buttonAddNotes = findViewById(R.id.fabAddNotes)
        recyclerView = findViewById(R.id.recyclerViewNotes)
    }

    private fun setupSharedferences() {
        StoreSession.init(this)
       // sharedPreferences = getSharedPreferences(PrefConstant.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    private fun setupToolbarText() {
        if (supportActionBar != null)
            supportActionBar?.title = fullName
    }





     private fun setupDialogBox() {
        val view = LayoutInflater.from(this@MyNotesActivity).inflate(R.layout.add_notes_dialog_layout,null)
        val editTextTitle = view.findViewById<EditText>(R.id.editTextTitle)
        val editTextDescription = view.findViewById<EditText>(R.id.editTextDescription)
        val buttonSubmit = view.findViewById<Button>(R.id.buttonSubmit)
        val dialog = AlertDialog.Builder(this)
                .setView(view)
                .setCancelable(false)
                .create()
        buttonSubmit.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                val title = editTextTitle.text.toString()
                val description = editTextDescription.text.toString()
               val notes = Notes(title = title, description =  description)
                notesList.add(notes)
                recyclerViewNotes.adapter?.notifyItemChanged(notesList.size-1)
                addNotesToDb(notes)
                dialog.hide()
            }


        })
        dialog.show()
    }

      private fun addNotesToDb(notes: Notes) {
    //insert to db
    val notesApp = applicationContext as NotesApp
    val notesDao = notesApp.getNotesDb().notesDao()
    notesDao.insert(notes)
}




override fun onActivityResult(requestCode: Int,resultCode: Int, data: Intent?){
       super.onActivityResult(requestCode,resultCode,data)
       if (requestCode == ADD_NOTES_CODE){
           val title = data?.getStringExtra(AppConstant.TITLE)
           val description  = data?.getStringExtra(AppConstant.DESCRIPTION)
           val imagePath = data?.getStringExtra(AppConstant.IMAGE_PATH)

           val notes = Notes(title = title!!,description = description!!,imagePath = imagePath!!,isTaskCompleted = false)
           addNotesToDb(notes)
           notesList.add(notes)
           recyclerViewNotes.adapter?.notifyItemChanged(notesList.size-1)
       }
   }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.blog){
            Log.d(TAG,"succeful")
            val intent = Intent(this@MyNotesActivity,BlogActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

}










