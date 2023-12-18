package com.borgar.foodtap

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MealAdapter(val feed: Map<String?, List<Map<String, String>>?>?): RecyclerView.Adapter<MealViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val layoutCell = layoutInflater.inflate(R.layout.meal_layout, parent, false)
        return MealViewHolder(layoutCell)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        holder.itemView.apply {
            val image = findViewById<ImageView>(R.id.food_img)
            Glide.with(this).load((feed?.get("meals") ?: error(""))[0]["strMealThumb"]).into(image)
            findViewById<ImageView>(R.id.food_img)
            findViewById<TextView>(R.id.food_title).text = (feed["meals"] ?: error(""))[0]["strMeal"]
            findViewById<TextView>(R.id.food_category).text = (feed["meals"] ?: error(""))[0]["strCategory"]
            var ingredientString = ""
            for(i in 1..20){
                if((feed["meals"] ?: error(""))[0]["strIngredient$i"] != "" && (feed["meals"] ?: error(""))[0]["strIngredient$i"] != null){
                    ingredientString += (feed["meals"] ?: error(""))[0]["strMeasure$i"] + " " + (feed["meals"] ?: error(""))[0]["strIngredient$i"] + "\n"
                } else {
                    break
                }
            }
            findViewById<TextView>(R.id.food_ingredients).text = ingredientString
            findViewById<TextView>(R.id.food_instructions).text = (feed["meals"] ?: error(""))[0]["strInstructions"]

        }
    }

    override fun getItemCount(): Int {
        return 1
    }
}

class MealViewHolder(val view: View): RecyclerView.ViewHolder(view)