package com.oristats.statistics

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.oristats.db.Main_Fragment
import com.oristats.db.Raw_Fragment


class StatisticsStateAdapter(fragment: Fragment) : FragmentStateAdapter(fragment){
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        var fragment : Fragment? = null
        when(position){
            0 -> fragment = Main_Fragment.newInstance()
            1 -> fragment = Raw_Fragment.newInstance()  // On the finished product, this fragment won't be necessary, because its info will be somehow incorporated in the fragment "Main_Fragment".
            2 -> fragment = Time_Chart.newInstance()
            3 -> fragment = Pie_chart.newInstance()
        }
        return fragment!!
    }
}