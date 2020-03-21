package com.oristats.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.oristats.R
import kotlinx.android.synthetic.main.statistics_fragment.*


class Statistics : Fragment() {


    companion object {
        fun newInstance() = Statistics()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.statistics_fragment, container, false)
    }




    //In this is the first plot
    //when more plots arrive have to link the fragments to this main with ViewPager
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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



    }




}

/*
    private fun initViews() {
        tabs = view.findViewById<TabLayout.Tab>(R.id.tabs)
        viewpager = findViewById(R.id.viewpager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
    }
*/

    /*
    private fun setupViewPager() {
        val adapter = MyFragmentPagerAdapter(getSupportFragmentManager())

        var firstFragment: MyFragment = MyFragment.newInstance("First Fragment")
        var secondFragment: MyFragment = MyFragment.newInstance("Second Fragment")
        var thirdFragment: MyFragment = MyFragment.newInstance("Third Fragment")

        adapter.addFragment(firstFragment, "ONE")
        adapter.addFragment(secondFragment, "TWO")
        adapter.addFragment(thirdFragment, "THREE")

        viewpager.adapter = adapter

        tabs.setupWithViewPager(viewpager)

    }

*/












//}
