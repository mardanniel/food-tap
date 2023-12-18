package com.borgar.foodtap

import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class Authentication : AppCompatActivity() {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        var loginBtn: Button = findViewById(R.id.loginbtn)
        var registerBtn: Button = findViewById(R.id.registerbtn)

        supportActionBar?.hide()

        loginBtn.setOnClickListener {
            val inflater = this.layoutInflater.inflate(R.layout.layout_login, null)
            val loginDialog = AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_launcher_foreground)
                .setView(inflater)
                .setPositiveButton("Login", DialogInterface.OnClickListener { _, _ ->
                    var email = inflater.findViewById<EditText>(R.id.loginEmail).text.toString()
                    var pass = inflater.findViewById<EditText>(R.id.loginPassword).text.toString()

                    auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                            var intent: Intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Email/Password Incorrect", Toast.LENGTH_LONG).show()
                        }
                    }
                })
                .setNegativeButton("Cancel"){
                        _, _ -> Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show()
                }
            loginDialog.show()
        }

        registerBtn.setOnClickListener {
            val inflater = this.layoutInflater.inflate(R.layout.layout_register, null)
            val registerDialog = AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_launcher_foreground)
                .setView(inflater)
                .setPositiveButton("Register", DialogInterface.OnClickListener { _, _ ->

                    var email = inflater.findViewById<EditText>(R.id.registerEmail).text.toString()

                    var pass = inflater.findViewById<EditText>(R.id.registerPassword).text.toString()

                    if(email.isEmpty() || pass.isEmpty()){
                        Toast.makeText(this, "Empty Credentials!", Toast.LENGTH_SHORT)
                    }else {
                        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this, OnCompleteListener<AuthResult> {
                            if(it.isSuccessful){
                                Toast.makeText(this, "Register Successful!", Toast.LENGTH_SHORT).show()
                            }
                            else {
                                Toast.makeText(this, "User already registered!", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                })
                .setNegativeButton("Cancel"){
                        _, _ -> Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show()
                }
            registerDialog.show()
        }
    }
}