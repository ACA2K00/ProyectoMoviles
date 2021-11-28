package com.emilianosloth.proyectofinal

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class pictureChangeActivity : AppCompatActivity() {

    lateinit var newURL : EditText
    lateinit var change : Button

    lateinit var buscarImagen: ActivityResultLauncher<String>
    lateinit var imagenUri: Uri
    var imagenEmpty : Boolean = true
    lateinit var imagen: ImageView
    lateinit var registrarImagenBT : Button

    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture_change)

        newURL = findViewById(R.id.newURLET)
        change = findViewById(R.id.changePictureURL)
        imagen = findViewById(R.id.previeImageView)
        registrarImagenBT = findViewById(R.id.choosePicBT)

        change.setOnClickListener{
            if(newURL.text.toString() == ""){
                Toast.makeText(this, "Falta un URL", Toast.LENGTH_SHORT).show()
            }else{
                changePicURL()
            }
        }

        buscarImagen = registerForActivityResult(ActivityResultContracts.GetContent()) {
            if(it != null) {
                imagenUri = it
                imagen.setImageURI(imagenUri)
                imagenEmpty = false
            }
        }

    }

    fun seleccionarImagen(v: View) {
        buscarImagen.launch("image/*")
    }



    fun registrarImagen(v: View) {
        val userIdentifier = Firebase.auth.currentUser?.email.toString()
        val storageReference = FirebaseStorage.getInstance().getReference("imagenesPerfiles/$userIdentifier")
        storageReference.putFile(imagenUri)
            .addOnSuccessListener {
                Log.d("FIREBASE Agregar imagen", "Correctamente cargado")
            }
            .addOnFailureListener {
                Log.e("FIREBASE Agregar imagen", "exception: ${it.message}")
            }
    }


    fun changePicURL(){
        val user = FirebaseAuth.getInstance().currentUser
        val db = Firebase.firestore

        db.collection("usuarios")
            .whereEqualTo("id", user!!.email)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents){
                    val id = document.id
                    db.collection("usuarios")
                        .document(id)
                        .update("imageURL", newURL.text.toString())
                }
                finish()
            }
            .addOnFailureListener{
                Log.d("FIREBASE", "EXCEPTION: ${it.message}")
                Toast.makeText(this, "ERROR: COULDN'T LOAD NEW NAME", Toast.LENGTH_SHORT).show()
            }

    }


}

