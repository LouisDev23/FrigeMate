package com.example.fridgemate

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.fridgemate.R

class FridgeInventory : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fridge_inventory)
        val backButton = findViewById<ImageView>(R.id.back_button)
        backButton.setOnClickListener { v: View? -> finish() }
        val intent = intent
        val fridgeInventory =
            intent.getSerializableExtra("fridgeInventory") as HashMap<String, Int>?
        val fridgeItemsList = findViewById<ListView>(R.id.fridge_items_list)
        val itemsList: MutableList<String> = ArrayList()
        for ((key, value) in fridgeInventory!!) {
            val item = "$key ($value)"
            itemsList.add(item)
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, itemsList)
        fridgeItemsList.adapter = adapter
    }
}
