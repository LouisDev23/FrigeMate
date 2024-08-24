package com.example.fridgemate

data class Recipe(
    val name: String,
    val imageResId: Int,
    val description: String,
    val details: String // Ensure this matches the property used in the Intent
)
