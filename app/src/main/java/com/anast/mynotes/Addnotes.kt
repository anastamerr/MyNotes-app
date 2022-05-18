package com.anast.mynotes

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_addnotes.*
import java.lang.Exception

class Addnotes : AppCompatActivity() {
    var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addnotes)


        try {
            var bundle: Bundle = intent.extras!! // so he reades all the intent put extras
            id = bundle.getInt("ID", 0)
            if (id != 0) {
                titlelol.setText(bundle.getString("Title").toString())
                descriptionlol.setText(bundle.getString("Descripition").toString())
            }
        } catch (ex: Exception) {
        }

    }

    fun finish(view: View) {
        var db = DataBaseManager(this)
        var values = ContentValues()
        values.put("Title", titlelol.text.toString())
        values.put("Description", descriptionlol.text.toString())
        if (id == 0) { // that means a new note is added
            var ID = db.insert(values)
            if (ID > 0) {
                Toast.makeText(this, " Note is added successfully", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, " Note isnt added", Toast.LENGTH_LONG).show()

            }
        } else { // means its a case of update
            var freak = arrayOf(id.toString())
            var ID = db.Edit(values, "ID=?", freak)
            if (ID > 0) {
                Toast.makeText(this, " Note is updated successfully", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, " Note isn't updated", Toast.LENGTH_LONG).show()
            }


            //finish() // closes the activity


        }
    }
}