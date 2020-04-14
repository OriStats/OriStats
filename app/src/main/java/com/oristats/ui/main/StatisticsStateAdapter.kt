package com.oristats.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.oristats.R


class StatisticsStateAdapter(fragment: Fragment) : FragmentStateAdapter(fragment){
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        var fragment : Fragment? = null
        when(position){
            0 -> fragment = Time_Chart.newInstance()
            1 -> fragment = Pie_chart.newInstance()
            2 -> fragment = DB_Test.newInstance()
        }
        return fragment!!
    }
}