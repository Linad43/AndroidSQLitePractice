package com.example.androidsqlitepractice.data

import java.io.Serializable

class Product(
    val id: Int,
    val name: String,
    val weight: Double,
    val price: Double
) : Serializable {
    override fun toString(): String {
        return "Наименование $name, вес $weight, цена $price."
    }
}