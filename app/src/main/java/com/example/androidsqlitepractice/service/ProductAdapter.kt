package com.example.androidsqlitepractice.service

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.androidsqlitepractice.R
import com.example.androidsqlitepractice.data.Product
import java.math.RoundingMode

class ProductAdapter(context: Context, productList: ArrayList<Product>) :
    ArrayAdapter<Product>(context, R.layout.list_item, productList) {

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val product = getItem(position)
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        }
        val nameProductTV = view?.findViewById<TextView>(R.id.nameProductTV)
        val weightProduct = view?.findViewById<TextView>(R.id.weightProductTV)
        val priceProductTV = view?.findViewById<TextView>(R.id.priceProductTV)
        val sumProductTV = view?.findViewById<TextView>(R.id.sumProductTV)

        nameProductTV!!.text = /*product!!.id.toString() + ". " + */product!!.name
        weightProduct!!.text = "Вес:  " + product.weight.toString()
        priceProductTV!!.text = "Цена: " + product.price.toString()
        val sumProduct =
            (product.weight * product.price).toBigDecimal().setScale(2, RoundingMode.HALF_EVEN)
        sumProductTV!!.text = "Стоимость: $sumProduct"
        return view!!
    }
}