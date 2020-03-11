package com.oristats

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import com.oristats.ui.main.SectionsPagerAdapter
import android.widget.Chronometer//chronometer imports
import android.view.View
import android.widget.Toast


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        //##############################
        //Stopwatch implementation code
        //#############################
        val meter = findViewById<Chronometer>(R.id.c_meter)//gets chronometer from the XML using the proper id
        val btn = findViewById<Button>(R.id.btStart)//gets the button from the XML

        btn?.setOnClickListener(object : View.OnClickListener {

            var isWorking = false

            override fun onClick(v: View) {
                if (!isWorking) {
                    meter.start()
                    //print("entrou")
                    isWorking = true
                } else {
                    meter.stop()
                    isWorking = false
                }

                btn.setText(if (isWorking) R.string.start else R.string.stop)

                Toast.makeText(this@MainActivity, getString(
                    if (isWorking)
                        R.string.working
                    else
                        R.string.stopped),
                    Toast.LENGTH_SHORT).show()
            }
        })


        //This part of the code refers to the button
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }
}