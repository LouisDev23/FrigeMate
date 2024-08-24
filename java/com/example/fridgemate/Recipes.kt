package com.example.fridgemate

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class Recipes : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var recipeList: List<Recipe>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipes)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize the recipe list with image resource IDs and descriptions
        recipeList = listOf(
            Recipe("Spaghetti Bolognese", R.mipmap.res1_foreground, "A classic Italian pasta dish with meat sauce.", "Recipe here1"),
            Recipe("Chicken Curry", R.mipmap.res2_foreground, "A spicy and flavorful chicken curry.", "Recipe here 2"),
            Recipe("Vegetable Stir Fry", R.mipmap.res3_foreground, "A quick and healthy vegetable stir fry.", "Recipe here 3")
        )

        recipeAdapter = RecipeAdapter(recipeList, object : RecipeAdapter.OnItemClickListener {
            override fun onItemClick(recipe: Recipe) {
                val intent = Intent(this@Recipes, RecipeDetailActivity::class.java).apply {
                    putExtra("RECIPE_TITLE", recipe.name)
                    putExtra("RECIPE_IMAGE_RES_ID", recipe.imageResId)
                    putExtra("RECIPE_DESCRIPTION", recipe.description)
                    putExtra("RECIPE_DETAILS", recipe.details) // Ensure `details` is the correct property
                }
                startActivity(intent)
            }
        })

        recyclerView.adapter = recipeAdapter


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
