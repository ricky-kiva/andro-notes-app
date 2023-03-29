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
/*CREATE TABLE table_name (
    column1 datatype column_constraint,
    column2 datatype column_constraint,
    column3 datatype column_constraint
);*/

// To INSERT data:
/*INSERT INTO table_name(column1, column2, ...)
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