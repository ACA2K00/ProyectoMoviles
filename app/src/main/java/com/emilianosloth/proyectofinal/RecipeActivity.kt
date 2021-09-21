package com.emilianosloth.proyectofinal

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.Executors

class RecipeActivity : AppCompatActivity() {
    lateinit var recipeName: TextView
    lateinit var authorName: TextView
    lateinit var recipeIngredients: TextView
    lateinit var recipeInstructions: TextView
    lateinit var recipeImage: ImageView
    lateinit var rReturnBT: Button
    lateinit var URLStr: String

    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)
        recipeName = findViewById(R.id.recipeNameTV)
        authorName = findViewById(R.id.recipeAuthorTV)
        recipeIngredients = findViewById(R.id.recipeIngredientsTV)
        recipeInstructions = findViewById(R.id.recipeDescriptionTV)
        recipeImage = findViewById(R.id.recipeImageIV)
        rReturnBT = findViewById(R.id.recipeCloseBT)
        URLStr = intent.getStringExtra("URL").toString()
        displayRecipe()
    }

    fun displayRecipe(){
        db.collection("recetas")
            .whereEqualTo("Autor", Firebase.auth.currentUser?.email)
            .whereEqualTo("Image", URLStr)
            .get()
            .addOnSuccessListener { documents ->
                for(document in documents){
                    recipeName.text = document.getString("Recipe Name").toString()
                    authorName.text = "By: " + document.getString("Autor").toString()
                    recipeIngredients.text = document.getString("Ingredients").toString()
                    recipeInstructions.text = document.getString("Instructions").toString()
                    loadImg(recipeImage, document.getString("Image").toString())
                    Log.d("FIRESTORE", "${document.id} ${document.data}")
                }
            }
            .addOnFailureListener{
                Log.d("FIREBASE", "EXCEPTION: ${it.message}")
                Toast.makeText(this, "ERROR: COULDN'T LOAD RECIPES", Toast.LENGTH_SHORT).show()
            }
    }

    fun loadImg(view: ImageView, url: String){
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())

        var image: Bitmap? = null

        // Only for Background process (can take time depending on the Internet speed)
        executor.execute {
            // Tries to get the image and post it in the ImageView
            // with the help of Handler
            try {
                val `in` = java.net.URL(url).openStream()
                image = BitmapFactory.decodeStream(`in`)
                // Only for making changes in UI
                handler.post {
                    view.setImageBitmap(image)
                }
            }
            catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun goBack(view: View?){
        finish()
    }
}