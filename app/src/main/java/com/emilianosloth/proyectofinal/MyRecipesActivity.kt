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

class MyRecipesActivity : AppCompatActivity() {
    lateinit var img1: ImageButton
    lateinit var img2: ImageButton
    lateinit var img3: ImageButton
    lateinit var name1: TextView
    lateinit var name2: TextView
    lateinit var name3: TextView

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

        callRecipes()
    }

    fun callRecipes(){
        val URLs = arrayOf("","","")
        val images = arrayOf(img1,img2,img3)
        val names = arrayOf("","","")
        val nameTVs = arrayOf(name1,name2,name3)
        var i = 0
        db.collection("recetas")
            .whereEqualTo("Autor", Firebase.auth.currentUser?.email)
            .get()
            .addOnSuccessListener { documents ->
                for(document in documents){
                    URLs[i] = document.getString("Image").toString()
                    names[i] = document.getString("Recipe Name").toString()
                    loadImg(images[i], URLs[i])
                    nameTVs[i].text = names[i]
                    i++

                    Log.d("FIRESTORE", "${document.id} ${document.data}")
                }
            }
            .addOnFailureListener{
                Log.d("FIREBASE", "EXCEPTION: ${it.message}")
                Toast.makeText(this, "ERROR: COULDN'T LOAD RECIPES", Toast.LENGTH_SHORT).show()
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

    fun goBack(view: View?){
        finish()
    }
}