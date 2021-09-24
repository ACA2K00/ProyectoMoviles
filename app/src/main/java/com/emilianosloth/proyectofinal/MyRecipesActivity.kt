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
import java.util.concurrent.Executors

class MyRecipesActivity : AppCompatActivity() {
    lateinit var img1: ImageButton
    lateinit var img2: ImageButton
    lateinit var img3: ImageButton
    lateinit var name1: TextView
    lateinit var name2: TextView
    lateinit var name3: TextView
    lateinit var nextBT: Button

    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_recipes)
        img1 = findViewById(R.id.recipeImg1IB)
        img2 = findViewById(R.id.recipeImg2IB)
        img3 = findViewById(R.id.recipeImg3IB)
        name1 = findViewById(R.id.recipeNameTV1)
        name2 = findViewById(R.id.recipeNameTV2)
        name3 = findViewById(R.id.recipeNameTV3)
        nextBT = findViewById(R.id.nextBT)

        //callRecipes-Start
        val URLs = arrayListOf<String>()
        val images = arrayOf(img1,img2,img3)
        val names = arrayListOf<String>()
        val nameTVs = arrayOf(name1,name2,name3)
        var totalRecipes = 0
        var currentSpace = 0
        var currentRecipe = 0
        var noImg = "https://jbarrios.com.ve/images/nofoto.jpg"
        db.collection("recetas")
            .whereEqualTo("Autor", Firebase.auth.currentUser?.email)
            .get()
            .addOnSuccessListener { documents ->
                for(document in documents){
                    //if(currentSpace > 2) currentSpace = 0
                    URLs.add(document.getString("Image").toString())
                    names.add(document.getString("Recipe Name").toString())
                    totalRecipes++
                    /*loadImg(images[currentSpace], URLs[totalRecipes])
                    images[currentSpace].setTag(URLs[totalRecipes]).toString()
                    nameTVs[currentSpace].text = names[totalRecipes]
                    totalRecipes++
                    currentSpace++*/

                    Log.d("FIRESTORE", "${document.id} ${document.data}")
                }
                for(i in 0..2){
                    loadImg(images[i], URLs[i])
                    images[i].setTag(URLs[i]).toString()
                    nameTVs[i].text = names[i]
                }
            }
            .addOnFailureListener{
                Log.d("FIREBASE", "EXCEPTION: ${it.message}")
                Toast.makeText(this, "ERROR: COULDN'T LOAD RECIPES", Toast.LENGTH_SHORT).show()
            }
        //callRecipes-End

        nextBT.setOnClickListener{
            currentRecipe += 3
            currentSpace = 0
            if(totalRecipes >= currentRecipe){
                for(i in currentRecipe..currentRecipe+2){
                    if (i > totalRecipes-1){
                        loadImg(images[currentSpace], noImg)
                        images[currentSpace].setTag(noImg).toString()
                        nameTVs[currentSpace].text = "No Recipe"

                        loadImg(images[currentSpace+1], noImg)
                        images[currentSpace+1].setTag(noImg).toString()
                        nameTVs[currentSpace+1].text = "No Recipe"
                    }else{
                        loadImg(images[currentSpace], URLs[i])
                        images[currentSpace].setTag(URLs[i]).toString()
                        nameTVs[currentSpace].text = names[i]
                        currentSpace++
                    }
                }
            }
        }
    }

    fun loadImg(view: ImageButton, url: String){
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

    fun showRecipe(view: View?){
        var intent = Intent(this, RecipeActivity::class.java)
        intent.putExtra("URL", view?.getTag().toString())
        //Toast.makeText(this, view?.getTag().toString(), Toast.LENGTH_SHORT).show()
        startActivity(intent)
    }

    fun goBack(view: View?){
        finish()
    }
}