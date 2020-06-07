package com.oristats

import android.os.Bundle
import android.os.SystemClock
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
//import com.oristats.ui.main.Linechart
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.oristats.db.DB_ViewModel
import com.oristats.ui.main.Stopwatch_ViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var stopwatch_ViewModel: Stopwatch_ViewModel
    lateinit var db_ViewModel: DB_ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        stopwatch_ViewModel = ViewModelProvider(this).get(Stopwatch_ViewModel::class.java)
        db_ViewModel = ViewModelProvider(this).get(DB_ViewModel::class.java)

        RebootDetector() // If necessary, it calls automatically Correct_Stopwatch_ViewModel().

        val navController = findNavController(R.id.nav_host_fragment)
        findViewById<BottomNavigationView>(R.id.bottom_nav)
            .setupWithNavController(navController)
        setupBottomNavMenu(navController)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

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
         /*   R.id.Settings -> {
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
            }*/
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

    private fun RebootDetector() { // Reacts if there was a reboot since last time the app was used. See Stopwatch_ViewModel.kt : LastBootCompare, etc. Stopwatch.kt : MillisForDB()
        val RealLastBoot : Long = System.currentTimeMillis()-SystemClock.elapsedRealtime()
        val RealLastBootCompare : Long = RealLastBoot%3600000

        if(stopwatch_ViewModel.getLastBootCompare()>0){
            if (stopwatch_ViewModel.getLastBootCompare()<RealLastBootCompare-5000 || stopwatch_ViewModel.getLastBootCompare()>RealLastBootCompare+5000){
                //Toast.makeText(this, "Rebooted", Toast.LENGTH_SHORT).show()
                if (!stopwatch_ViewModel.getStart()) {  // If it was "resetted", no need to correct SystemClock.elapsedRealtime().
                    Correct_Stopwatch_ViewModel(RealLastBoot-stopwatch_ViewModel.getLastBoot())
                }
            } /*else {
                Toast.makeText(this, "Not Rebooted", Toast.LENGTH_SHORT).show()
            }*/
        } /*else {
            Toast.makeText(this, "First Time Using the App", Toast.LENGTH_SHORT).show()
        }*/

        stopwatch_ViewModel.setLastBoot(RealLastBoot)
        stopwatch_ViewModel.setLastBootCompare(RealLastBootCompare)
    }
    private fun Correct_Stopwatch_ViewModel(difference : Long) { // NOTE: Only in this case we will use currentTimeMillis() to measure time intervals, because there is no other way.
        // For Stopwatch Display [multi-reboot proof]
        stopwatch_ViewModel.setLastPlay(stopwatch_ViewModel.getLastPlay()-difference)
        stopwatch_ViewModel.setLastPause(stopwatch_ViewModel.getLastPause()-difference)
        stopwatch_ViewModel.setLastWorkBase(stopwatch_ViewModel.getLastWorkBase()-difference)
        stopwatch_ViewModel.setLastBreakBase(stopwatch_ViewModel.getLastBreakBase()-difference)
        // For DB [multi-reboot proof]
        stopwatch_ViewModel.setRebootCorrection(stopwatch_ViewModel.getRebootCorrection()+difference)
    }

}