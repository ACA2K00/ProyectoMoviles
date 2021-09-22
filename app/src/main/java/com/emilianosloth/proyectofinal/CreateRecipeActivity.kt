package com.emilianosloth.proyectofinal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CreateRecipeActivity : AppCompatActivity() {
    lateinit var recipeName : EditText
    lateinit var recipeIngredients : EditText
    lateinit var recipeImage : EditText
    lateinit var recipeInstructions : EditText
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_recipe)

        recipeName = findViewById(R.id.recipeNameET)
        recipeIngredients = findViewById(R.id.recipeIngredientsET)
        recipeImage = findViewById(R.id.recipeImageET)
        recipeInstructions = findViewById(R.id.recipeInstructionsET)

    }

    fun enlist(inStr: String): String {
        val inlist = inStr.split(",")
        return inlist.joinToString(separator = "\n")
    }

    fun goBack(view: View?){
        finish()
    }

    fun registrarReceta(view : View?){
        val receta = hashMapOf(
            "Autor" to Firebase.auth.currentUser?.email,
            "Recipe Name" to recipeName.text.toString(),
            "Ingredients" to enlist(recipeIngredients.text.toString()),
            "Image" to recipeImage.text.toString(),
            "Instructions" to recipeInstructions.text.toString()
        )

        db.collection("recetas").add(receta)
            .addOnSuccessListener {
                Log.d("FIREBASE", "ID: ${it.id}")
                Toast.makeText(this, "Recipe Uploaded Correctly", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Log.d("FIREBASE", "EXCEPTION: ${it.message}")
                Toast.makeText(this, "ERROR: COULDN'T UPLOAD RECIPE", Toast.LENGTH_SHORT).show()
            }
    }
}