package com.example.pin

import Nota
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.BaseAdapter

class DataHelper(contexto: Context) {
    private val DBNAME = "notas_app.db"
    private val DBVERSION = 1

    private var mAdapter: BaseAdapter? = null
    private lateinit var mDB: SQLiteDatabase

    init {
        val oHelper = MiOpenHelper(contexto, DBNAME, null, DBVERSION)
        mDB = oHelper.writableDatabase
    }

    fun setAdapter(adapter: BaseAdapter?) {
        mAdapter = adapter
    }

    fun getCursor(): Cursor? {
        val orderBy = "nombre ASC"
        return mDB.query("notas", null, null, null, null, null, orderBy)
    }

    fun selectById(id: Long): Nota? {
        val columns = arrayOf("nombre", "descripcion")
        val selection = "_id = ?"
        val args = arrayOf(id.toString())
        val limit = "1"
        val cursor = mDB.query("notas", columns, selection, args, null, null, null, limit)
        var nota: Nota? = null
        if (cursor != null && cursor.moveToFirst()) {
            nota = Nota(id, cursor.getString(0), cursor.getString(1))
        }
        cursor?.close()
        return nota
    }

    fun insert(nota: Nota): Long {
        val values = ContentValues()
        values.put("nombre", nota.nombre)
        values.put("descripcion", nota.descripcion)
        val rvalue = mDB.insert("notas", null, values)
        mAdapter?.notifyDataSetChanged()
        return rvalue
    }

    fun update(nota: Nota): Int {
        val values = ContentValues()
        values.put("nombre", nota.nombre)
        values.put("descripcion", nota.descripcion)
        val selection = "_id = ?"
        val args = arrayOf(nota.id.toString())
        val rvalue = mDB.update("notas", values, selection, args)
        mAdapter?.notifyDataSetChanged()
        return rvalue
    }

    fun deleteAll(): Int {
        val rvalue = mDB.delete("notas", "1", null)
        mAdapter?.notifyDataSetChanged()
        return rvalue
    }

    fun deleteById(id: Long): Int {
        val rvalue = mDB.delete("notas", "_id = ?", arrayOf(id.toString()))
        mAdapter?.notifyDataSetChanged()
        return rvalue
    }

    inner class MiOpenHelper(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) :
        SQLiteOpenHelper(context, name, factory, version) {

        override fun onCreate(db: SQLiteDatabase) {
            val CREATE_DB = ("CREATE TABLE notas ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "nombre TEXT,"
                    + "descripcion TEXT)")
            db.execSQL(CREATE_DB)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            if (oldVersion < 2) {
                // Aquí puedes realizar modificaciones si es necesario en futuras actualizaciones
            }
        }
    }
}
