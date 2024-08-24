package com.example.fridgemate

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button1: Button = findViewById(R.id.button1)
        val button2: Button = findViewById(R.id.button2)
        val button3: Button = findViewById(R.id.button3)
        val button4: Button = findViewById(R.id.button4)

        button1.setOnClickListener {
            val intent = Intent(this, ScanInvertory::class.java)
            startActivity(intent)
        }

        button2.setOnClickListener {
            val intent = Intent(this, FridgeInventory::class.java)
            startActivity(intent)
        }

        button3.setOnClickListener {
            val intent = Intent(this, StoreLocator::class.java)
            startActivity(intent)
        }

        button4.setOnClickListener {
            val intent = Intent(this, Recipes::class.java)
            startActivity(intent)
        }
    }
}
