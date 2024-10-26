package com.example.androidsqlitepractice.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.androidsqlitepractice.R
import com.example.androidsqlitepractice.data.DBHelper
import com.example.androidsqlitepractice.data.Product
import com.example.androidsqlitepractice.dialog.AlertProduct
import com.example.androidsqlitepractice.dialog.AlertUpdate
import com.example.androidsqlitepractice.service.ProductAdapter
import com.example.androidsqlitepractice.service.Removable
import com.example.androidsqlitepractice.service.Updateble

private const val BEGIN_ID = 1
private const val IS_NOT_FIND = -1

class MainActivity : AppCompatActivity(), Removable, Updateble {
    companion object{
        fun checkAllIsNotEmpty(listET:ArrayList<EditText>): Boolean {
            var flag = true
            listET.forEach {
                if (it.text.isEmpty()) {
                    flag = false
                    it.setHint(R.string.shouldNotBeEmpty)
                }
            }
            return flag
        }
    }
    private val db = DBHelper(this)
    private var listProduct = arrayListOf<Product>()
    private lateinit var adapter: ProductAdapter
    private val listET = arrayListOf<EditText>()

    private lateinit var toolbar: Toolbar
    private lateinit var nameProductET: EditText
    private lateinit var weightProductET: EditText
    private lateinit var priceProductET: EditText
    private lateinit var saveBTN: Button
    private lateinit var listLV: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        init()

        saveBTN.setOnClickListener {
            saveBTN()
        }

        listLV.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val product = adapter.getItem(position)
                val dialog = AlertProduct()
                val args = Bundle()
                args.putSerializable(Product::class.java.simpleName, product)
                dialog.arguments = args
                dialog.show(supportFragmentManager, "custom")
            }

    }

    private fun init() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        nameProductET = findViewById(R.id.nameProductET)
        weightProductET = findViewById(R.id.weightProductET)
        priceProductET = findViewById(R.id.priceProductET)
        saveBTN = findViewById(R.id.saveBTN)
        listLV = findViewById(R.id.listLV)

        listET.add(nameProductET)
        listET.add(weightProductET)
        listET.add(priceProductET)

        updateData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.exit -> {
                finishAffinity()
            }
        }
        return true
    }

//    fun checkAllIsNotEmpty(listET:ArrayList<EditText>): Boolean {
//        var flag = true
//        listET.forEach {
//            if (it.text.isEmpty()) {
//                flag = false
//                it.setHint(R.string.shouldNotBeEmpty)
//            }
//        }
//        return flag
//    }

    private fun saveBTN() {
        if (checkAllIsNotEmpty(listET)) {
            val id = getID()
            val name = nameProductET.text.toString()
            val weight = weightProductET.text.toString().toDouble()
            val price = priceProductET.text.toString().toDouble()
            val product = Product(
                id,
                name,
                weight,
                price
            )

            db.addProduct(product)
            updateData()

            listET.forEach {
                it.text.clear()
                it.setHint(R.string.empty)
            }
        }
    }

    private fun updateData() {
        listProduct = db.readProduct()
        adapter = ProductAdapter(this, listProduct)
        listLV.adapter = adapter
    }

    private fun getID(): Int {
        var id = BEGIN_ID
        while (true) {
            if (listProduct.isNotEmpty() && listProduct.groupBy { it.id }
                    .keys.indexOf(id) != IS_NOT_FIND) {
                id++
            } else {
                break
            }
        }
        return id
    }

    override fun remove(product: Product) {
        db.deleteProduct(product)
        updateData()
    }

    override fun runDialogUpdate(product: Product) {
        val dialog = AlertUpdate(this)
        val args = Bundle()
        args.putSerializable(Product::class.java.simpleName, product)
        dialog.arguments = args
        dialog.show(supportFragmentManager, "custom")
        updateData()
    }

    override fun update(product: Product) {
        db.updateProduct(product)
        updateData()
    }
}