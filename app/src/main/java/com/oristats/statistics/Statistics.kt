package com.oristats.statistics

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.oristats.MainActivity
import com.oristats.NavGraphDirections
import com.oristats.R
import com.oristats.db.DB_ViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.statistics_fragment.*


class Statistics : Fragment() {

    private lateinit var statisticsStateAdapter: StatisticsStateAdapter
    private lateinit var db_Viewmodel: DB_ViewModel
    private lateinit var viewPager: ViewPager2

    companion object {
        fun newInstance() = Statistics()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        db_Viewmodel = (activity as MainActivity).db_ViewModel
        return inflater.inflate(R.layout.statistics_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.app_name)

        statisticsStateAdapter =
            StatisticsStateAdapter(this)
        viewPager = view.findViewById(R.id.statistics_viewpager)
        viewPager.adapter = statisticsStateAdapter

        checkuntagged.isChecked = db_Viewmodel.viewUntagged

        checkuntagged.setOnClickListener {
            db_Viewmodel.viewUntagged = checkuntagged.isChecked
        }

        tagfilter.setOnClickListener {
            db_Viewmodel.tagMode = "statSelect"
            val action = NavGraphDirections.actionGlobalTags()
            NavHostFragment.findNavController(nav_host_fragment).navigate(action)
        }
    }

    override fun onResume() {
        super.onResume()
        db_Viewmodel.tagMode = "normal"
        checkuntagged.isChecked = db_Viewmodel.viewUntagged
        /*
        db_Viewmodel.statTags?.forEach {
            val id :Int? = it
            Log.d("teste", db_Viewmodel.currentTags.filter { it.id == id }[0].path_name)
        }
         */
    }

}



/* Example Plot [Final de onViewCreated().]
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