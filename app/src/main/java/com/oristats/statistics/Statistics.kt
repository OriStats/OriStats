package com.oristats.statistics

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.oristats.MainActivity
import com.oristats.NavGraphDirections
import com.oristats.R
import com.oristats.db.DB_ViewModel
import com.oristats.db.Main_Fragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.statistics_fragment.*


class Statistics : Fragment() , Main_Fragment.MainInterface{

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

        statisticsStateAdapter = StatisticsStateAdapter(this,this)
        viewPager = view.findViewById(R.id.statistics_viewpager)
        viewPager.adapter = statisticsStateAdapter

        checkuntagged.isChecked = db_Viewmodel.viewUntagged

        checkuntagged.setOnClickListener {
            db_Viewmodel.viewUntagged = checkuntagged.isChecked
            statisticsStateAdapter.Main_Fragment.updateMains()
            if(statisticsStateAdapter.Bar_Chart.created) {
                statisticsStateAdapter.Bar_Chart.setBarChart()
                statisticsStateAdapter.Bar_Chart.updateChart()
            }
            if(statisticsStateAdapter.Pie_chart.created) {
                statisticsStateAdapter.Pie_chart.Pie_chart()
                statisticsStateAdapter.Pie_chart.updateChart()
            }
        }

        tagfilter.setOnClickListener {
            db_Viewmodel.tagMode = "statSelect"
            val action = NavGraphDirections.actionGlobalTags()
            NavHostFragment.findNavController(nav_host_fragment).navigate(action)
        }

        db_Viewmodel.allMains.observe(viewLifecycleOwner, Observer {
            db_Viewmodel.currentMains = it
        })

        db_Viewmodel.allRaws.observe(viewLifecycleOwner, Observer {
            db_Viewmodel.currentRaws = it
        })

        checkuntagged()
        getWorkPauses()
    }

    override fun onResume() {
        super.onResume()
        db_Viewmodel.tagMode = "normal"
        checkuntagged.isChecked = db_Viewmodel.viewUntagged
        checkuntagged()
        getWorkPauses()
    }

    private fun checkuntagged() {
        db_Viewmodel.currentMains.forEach {
            val main = it.tag_id
            if (db_Viewmodel.currentTags.isNotEmpty()) {
                if (db_Viewmodel.currentTags.filter { it.id == main }.isEmpty() && main != -1) {
                    db_Viewmodel.main_untagged_by_id(main)
                }
            }
        }
    }

    private fun getWorkPauses() {
        db_Viewmodel.MainIds.clear()
        db_Viewmodel.MainWorks.clear()
        db_Viewmodel.MainPauses.clear()
        db_Viewmodel.currentMains.forEach {
            val main = it
            var Work: Long = 0
            var Pause: Long = 0
            val raws = db_Viewmodel.currentRaws.filter { it.id!! >= main.start_raw_id && it.id!! <= main.end_raw_id }
            for (i in raws.indices) {
                if (i % 2 == 1) {
                    Work += raws[i].millis - raws[i - 1].millis
                } else if (i != 0) {
                    Pause += raws[i].millis - raws[i - 1].millis
                }
            }
            db_Viewmodel.MainIds.add(main.start_time)
            db_Viewmodel.MainWorks.add(Work)
            db_Viewmodel.MainPauses.add(Pause)
        }
    }

    override fun gototabs() {
        val action = NavGraphDirections.actionGlobalTags()
        NavHostFragment.findNavController(nav_host_fragment).navigate(action)
    }
}

