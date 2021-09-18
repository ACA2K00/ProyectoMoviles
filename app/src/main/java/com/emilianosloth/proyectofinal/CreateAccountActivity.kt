package com.emilianosloth.proyectofinal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class CreateAccountActivity : AppCompatActivity() {

    lateinit var createButton: Button
    lateinit var emailET: EditText
    lateinit var passET: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        createButton = findViewById(R.id.CACreateBT)
        emailET = findViewById(R.id.CAEmailET)
        passET = findViewById(R.id.CAPassET)
    }

    fun registro(view: View?){
        Firebase.auth.createUserWithEmailAndPassword(
            emailET.text.toString(),
            passET.text.toString()).addOnCompleteListener(this){
            if (it.isSuccessful){
                Toast.makeText(this, "Registro Exitoso", Toast.LENGTH_SHORT).show()
                Log.d("FIREBASE", "Registro Exitoso")
                finish()
            }else{
                Toast.makeText(this, "Registro fracaso: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                Log.e("FIREBASE", "Registro fracaso: ${it.exception?.message}")
            }
        }
    }

}