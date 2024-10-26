package com.example.androidsqlitepractice.service

import com.example.androidsqlitepractice.data.Product

interface Updateble {
    fun runDialogUpdate(product: Product)
    fun update(product: Product)
}
