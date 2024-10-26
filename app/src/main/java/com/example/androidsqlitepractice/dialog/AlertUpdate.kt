package com.example.androidsqlitepractice.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.androidsqlitepractice.R
import com.example.androidsqlitepractice.activity.MainActivity
import com.example.androidsqlitepractice.data.DBHelper
import com.example.androidsqlitepractice.data.Product
import com.example.androidsqlitepractice.service.Updateble

class AlertUpdate(context: Context) : DialogFragment() {
    private val db = DBHelper(context)
    private var updatedle: Updateble? = null
    private lateinit var nameProductET: EditText
    private lateinit var weightProductET: EditText
    private lateinit var priceProductET: EditText
    override fun onAttach(context: Context) {
        super.onAttach(context)
        updatedle = context as Updateble?
    }
    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val product = requireArguments().getSerializable(Product::class.java.simpleName) as Product
        val builder = AlertDialog.Builder(
            requireActivity()
        )
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.update_dialog, null)
        builder.setView(dialogView)
        nameProductET = dialogView.findViewById(R.id.nameProductET)
        weightProductET = dialogView.findViewById(R.id.weightProductET)
        priceProductET = dialogView.findViewById(R.id.priceProductET)
        val listET = arrayListOf(
            nameProductET,
            weightProductET,
            priceProductET
        )
        nameProductET.setText(product.name)
        weightProductET.setText(product.weight.toString())
        priceProductET.setText(product.price.toString())
        return builder
            .setTitle("Обновление строки")
            .setMessage("Исправьте необходимые данные\n$product")
            .setPositiveButton("Изменить продукт") { dialog, which ->
                if (MainActivity.checkAllIsNotEmpty(listET)) {
                    val id = product.id
                    val name = nameProductET.text.toString()
                    val weight = weightProductET.text.toString().toDouble()
                    val price = priceProductET.text.toString().toDouble()
                    val newProduct = Product(id, name, weight, price)
                    updatedle?.update(newProduct)
                }
            }
            .setNegativeButton("Отмена", null)
            .create()
    }
}