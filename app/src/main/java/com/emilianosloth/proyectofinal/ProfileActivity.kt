package com.emilianosloth.proyectofinal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class ProfileActivity : AppCompatActivity() {

    lateinit var upBT: Button
    lateinit var viewBT: Button
    lateinit var preturnBT: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        upBT = findViewById(R.id.upBT)
        viewBT = findViewById(R.id.viewBT)
        preturnBT = findViewById(R.id.pReturnBT)

        upBT.setOnClickListener {
            var intent = Intent(this, CreateRecipeActivity::class.java)
            startActivity(intent)
        }

        viewBT.setOnClickListener {
            var intent = Intent(this, MyRecipesActivity::class.java)
            startActivity(intent)
        }

        preturnBT.setOnClickListener {
            finish()
        }
    }

}