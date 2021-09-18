package com.emilianosloth.proyectofinal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.emilianosloth.proyectofinal.fragments.AccountFragment
import com.emilianosloth.proyectofinal.fragments.HomeFragment
import com.emilianosloth.proyectofinal.fragments.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    lateinit var bottom_navigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        bottom_navigation = findViewById(R.id.bottom_navigation)
        val homeFragment = HomeFragment()
        val searchFragment = SearchFragment()
        val accountFragment = AccountFragment()

        makeCurrentFragment(homeFragment)


        bottom_navigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.ic_home -> makeCurrentFragment(homeFragment)
                R.id.ic_account -> makeCurrentFragment(accountFragment)
                R.id.ic_search -> makeCurrentFragment(searchFragment)
            }
            true
        }

    }

    private fun makeCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }
}