package com.borgar.foodtap

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NavUtils
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.w3c.dom.Text

class FoodList : AppCompatActivity(), CellClickListener {

    private lateinit var rvfoodlist: RecyclerView
    private var auth = FirebaseAuth.getInstance()
    private var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_list)

        supportActionBar?.title = "Your Food List"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(0xffb55400.toInt()))

        rvfoodlist = findViewById(R.id.rv_food_list)
        rvfoodlist.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        db.collection(auth.uid.toString()).get().addOnSuccessListener { result ->
            rvfoodlist.adapter = FoodListAdapter(result, this)
        }
    }

    override fun onCellClickListener(foodName: String, foodCategory: String, foodIngredients: String, foodInstruction: String) {
        val inflate = this.layoutInflater.inflate(R.layout.meal_dialog, null)
        inflate.findViewById<TextView>(R.id.md_food_title).text = foodName
        inflate.findViewById<TextView>(R.id.md_food_category).text = foodCategory
        inflate.findViewById<TextView>(R.id.md_food_ingredients).text = foodIngredients
        inflate.findViewById<TextView>(R.id.md_food_instructions).text = foodInstruction
        val foodDialog = AlertDialog.Builder(this)
                .setView(inflate)
                .setNegativeButton("Close"){ _,_ -> }
        foodDialog.show()
    }

    override fun onDeleteCellListener(foodName: String, position: Int) {
        db.collection(auth.uid.toString()).document(foodName).delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "Recipe Deleted Successfully!", Toast.LENGTH_SHORT).show()
                    db.collection(auth.uid.toString()).get().addOnSuccessListener { result ->
                        rvfoodlist.adapter = FoodListAdapter(result, this)
                    }
                }
                .addOnFailureListener { Toast.makeText(this, "Error deleting recipe!", Toast.LENGTH_SHORT).show()  }
    }
}

interface CellClickListener {
    fun onCellClickListener(foodName: String, foodCategory: String, foodIngredients: String, foodInstruction: String)
    fun onDeleteCellListener(foodName: String, position: Int)
}


