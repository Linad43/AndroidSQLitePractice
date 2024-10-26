package com.example.androidsqlitepractice.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.androidsqlitepractice.data.Product
import com.example.androidsqlitepractice.service.Removable
import com.example.androidsqlitepractice.service.Updateble

class AlertProduct : DialogFragment() {
    private var updatedle: Updateble? = null
    private var removable: Removable? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        removable = context as Removable?
        updatedle = context as Updateble?
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val product = requireArguments().getSerializable(Product::class.java.simpleName)
        val builder = AlertDialog.Builder(
            requireActivity()
        )

        return builder
            .setTitle("Внимание")
            .setMessage("Выбран следующий продукт\n$product")
            .setPositiveButton("Изменить продукт") { dialog, which ->
                updatedle?.runDialogUpdate(product as Product)
            }
            .setNeutralButton("Удалить продукт") { dialog, which ->
                removable?.remove(product as Product)
            }.setNegativeButton("Отмена", null)
            .create()
    }
}