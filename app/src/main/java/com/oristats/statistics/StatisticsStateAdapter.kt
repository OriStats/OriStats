package com.oristats.statistics

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.oristats.db.DB_Test


class StatisticsStateAdapter(fragment: Fragment) : FragmentStateAdapter(fragment){
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        var fragment : Fragment? = null
        when(position){
            0 -> fragment = DB_Test.newInstance()
            1 -> fragment = Time_Chart.newInstance()
            2 -> fragment = Pie_chart.newInstance()
        }
        return fragment!!
    }
}