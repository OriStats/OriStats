package com.oristats.statistics

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.oristats.db.Main_Fragment
import com.oristats.db.Raw_Fragment


class StatisticsStateAdapter(fragment: Fragment, main_interface: Main_Fragment.MainInterface) : FragmentStateAdapter(fragment){

    var Main_Fragment = com.oristats.db.Main_Fragment.newInstance(main_interface)

    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        var fragment : Fragment? = null
        when(position){
            0 -> fragment = Main_Fragment
            1 -> fragment = Time_Chart.newInstance()
            2 -> fragment = Bar_Chart.newInstance()
            3 -> fragment = Pie_chart.newInstance()
//            4 -> fragment = Raw_Fragment.newInstance()  // On the finished product, this fragment won't be necessary, because its info will be somehow incorporated in the fragment "Main_Fragment".
        }
        return fragment!!
    }
}