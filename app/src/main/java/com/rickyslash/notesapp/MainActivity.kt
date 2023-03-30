package com.rickyslash.notesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

// SQL (Structured Query Language) is a 'standardized query language' to 'access database'
// Example of Query -> Select * from students where full_name = 'Rickyslash' (it has 3 parts):
// - 'select *' -> means it is going to 'get data from database' with 'range of all column'
// - 'from students' -> means it's going to search from 'students' 'table'
// - 'where full_name' -> means it's going to search on 'full_name' 'column'

// CRUD (Create, Read, Update, Delete) is the 'basic transaction' of 'managing data in database'
// - CREATE (C): query -> CREATE, INSERT
// - READ (R): query -> SELECT
// - UPDATE (U): query -> UPDATE & ALTER
// - DELETE (D): query -> DELETE

// Another basic query:
// - TABLE: make table before adding data. Note:
// --- title: SQL is non-sensitive case, but need '_' for space
// --- stating 'data type' on a table is 'a must'
// --- Constraint -> 'Rules need to be applied' to 'each row' (UNIQUE, NOT NULL, PRIMARY KEY, etc)

// To create TABLE:
/*
CREATE TABLE table_name (
    column1 datatype column_constraint,
    column2 datatype column_constraint,
    column3 datatype column_constraint
);
*/

// To INSERT data:
/*
INSERT INTO table_name(column1, column2, ...)
  VALUES (value1, value2, ...);*/
// OR
// INSERT INTO table_name VALUES (value1, value2, ...);

// To SELECT and view data:
// SELECT list_of_column_to_be_displayed, ... FROM table_name;
// To SELECT inside all, with some filtered value:
// SELECT * FROM table_name WHERE column_name = 'value'
// To SELECT some words inside value:
// SELECT * FROM table_name WHERE column_name LIKE 'value%'
// To SELECT some value with custom ordering:
// SELECT * FROM table_name ORDER BY column_name 'ASC' (in example: ascending)
// To SELECT some value with limit:
// SELECT * FROM table_name LIMIT 'number_limit';

// To UPDATE value:
/*UPDATE table_name
* SET column_name = 'value'
* WHERE condition*/

// ex condition: WHERE id = '3'

// To DELETE value:
// DELETE FROM table_name WHERE condition

// SQLite: 'open source database' that supports 'standard relational operation', and is 'lightweight
// SQLite doesn't need 'authentication' like other scaled database
// SQLite can 'only be accessed' by the 'app itself'
// To enable 'SQLite data' be 'accessed by another app', it needs 'ContentProvider' mechanism 'by app that made the database'
// best to use 'asynchronous' (Executor / Coroutine) to save big data

// 'Defining a schema' is the 'first thing to do' to 'create SQLite database'
// 'Creating a contract' often known as 'defining schema' in Android

// example of SQL contract:
/*
class BookContract {
    class BookColumns : BaseColumns {
        companion object {
            val TABLE_NAME = "book"
            val COLUMN_NAME_TITLE = "title"
            val COLUMN_NAME_AUTHOR = "author"
            val COLUMN_NAME_GENRE = "genre"
            val COLUMN_NAME_PAGES = "pages"
        }
    }
}
*/

// Note: if the 'value' is 'incremental' & act as 'primary key' (like ID), the column need to be started with '_' (example: '_id'). Thus the column will be implemented 'BaseColumns'

// // SQLiteOpenHelper: a class containing set of API to make & update application. The class specified to run 'Data Definition Language' (DDL) functions on a 'database'

// 'SQL_CREATE_ENTRIES' is a statement to create database. Example:
/*
private val SQL_CREATE_ENTRIES = "CREATE TABLE ${BookColumns.TABLE_NAME}" +
        " (${BookColumns._ID} INTEGER PRIMARY KEY," +
        " ${BookColumns.COLUMN_NAME_TITLE} TEXT," +
        " ${BookColumns.COLUMN_NAME_AUTHOR} TEXT," +
        " ${BookColumns.COLUMN_NAME_GENRE} TEXT," +
        " ${BookColumns.COLUMN_NAME_PAGES} INT)"
*/

// 'SQL_DELETE_ENTRIES' is a statement to delete database
/*private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $BookColumns.TABLE_NAME"*/

// Best practice for having 'so much table' inside a 'database': 'make class' for 'each table'

