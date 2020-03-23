package com.oristats

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import com.oristats.ui.main.SectionsPagerAdapter
//import com.oristats.ui.main.Linechart
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        /* Old tablayout code (might be usefull as example)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
         */
        val navController = findNavController(R.id.nav_host_fragment)
        findViewById<BottomNavigationView>(R.id.bottom_nav)
            .setupWithNavController(navController)
        setupBottomNavMenu(navController)
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
                Toast.makeText(this, "Settings Selected", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this, "Help Selected", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.About -> {
                Toast.makeText(this, "About Selected", Toast.LENGTH_LONG).show()
                true
            }
            R.id.Donate -> {
                Toast.makeText(this, "Donate Selected", Toast.LENGTH_LONG).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}