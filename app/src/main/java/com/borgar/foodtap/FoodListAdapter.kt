package com.borgar.foodtap

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.QuerySnapshot

class FoodListAdapter(val data: QuerySnapshot, val cellClickListener: CellClickListener) : RecyclerView.Adapter<FoodListAdapter.FoodListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodListViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val layoutCell = layoutInflater.inflate(R.layout.meal_row, parent, false)
        return FoodListViewHolder(layoutCell)
    }

    override fun onBindViewHolder(holder: FoodListViewHolder, position: Int) {
        holder.itemView.apply {
            findViewById<TextView>(R.id.meal_row_name).text = data.documents[position].get("foodTitle").toString()
        }
        holder.itemView.setOnClickListener {
            cellClickListener.onCellClickListener(
                    data.documents[position].get("foodTitle").toString(),
                    data.documents[position].get("foodCategory").toString(),
                    data.documents[position].get("foodIngredients").toString(),
                    data.documents[position].get("foodInstructions").toString()
            )
        }
        holder.view.findViewById<ImageButton>(R.id.meal_delete).setOnClickListener {
            cellClickListener.onDeleteCellListener(
                    data.documents[position].get("foodTitle").toString(),
                    position
            )
        }
    }

    override fun getItemCount(): Int {
        return data.documents.size
    }

    inner class FoodListViewHolder(val view: View): RecyclerView.ViewHolder(view)
}


