package com.example.fridgemate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecipeAdapter(
    private val recipes: List<Recipe>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(recipe: Recipe)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recipe_item, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.bind(recipe)
    }

    override fun getItemCount(): Int = recipes.size

    inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.recipe_image)

        fun bind(recipe: Recipe) {
            imageView.setImageResource(recipe.imageResId)
            itemView.setOnClickListener {
                onItemClickListener.onItemClick(recipe)
            }
        }
    }
}
