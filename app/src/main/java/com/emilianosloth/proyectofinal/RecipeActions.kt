package com.emilianosloth.proyectofinal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class RecipeActions : AppCompatActivity() {

    lateinit var upBT: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_actions)
        upBT = findViewById(R.id.upBT)

        upBT.setOnClickListener {
            var intent = Intent(this, CreateRecipeActivity::class.java)
            startActivity(intent)
        }
    }
}