// 'Data Manipulation Language' (DML) API is available via 'SQLiteDatabase class' . Example:
// - INSERT:
/*
val db = mDbHelper.getWritableDatabase()

val values = ContentValues()
values.put(BookColumns.COLUMN_NAME_TITLE, "Clean Code: A Handbook of Agile Software Craftsmanship")
values.put(BookColumns.COLUMN_NAME_AUTHOR, "Robert C. Martin")
values.put(BookColumns.COLUMN_NAME_GENRE, "Programming")
values.put(BookColumns.COLUMN_NAME_PAGES, 434)

val newRowId = db.insert(BookColumns.TABLE_NAME, null, values)
*/
// - the 'null' in '.insert' is 'nullColumnHack'

// Note: provide non-null values for at least one of the columns for 'nullColumnHack', to 'prevent row can't be inserted to database' when 'there is null value'
// when 'nullColumnHack' is only being passed 'null', when 'null' value is inserted, the 'primary key value' (often '_id') will be passed 'null'. This will leads to an error
// passing 'another column' as a 'hack' will fix this. example: '.insert(BookColums.TABLE_NAME, BookColumns.COLUMN_NAME_GENRE, values)'

// READ (get data) could be done using 'query()' or 'rawQuery()' method
// - the output of both methods is 'cursor object'

/*
var db = mDbHelper.getReadableDatabase()

// Defining the 'column to be read'
var projection = arrayOf(
    BookColumns._ID,
    BookColumns.COLUMN_NAME_TITLE,
    BookColumns.COLUMN_NAME_AUTHOR,
    BookColumns.COLUMN_NAME_GENRE
)

// in case there is a 'filter'
var selection = BookColumns.COLUMN_NAME_AUTHOR + " LIKE ?"
var selectionArgs = arrayOf("Stephen")

// in case wanting to be 'ordered'
var sortOrder = BookColumns.COLUMN_NAME_TITLE + " DESC"

var c = db.query(
        BookColumns.TABLE_NAME, // table to search
        projection,             // column to be read
        selection,              // column that act as a filter
        selectionArgs,          // value for the filter
        null,                   // if there is grouping for result (null for no)
        null,                   // filter for grouping (null for no)
        sortOrder               // sort order type
)
*/

// - that '.query' will return 'cursor object'
// - to get data from the 'cursor object' could be done like this:
/*
cursor.moveToFirst()
val itemId = cursor.getInt(cursor.getColumnIndexOrThrow(BookColums._ID))
*/

// to pass all 'object cursor' item to ArrayList:
/*
if (cursor.count()>0) {
    do {
        bookColumns = BookColumns()
        bookColumns.setId(cursor.getInt(cursor.getColumnIndex(BookColumns._ID)))
        bookColumns.setTitle(cursor.getString(cursor.getColumnIndex(BookColumns.COLUMN_NAME_TITLE)))
        bookColumns.setAuthor(cursor.getString(cursor.getColumnIndex(BookColumns.COLUMN_NAME_AUTHOR)))
        bookColumns.setGenre(cursor.getString(cursor.getColumnIndex(BookColumns.COLUMN_NAME_GENRE)))
        bookColumns.setPages(cursor.getInt(cursor.getColumnIndex(BookColumns.COLUMN_NAME_PAGES)))
        arrayList.add(bookColumns)
        cursor.moveToNext()
    } while (!cursor.isAfterLast)
}
*/

// rawQuery: a way to do query inline example: (to get all column by descending order):
// - database.rawQuery("SELECT * FROM ${BookColums.TABLE_NAME} ORDER BY ${BookColums.COLUMN_NAME_PAGES} DESC", null)

// UPDATE data inside table:
/*
val db = mDbHelper.getWritableDatabase()

// 'set value' to 'update the column'
val values =  ContentValues()
values.put(BookColumns.COLUMN_NAME_TITLE, title)

// 'Condition clause' where 'update' is 'being conducted'
val selection = BookColumns.COLUMN_NAME_GENRE + " = ?"
val selectionArgs = arrayOf("Programming")

int count = db.update(
    BookColumns.TABLE_NAME,
    values,
    selection,
    selectionArgs
)
*/

// DELETE data inside table:
/*
// 'Condition clause' where 'delete' is being 'conducted'
val selection = BookColumns.COLUMN_NAME_AUTHOR + " LIKE ?"
// set cell with 'related value' to be 'deleted'
val selectionArgs = arrayOf("shark")

// 'execute' the 'sql statement'
db.delete(BookColums.TABLE_NAME, selection, selectionArgs)
*/

// SQLiteCipher is one of the 'database security' mechanism that could be implemented
// - it encrypts data using '256-bit AES'