package com.rickyslash.notesapp.helper

import android.database.Cursor
import com.rickyslash.notesapp.db.DatabaseContract
import com.rickyslash.notesapp.entity.Note

// this object is to pass cursor to arrayList
object MappingHelper {

    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<Note> {
        val notesList = ArrayList<Note>()

        notesCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.NoteColumns._ID))
                val title = getString(getColumnIndexOrThrow(DatabaseContract.NoteColumns.TITLE))
                val desc = getString(getColumnIndexOrThrow(DatabaseContract.NoteColumns.DESC))
                val date = getString(getColumnIndexOrThrow(DatabaseContract.NoteColumns.DATE))
                notesList.add(Note(id, title, desc, date))
            }
        }
        return notesList
    }

}