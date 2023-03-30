package com.rickyslash.notesapp.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.rickyslash.notesapp.db.DatabaseContract.NoteColumns
import java.sql.SQLException

class NoteHelper(context: Context) {
    private var databaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    @Throws(SQLException::class)
    fun open() {
        database = databaseHelper.writableDatabase
    }

    fun close() {
        databaseHelper.close()
        if (database.isOpen) database.close()
    }

    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "${NoteColumns._ID} ASC"
        )
    }

    fun queryById(id: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "${NoteColumns._ID} = ?",
            arrayOf(id),
            null,
            null,
            null
        )
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun update(id: String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "${NoteColumns._ID} = ?", arrayOf(id))
    }

    fun deleteById(id: String): Int {
        return database.delete(DATABASE_TABLE, "${NoteColumns._ID} = $id", null)
    }

    companion object {
        private const val DATABASE_TABLE = NoteColumns.TABLE_NAME
        private var INSTANCE: NoteHelper? = null

        fun getInstance(context: Context): NoteHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: NoteHelper(context)
            }
    }
}