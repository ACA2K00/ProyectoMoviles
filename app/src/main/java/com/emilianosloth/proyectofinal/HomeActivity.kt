package com.emilianosloth.proyectofinal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {
    lateinit var logoutIB: ImageButton
    lateinit var profileIB: ImageButton
    lateinit var srchIB: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        logoutIB = findViewById(R.id.logoutIB)
        profileIB = findViewById(R.id.profileIB)
        srchIB = findViewById(R.id.searchIB)

        profileIB.setOnClickListener {
            var intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        srchIB.setOnClickListener {
            var intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
    }

    fun logout(view: View?){
        Firebase.auth.signOut()
        finish()
    }

}