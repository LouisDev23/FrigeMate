package com.example.fridgemate

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class RecipeDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

        // Find the views
        val recipeTitle = findViewById<TextView>(R.id.recipe_title)
        val recipeImage = findViewById<ImageView>(R.id.recipe_image)
        val recipeDescription = findViewById<TextView>(R.id.recipe_description)

        // Retrieve the Intent and its extras
        val intent = intent
        val title = intent.getStringExtra("RECIPE_TITLE")
        val imageResId = intent.getIntExtra("RECIPE_IMAGE_RES_ID", R.drawable.soup) // Ensure default_image exists
        val description = intent.getStringExtra("RECIPE_DESCRIPTION")

        // Set the data on the views
        recipeTitle.text = title ?: "No title provided"
        recipeImage.setImageResource(imageResId)
        recipeDescription.text = description ?: "No description provided"

        // Setup Bottom Navigation View
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_scan -> {
                    // Open the Scan Inventory activity
                    startActivity(Intent(this, ScanInvertory::class.java))
                    true
                }
                R.id.nav_fridge -> {
                    // Open the Fridge Inventory activity
                    startActivity(Intent(this, FridgeInventory::class.java))
                    true
                }
                R.id.nav_store -> {
                    // Open the Store Locator activity
                    startActivity(Intent(this, StoreLocator::class.java))
                    true
                }
                R.id.nav_res -> {
                    // Open the Recipes activity
                    startActivity(Intent(this, Recipes::class.java))
                    true
                }
                else -> false
            }
        }
    }
}
