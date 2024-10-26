package com.example.androidsqlitepractice.service

import com.example.androidsqlitepractice.data.Product

interface Removable {
    fun remove(product: Product)
}