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
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.Executors

class SearchRecipes : AppCompatActivity() {
    lateinit var img1: ImageButton
    lateinit var img2: ImageButton
    lateinit var img3: ImageButton
    lateinit var name1: TextView
    lateinit var name2: TextView
    lateinit var name3: TextView
    lateinit var nextBT: Button
    lateinit var prevBT: Button
    lateinit var catSTR: String

    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_recipes)
        img1 = findViewById(R.id.sRecipeImg1IB)
        img2 = findViewById(R.id.sRecipeImg2IB)
        img3 = findViewById(R.id.sRecipeImg3IB)
        name1 = findViewById(R.id.sRecipeNameTV1)
        name2 = findViewById(R.id.sRecipeNameTV2)
        name3 = findViewById(R.id.sRecipeNameTV3)
        nextBT = findViewById(R.id.sNextBT)
        prevBT = findViewById(R.id.sPrevBT)
        catSTR = intent.getStringExtra("Cat").toString()

        //callRecipes-Start
        val URLs = arrayListOf<String>()
        val images = arrayOf(img1,img2,img3)
        val names = arrayListOf<String>()
        val nameTVs = arrayOf(name1,name2,name3)
        var totalRecipes = 0
        var currentSpace: Int
        var currentRecipe = 0
        var noImg = "https://jbarrios.com.ve/images/nofoto.jpg"
        db.collection("recetas")
            .whereEqualTo("Category", catSTR)
            .get()
            .addOnSuccessListener { documents ->
                for(document in documents){
                    URLs.add(document.getString("Image").toString())
                    names.add(document.getString("Recipe Name").toString())
                    totalRecipes++
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

        //NextPage-Start
        nextBT.setOnClickListener{
            currentSpace = 0
            currentRecipe += 3
            Log.i("cRecipe", currentRecipe.toString())
            if(totalRecipes > currentRecipe){
                for(i in currentRecipe..currentRecipe+2){
                    if (i >= totalRecipes){ // i = 4 -> pos 5, totalRecipes = 4
                        loadImg(images[currentSpace], noImg)
                        images[currentSpace].setTag(noImg).toString()
                        nameTVs[currentSpace].text = "No Recipe"
                        currentSpace++
                    }else {
                        loadImg(images[currentSpace], URLs[i])
                        images[currentSpace].setTag(URLs[i]).toString()
                        nameTVs[currentSpace].text = names[i]
                        currentSpace++
                    }
                }
            }else{
                currentRecipe-=3
            }
        }
        //NextPage-End

        //PrevPage-Start
        prevBT.setOnClickListener{
            if (currentRecipe!=0){
                currentSpace = 0
                currentRecipe -= 3
                Log.i("cRecipe", currentRecipe.toString())
                if(totalRecipes > currentRecipe){
                    for(i in currentRecipe..currentRecipe+2){
                        if (i >= totalRecipes){ // i = 4 -> pos 5, totalRecipes = 4
                            loadImg(images[currentSpace], noImg)
                            images[currentSpace].setTag(noImg).toString()
                            nameTVs[currentSpace].text = "No Recipe"
                            currentSpace++
                        }else {
                            loadImg(images[currentSpace], URLs[i])
                            images[currentSpace].setTag(URLs[i]).toString()
                            nameTVs[currentSpace].text = names[i]
                            currentSpace++
                        }
                    }
                }
            }
        }
        //PrevPage-End
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
        if (view?.getTag().toString() != "https://jbarrios.com.ve/images/nofoto.jpg"){
            var intent = Intent(this, RecipeActivity::class.java)
            intent.putExtra("URL", view?.getTag().toString())
            startActivity(intent)
        }
    }

    fun goBack(view: View?){
        finish()
    }
}