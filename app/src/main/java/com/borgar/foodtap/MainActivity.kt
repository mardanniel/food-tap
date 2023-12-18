package com.borgar.foodtap

import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var rv: RecyclerView
    private val url = "https://www.themealdb.com/api/json/v1/1/random.php"
    private val request = Request.Builder().url(url).build()
    private val client = OkHttpClient()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.setBackgroundDrawable(ColorDrawable(0xffb55400.toInt()))

        rv = findViewById(R.id.rv_view)
        rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)

        fetchJSON()

        val saveBtn = findViewById<Button>(R.id.btn_save)
        val nextBtn = findViewById<Button>(R.id.btn_next)

        saveBtn.setOnClickListener {
            var foodTitle = findViewById<TextView>(R.id.food_title).text.toString()
            var foodCategory = findViewById<TextView>(R.id.food_category).text.toString()
            var foodIngredients = findViewById<TextView>(R.id.food_ingredients).text.toString()
            var foodInstructions = findViewById<TextView>(R.id.food_instructions).text.toString()
            var map = mutableMapOf<String, String>()
            map.put("foodTitle", foodTitle)
            map.put("foodCategory", foodCategory)
            map.put("foodIngredients", foodIngredients)
            map.put("foodInstructions", foodInstructions)

            db.collection(auth.uid.toString()).document(foodTitle).set(map).addOnCompleteListener {
                if(it.isSuccessful){
                    fetchJSON()
                    Toast.makeText(this, "Recipe added to Collection!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Recipe already added to Collection!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        nextBtn.setOnClickListener {
            fetchJSON()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_layout, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logoutBtn){
            val logoutDialog = AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", DialogInterface.OnClickListener { _, _ ->
                        FirebaseAuth.getInstance().signOut()
                        Toast.makeText(this, "Logged Out Successfully", Toast.LENGTH_SHORT)
                        var intent = Intent(this, Authentication::class.java)
                        startActivity(intent)
                        finish()
                    })
                    .setNegativeButton("Cancel"){ _, _ -> }
            logoutDialog.show()
        }
        return true
    }

    fun fetchJSON() {
        val loading = LoadingDialog(this)
        loading.startLoadingDialog()
        val gson = GsonBuilder().create()
        val type = object : TypeToken<Map<String?, List<Map<String, String>>?>?>() {}.type

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Fetching Failed!")
                fetchJSON()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (it.isSuccessful) {
                        val body = response?.body?.string()
                        val feed = gson.fromJson<Map<String?, List<Map<String, String>>?>?>(body, type)
                        runOnUiThread {
                            rv.adapter = MealAdapter(feed)
                            loading.run { dismissLoadingDialog() }
                        }
                    } else {
                        fetchJSON()
                    }
                }
            }
        })
    }

    fun showFoodList(view: View) {
        var intent = Intent(this, FoodList::class.java)
        startActivity(intent)
    }





}