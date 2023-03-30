package com.rickyslash.notesapp

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.rickyslash.notesapp.databinding.ActivityNoteAddUpdateBinding
import com.rickyslash.notesapp.db.DatabaseContract.NoteColumns
import com.rickyslash.notesapp.db.NoteHelper
import com.rickyslash.notesapp.entity.Note
import java.text.SimpleDateFormat
import java.util.*

// this activity is used when you want to 'add / update' a 'Note'
class NoteAddUpdateActivity : AppCompatActivity(), View.OnClickListener {

    private var isEdit = false
    private var note: Note? = null
    private var position: Int = 0
    private lateinit var noteHelper: NoteHelper

    private lateinit var binding: ActivityNoteAddUpdateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteAddUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // making new singleton instance of NoteHelper
        noteHelper = NoteHelper.getInstance(applicationContext)
        noteHelper.open()

        // get parcelable extra containing Note
        note = intent.getParcelableExtra(EXTRA_NOTE)

        // sets whether it's going to be 'add' / 'edit'
        if (note != null) {
            position = intent.getIntExtra(EXTRA_POSITION, 0)
            isEdit = true
        } else {
            note = Note()
        }

        // initialize string for actionBar
        val actionBarTitle: String
        val btnTitle: String

        // set component if it's 'edit'
        if (isEdit) {
            actionBarTitle = "Change"
            btnTitle = "Update"

            // pass the assigned value inside note to layout element
            note?.let {
                binding.edtTitle.setText(it.title)
                binding.edtDescription.setText(it.desc)
            }
        } else {
            // set component if it's 'add'
            actionBarTitle = "Add"
            btnTitle = "Save"
        }

        // set actionBar strings
        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // add action to btnSubmit
        binding.btnSubmit.text = btnTitle
        binding.btnSubmit.setOnClickListener(this)
    }

    // set action when btnSubmit clicked
    override fun onClick(v: View?) {
        if (v?.id == R.id.btn_submit) {

            // set 'title' / 'desc' value
            val title = binding.edtTitle.text.toString().trim()
            val desc = binding.edtDescription.text.toString().trim()

            // checks whether 'title' is empty
            if (title.isEmpty()) {
                binding.edtTitle.error = "Field can't be blank"
                return
            }

            // update value of 'Note' object
            note?.title = title
            note?.desc = desc

            // putExtra to Intent
            val intent = Intent()
            intent.putExtra(EXTRA_NOTE, note)
            intent.putExtra(EXTRA_POSITION, position)

            // put values to 'database'
            val values = ContentValues()
            values.put(NoteColumns.TITLE, title)
            values.put(NoteColumns.DESC, desc)

            // if the type is 'edit'
            if (isEdit) {
                // execute 'noteHelper.update()' to update by new value
                val result = noteHelper.update(note?.id.toString(), values).toLong()
                // if 'result' successfully updated to database, then 'send intent with result' using 'setResult'
                if (result > 0) {
                    setResult(RESULT_UPDATE, intent)
                    finish()
                } else {
                    Toast.makeText(this@NoteAddUpdateActivity, "Fail to update data", Toast.LENGTH_SHORT).show()
                }
            } else {
                // if the type is 'NOT edit' (means it's add) then this continued

                // 'set' the 'Date' value of 'Note' object
                note?.date = getCurrentDate()

                // put values to 'database'
                values.put(NoteColumns.DATE, getCurrentDate())

                // execute 'noteHelper.insert()' to insert new value
                val result = noteHelper.insert(values)

                // if 'result' successfully added to database, then 'send intent with result' using 'setResult'
                if (result > 0) {
                    note?.id = result.toInt()
                    setResult(RESULT_ADD, intent)
                    finish()
                } else {
                    Toast.makeText(this@NoteAddUpdateActivity, "Fail to add data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // set 'clear' on 'menu' when it's 'edit'
        if (isEdit) {
            menuInflater.inflate(R.menu.menu_form, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> showAlertDialog(ALERT_DIALOG_DELETE)
            android.R.id.home -> showAlertDialog(ALERT_DIALOG_CLOSE)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE)
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        val date = Date()

        return dateFormat.format(date)
    }

    private fun showAlertDialog(type: Int) {
        val isDialogClose = type == ALERT_DIALOG_CLOSE
        val dialogTitle: String
        val dialogMessage: String

        if (isDialogClose) {
            dialogTitle = "Cancel"
            dialogMessage = "Cancel updating the form?"
        } else {
            dialogTitle = "Delete"
            dialogMessage = "Delete the note?"
        }

        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(dialogTitle)
        alertDialogBuilder
            .setMessage(dialogMessage)
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                if (isDialogClose) {
                    finish()
                } else {
                    val result = noteHelper.deleteById(note?.id.toString()).toLong()
                    if (result > 0) {
                        val intent = Intent()
                        intent.putExtra(EXTRA_POSITION, position)
                        setResult(RESULT_DELETE, intent)
                        finish()
                    } else {
                        Toast.makeText(this@NoteAddUpdateActivity, "Failed to delete Note", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("No") { dialog, _ -> dialog.cancel()}
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    companion object {
        const val EXTRA_NOTE = "extra_note"
        const val EXTRA_POSITION = "extra_position"
        const val RESULT_ADD = 101
        const val RESULT_UPDATE = 201
        const val RESULT_DELETE = 301
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 20
    }

}