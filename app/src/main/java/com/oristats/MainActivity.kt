package com.oristats

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import android.widget.Toolbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
//import com.oristats.ui.main.Linechart
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /* Old tablayout code (might be usefull as example)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
         */
        val navController = findNavController(R.id.nav_host_fragment)
        findViewById<BottomNavigationView>(R.id.bottom_nav)
            .setupWithNavController(navController)
        setupBottomNavMenu(navController)
//        setupActionBar(navController)
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun setupActionBar(navController: NavController) {

        NavigationUI.setupActionBarWithNavController(this, navController)

    }


    private fun setupBottomNavMenu(navController: NavController) {
        bottom_nav?.let {
            NavigationUI.setupWithNavController(it, navController)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.three_dots_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.Settings -> {
                val action = NavGraphDirections.actionGlobalSettings()
                NavHostFragment.findNavController(nav_host_fragment).navigate(action)
                true
            }
            R.id.Export -> {
                Toast.makeText(this, "Export Data Selected", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.Import -> {
                Toast.makeText(this, "Import Data Selected", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.Help -> {
                val action = NavGraphDirections.actionGlobalHelp()
                NavHostFragment.findNavController(nav_host_fragment).navigate(action)
                true
            }
            R.id.About -> {
                val action = NavGraphDirections.actionGlobalAbout()
                NavHostFragment.findNavController(nav_host_fragment).navigate(action)
                true
            }
            R.id.Donate -> {
                val action = NavGraphDirections.actionGlobalDonate()
                NavHostFragment.findNavController(nav_host_fragment).navigate(action)
                true
            }
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}