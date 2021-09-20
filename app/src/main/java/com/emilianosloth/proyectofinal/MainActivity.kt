package com.emilianosloth.proyectofinal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    lateinit var createBT: Button
    lateinit var loginBT: Button
    lateinit var emailET: EditText
    lateinit var passET: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createBT = findViewById(R.id.mainCreateBT)
        loginBT = findViewById(R.id.mainLoginBT)
        emailET = findViewById(R.id.mainMailBT)
        passET  = findViewById(R.id.mainPassET)

        createBT.setOnClickListener {
            var intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
        }
    }

    //registrar usuario en firebase
    fun registro(view: View?){
        Firebase.auth.createUserWithEmailAndPassword(
            emailET.text.toString(),
            passET.text.toString()).addOnCompleteListener(this){
                if (it.isSuccessful){
                    Log.d("FIREBASE", "Registro Exitoso")
                }else{
                    Log.e("FIREBASE", "Registro fracaso: ${it.exception?.message}")
                }
        }
    }



    fun login(view: View?){
        Firebase.auth.signInWithEmailAndPassword(
            emailET.text.toString(),
            passET.text.toString()).addOnCompleteListener(this){
                if (it.isSuccessful){
                    Log.d("FIREBASE", "Login Exitoso")
                    var intent = Intent(this, RecipeActions::class.java)
                    startActivity(intent)
                }else{
                    Toast.makeText(this, "Incorrect User or Password", Toast.LENGTH_SHORT).show()
                }
        }
    }

    fun logout(view: View?){
        Firebase.auth.signOut()
    }

    fun verificarUsuario(){ //verificar si el ususario  sigue logeaado
        if(Firebase.auth.currentUser == null){
           Toast.makeText(this, "SIN USUARIO", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, Firebase.auth.currentUser?.email, Toast.LENGTH_SHORT).show()
        }
    }

    fun verificarUsuarioGUI(view: View?){
        verificarUsuario()
    }

    override fun onStart(){
        super.onStart()
        verificarUsuario()
        //verificar siempre que la actividad vuelva a correr
    }

}