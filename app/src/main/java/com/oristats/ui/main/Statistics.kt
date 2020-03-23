package com.oristats.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.oristats.MainActivity
import com.oristats.R
import com.oristats.ui.main.StatisticsStateAdapter
import kotlinx.android.synthetic.main.statistics_fragment.*


class Statistics : Fragment() {

    private lateinit var statisticsStateAdapter: StatisticsStateAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    companion object {
        fun newInstance() = Statistics()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.statistics_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.app_name)

        statisticsStateAdapter = StatisticsStateAdapter(this)
        viewPager = view.findViewById(R.id.statistics_viewpager)
        viewPager.adapter = statisticsStateAdapter

        //Things added to put the dots
        //the dots aren't displayed
        //because of tablayoutmediator
        tabLayout = view.findViewById(R.id.tab_layout)
        TabLayoutMediator(tabLayout, viewPager,
            TabLayoutMediator.TabConfigurationStrategy {tab, position ->position+1
            }).attach()
        /* Example plot
        //Part1
        val entries = ArrayList<Entry>()

//Part2
        entries.add(Entry(1f, 10f))
        entries.add(Entry(2f, 2f))
        entries.add(Entry(3f, 7f))
        entries.add(Entry(4f, 20f))
        entries.add(Entry(5f, 16f))

//Part3
        val vl = LineDataSet(entries, "My Type")

//Part4
        vl.setDrawValues(false)
        vl.setDrawFilled(true)
        vl.lineWidth = 3f
        vl.fillColor = R.color.Blue
        vl.fillAlpha = R.color.Red

//Part5
        lineChart.xAxis.labelRotationAngle = 0f

//Part6
        lineChart.data = LineData(vl)

//Part7
        lineChart.axisRight.isEnabled = false


//Part8
        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(true)

//Part9
        lineChart.description.text = "Days"
        lineChart.setNoDataText("No forex yet!")

         */


    }

}

