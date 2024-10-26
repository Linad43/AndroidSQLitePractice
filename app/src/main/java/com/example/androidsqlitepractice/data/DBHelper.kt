package com.example.androidsqlitepractice.data

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "PRODUCT_DATABASE"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "product_table"
        private const val KEY_ID = "id"
        private const val KEY_NAME = "name"
        private const val KEY_WEIGHT = "weight"
        private const val KEY_PRICE = "price"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE $TABLE_NAME " +
                "($KEY_ID INTEGER PRIMARY KEY, " +
                "$KEY_NAME TEXT, " +
                "$KEY_WEIGHT DOUBLE, " +
                "$KEY_PRICE DOUBLE)")
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addProduct(product: Product) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, product.id)
        contentValues.put(KEY_NAME, product.name)
        contentValues.put(KEY_WEIGHT, product.weight)
        contentValues.put(KEY_PRICE, product.price)
        db.insert(TABLE_NAME, null, contentValues)
        db.close()
    }

    @SuppressLint("Recycle", "Range")
    fun readProduct(): ArrayList<Product> {
        val productList = arrayListOf<Product>()
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val db = this.writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return productList
        }
        var productId: Int
        var productName: String
        var productWeight: Double
        var productPrice: Double
        var product: Product
        if (cursor.moveToFirst()) {
            do {
                productId = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                productName = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                productWeight = cursor.getDouble(cursor.getColumnIndex(KEY_WEIGHT))
                productPrice = cursor.getDouble(cursor.getColumnIndex(KEY_PRICE))
                product = Product(productId, productName, productWeight, productPrice)
                productList.add(product)
            } while (cursor.moveToNext())
        }
        return productList
    }

    fun updateProduct(product: Product) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, product.id)
        contentValues.put(KEY_NAME, product.name)
        contentValues.put(KEY_WEIGHT, product.weight)
        contentValues.put(KEY_PRICE, product.price)
        db.update(TABLE_NAME, contentValues, "id=" + product.id, null)
        db.close()
    }
    fun deleteProduct(product: Product) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, product.id)
        db.delete(TABLE_NAME, "id=" + product.id, null)
        db.close()
    }
}