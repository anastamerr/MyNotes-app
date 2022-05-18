package com.anast.mynotes

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.media.projection.MediaProjection
import android.widget.Toast


class DataBaseManager {
    val dbName = "MyNotes"
    val dbTable ="Notes"
    val colID= "ID"
    val colTitle = "Title"        // the title key of the note that we implemented
    val colDes = "Description"   // the description of the note key
    val dbVersion = 1
    val sqlCreateTable = "CREATE TABLE IF NOT EXISTS " + dbTable + " ("+ colID +" INTEGER PRIMARY KEY,"+ //every table should have a primary key
    colTitle + " TEXT, " + colDes+" TEXT)"    // THIS IS AN sql statement to make the data familiar
    var sqlDB:SQLiteDatabase?=null

    constructor(context: Context){
            var db = databaseHelper(context) // sends the data to the database helper class
            sqlDB = db.writableDatabase     // means i want to write something in the database and it writes it in the sqlDB

    }

   inner class databaseHelper: SQLiteOpenHelper { // its responsible to create database for me that is the function of SQLITEOpenHelper
       var context:Context?=null
        constructor(context:Context) : super(context,dbName,null,dbVersion) {   // the constructor of opne helper takes 4 parameters #1 the context , #2 the data base name #3 faacotry and #4 the version od the database
            this.context = context
        }
        override fun onCreate(db: SQLiteDatabase?) { // when someone creates database
            db!!.execSQL(sqlCreateTable)    // it means run and save the database in the following statement
            Toast.makeText(this.context," database is created",Toast.LENGTH_LONG).show()
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) { // when someone upgrades the version of the app
           db!!.execSQL("Drop table IF EXISTS " + dbTable)
        }

    }

    fun insert(values:ContentValues):Long{
        val ID =sqlDB!!.insert(dbTable,"",values)
        return ID


    }
    fun query(projection: Array<String>,selection:String,selectionargs:Array<String>,sortOrder:String):Cursor{ // projection is calling which column in the data base , selection means which row you want to select, selection args means which argument
    val qb = SQLiteQueryBuilder()
        qb.tables = dbTable
        // you send the data which column you want and which row and do you want it sorted or not
        val mycursor = qb.query(sqlDB,projection,selection,selectionargs,null,null,sortOrder) // takes the database sql and the projcetion selection and finally the sort
        return mycursor


    }
    fun Delete(selection:String,selectionargs:Array<String>):Int{
        val count = sqlDB!!.delete(dbTable,selection,selectionargs)


        return count



    }
    fun Edit(values:ContentValues,selection:String,selectionargs: Array<String>):Int{
        val count = sqlDB!!.update(dbTable,values,selection,selectionargs)
        return count
    }





}