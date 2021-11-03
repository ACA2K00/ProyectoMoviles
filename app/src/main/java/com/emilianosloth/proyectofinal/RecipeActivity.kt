package com.emilianosloth.proyectofinal

import android.content.Intent
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
import com.google.firebase.storage.ktx.storage
import java.util.concurrent.Executors

class RecipeActivity : AppCompatActivity() {
    lateinit var recipeName: TextView
    lateinit var authorName: TextView
    lateinit var recipeIngredients: TextView
    lateinit var recipeInstructions: TextView
    lateinit var recipeCategory: TextView
    lateinit var recipeImage: ImageView
    lateinit var rReturnBT: Button
    lateinit var recRecipe: String
    lateinit var recAuthor: String
    lateinit var idRecipe: String

    val db = Firebase.firestore
    var storageReference = Firebase.storage.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)
        recipeName = findViewById(R.id.recipeNameTV)
        authorName = findViewById(R.id.recipeAuthorTV)
        recipeIngredients = findViewById(R.id.recipeIngredientsTV)
        recipeInstructions = findViewById(R.id.recipeDescriptionTV)
        recipeCategory = findViewById(R.id.categoryTV)
        recipeImage = findViewById(R.id.recipeImageIV)
        rReturnBT = findViewById(R.id.recipeCloseBT)
        recRecipe = intent.getStringExtra("name").toString()
        recAuthor = intent.getStringExtra("author").toString()
        displayRecipe()
    }

    fun displayRecipe(){
        db.collection("recetas")
            .whereEqualTo("Recipe Name", recRecipe)
            .whereEqualTo("Autor", recAuthor)
            .get()
            .addOnSuccessListener { documents ->
                for(document in documents){
                    recipeName.text = document.getString("Recipe Name").toString()
                    authorName.text = "By: " + document.getString("Autor").toString()
                    recipeIngredients.text = document.getString("Ingredients").toString()
                    recipeInstructions.text = document.getString("Instructions").toString()
                    recipeCategory.text = document.getString("Category").toString()
                    var imageString = document.getString("Image").toString()
                    idRecipe = document.id

                    if(imageString.substring(imageString.length-4) == ".jpg"){
                        var imageReference = storageReference.child("images/"+imageString)
                        loadImg(recipeImage, "https://firebasestorage.googleapis.com/v0/b/proyectofinalmoviles-e98e6.appspot.com/o/images%2Fmolletes.jpg?alt=media&token=171c0d48-f92d-446f-b50b-7a258411d7a8")
                    }else{
                        loadImg(recipeImage, document.getString("Image").toString())
                    }
//                    loadImg(recipeImage, document.getString("Image").toString())
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

    fun edit(view: View?){
        var intent = Intent(this, CreateRecipeActivity::class.java)
        intent.putExtra("ID", idRecipe)
        intent.putExtra("NAME", recRecipe)
        intent.putExtra("AUTHOR", recAuthor)
        startActivity(intent)
    }

    fun delete(view: View?){
        db.collection("recetas").document(idRecipe).delete()
        finish()
    }

}