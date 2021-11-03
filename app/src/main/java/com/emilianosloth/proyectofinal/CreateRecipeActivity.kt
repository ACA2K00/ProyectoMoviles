package com.emilianosloth.proyectofinal

import android.R.attr
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.R.attr.bitmap
import com.google.common.io.ByteArrayDataOutput
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream


class CreateRecipeActivity : AppCompatActivity() {
    lateinit var recipeName : EditText
    lateinit var recipeIngredients : EditText
    lateinit var recipeImage : EditText
    lateinit var recipeInstructions : EditText
    lateinit var rUpBT: Button
    lateinit var idRecipe: String
    var isEditing: Boolean = false
    val db = Firebase.firestore
    val storage = Firebase.storage

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->

        //obtener un tumbnail

        val image = result.data?.extras?.get("data") as Bitmap


        val storageReference = storage.reference
        val imageReference = storageReference.child(recipeName.text.toString()+".jpg")
        val imagesReference = storageReference.child("images/"+recipeName.text.toString()+".jpg")

        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()


        var uploadTask = imagesReference.putBytes(data)
        uploadTask.addOnFailureListener{
            Log.wtf("Falla", "fallo")
        }.addOnSuccessListener { taskSnapshot ->
            recipeImage.setText(recipeName.text.toString()+".jpg")
            Log.wtf("Success", "Logrado")
        }



    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_recipe)

        recipeName = findViewById(R.id.recipeNameET)
        recipeIngredients = findViewById(R.id.recipeIngredientsET)
        recipeImage = findViewById(R.id.recipeImageET)
        recipeInstructions = findViewById(R.id.recipeInstructionsET)
        rUpBT = findViewById(R.id.recipeUploadBT)

        if (intent.getStringExtra("NAME") != null && intent.getStringExtra("AUTHOR") != null){
            isEditing = true
            idRecipe = intent.getStringExtra("ID").toString()
            db.collection("recetas")
                .whereEqualTo("Recipe Name", intent.getStringExtra("NAME"))
                .whereEqualTo("Autor", intent.getStringExtra("AUTHOR"))
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents){
                        recipeName.setText(document.getString("Recipe Name").toString())
                        recipeIngredients.setText(document.getString("Ingredients").toString())
                        recipeInstructions.setText(document.getString("Instructions").toString())
                        recipeImage.setText(document.getString("Image").toString())
                    }
                }
        }

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
            if(recipeName.text.toString() == "" || recipeIngredients.text.toString() == "" ||
                recipeImage.text.toString() == "" || recipeInstructions.text.toString() == ""){
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }else{
                val receta = hashMapOf(
                    "Autor" to Firebase.auth.currentUser?.email,
                    "Recipe Name" to recipeName.text.toString(),
                    "Ingredients" to enlist(recipeIngredients.text.toString()),
                    "Image" to recipeImage.text.toString(),
                    "Instructions" to recipeInstructions.text.toString(),
                    "Category" to spinner.getTag().toString()
                )
                if(isEditing){
                    db.collection("recetas").document(idRecipe).update(receta as Map<String, Any>)
                }else{
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


                finish()
            }
        }
    }

    fun enlist(inStr: String): String {
        val inlist = inStr.split(",")
        return inlist.joinToString(separator = "\n")
    }

    fun takePicture(view: View?){
        val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        resultLauncher.launch(takePhotoIntent)
    }


    fun goBack(view: View?){
        finish()
    }

}