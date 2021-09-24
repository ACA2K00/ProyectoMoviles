package com.emilianosloth.proyectofinal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CreateRecipeActivity : AppCompatActivity() {
    lateinit var recipeName : EditText
    lateinit var recipeIngredients : EditText
    lateinit var recipeImage : EditText
    lateinit var recipeInstructions : EditText
    lateinit var rUpBT: Button
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_recipe)

        recipeName = findViewById(R.id.recipeNameET)
        recipeIngredients = findViewById(R.id.recipeIngredientsET)
        recipeImage = findViewById(R.id.recipeImageET)
        recipeInstructions = findViewById(R.id.recipeInstructionsET)
        rUpBT = findViewById(R.id.recipeUploadBT)

        //Spinner|Lista desplegable
        val languages = resources.getStringArray(R.array.Categories)
        var sp: String
        //access spinner
        val spinner = findViewById<Spinner>(R.id.spinner)
        if (spinner != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, languages)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    sp = languages[position]
                    spinner.setTag(sp).toString()
                    Log.i("Pos", sp)
                    Log.i("Sp TAG", spinner.getTag().toString())
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }

        rUpBT.setOnClickListener{
            val receta = hashMapOf(
                "Autor" to Firebase.auth.currentUser?.email,
                "Recipe Name" to recipeName.text.toString(),
                "Ingredients" to enlist(recipeIngredients.text.toString()),
                "Image" to recipeImage.text.toString(),
                "Instructions" to recipeInstructions.text.toString(),
                "Category" to spinner.getTag().toString()
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
            finish()
        }
    }

    fun enlist(inStr: String): String {
        val inlist = inStr.split(",")
        return inlist.joinToString(separator = "\n")
    }

    fun goBack(view: View?){
        finish()
    }

}