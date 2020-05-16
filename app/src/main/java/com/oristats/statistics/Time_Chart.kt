package com.oristats.statistics

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.Room
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.oristats.MainActivity
import com.oristats.R
import com.oristats.db.DB_Raw_Entity
import com.oristats.db.DB_Room
import com.oristats.db.DB_ViewModel
import kotlinx.android.synthetic.main.time_chart_fragment.*
import kotlinx.coroutines.Job


class Time_Chart : Fragment() {
    private val DB_Raw_New_Entry_ActivityRequestCode = 1
    private lateinit var db_ViewModel: DB_ViewModel
    fun Graph() {
        // Required empty public constructor
    }

    //private var mChart: LineChart? = null
    companion object {
        fun newInstance() = Time_Chart()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.time_chart_fragment, container, false)

        db_ViewModel = (getActivity() as MainActivity).db_ViewModel
        return view


        /*  db_ViewModel = (getActivity() as MainActivity).db_ViewModel
        val timeView = view.findViewById<LineChart>(R.id.lineChart)
        val adapter = context?.let { Time_adapter(it) }

        db_ViewModel.allRaws.observe(viewLifecycleOwner, Observer { db_raw_entities ->
            // Update the cached copy of entities in the adapter.
            db_raw_entities?.let {
                if (adapter != null) {
                    adapter.setDB_Raw_Entities(it)
                }
            }
        })
return view*/
    }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            (activity as MainActivity).supportActionBar?.title = getString(
                R.string.fragment_time_chart
            )

         val description = Description()

           description.text = "Study hours"
           lineChart.description = description
           // DISPLAY FOR AN EMPTY GRAPH
           lineChart.setNoDataText("No Data Available. Add a Study time")
           //enable touch gestures
           lineChart.setTouchEnabled(true)
          //enable scaling and dragging
          lineChart.isDragEnabled = true
          lineChart.setScaleEnabled(true)
           //draw background grid
          lineChart.setDrawGridBackground(true)
          val xAxis: XAxis = lineChart.xAxis
          xAxis.isEnabled = true
          xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
          xAxis.setDrawAxisLine(true)
          xAxis.setDrawGridLines(true)

            val yAxis: YAxis = lineChart.getAxisLeft()
              // setting the count of Y-axis label's
              // setting the count of Y-axis label's
              yAxis.setLabelCount(12, false)
              yAxis.textColor = Color.BLACK
              yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
              yAxis.setDrawGridLines(true)

              SetData()
              lineChart.notifyDataSetChanged()
              lineChart.invalidate()
                lineChart.xAxis.labelRotationAngle = 0f
            lineChart.axisRight.isEnabled = false

    }

    private fun SetData(){

        val ides: IntArray = intArrayOf(3,4,5)
        val entries = ArrayList<Entry>()
        //val aa=db_ViewModel.raw_load_id(ides)
        val ab=db_ViewModel.get_millis()
        val aa= ab.value?.get(0)?.toFloat()
        println("FUUUUUUUUUUUUUUUUCKKKKKKKKKKKKK")
        print(ab.value?.size)
        //the one I want
        entries.add(Entry(1f, 2f))
        entries.add(Entry(2f, 2f))
        entries.add(Entry(3f, 7f))
        entries.add(Entry(4f, 20f))
        entries.add(Entry(5f, 16f))
        val vl = LineDataSet(entries, "My Type")
        vl.setDrawValues(false)
        vl.setDrawFilled(true)
        vl.lineWidth = 3f
        vl.fillColor = R.color.Blue
        vl.fillAlpha = R.color.Red
        lineChart.data = LineData(vl)


    }


    //Update action bar title when viewpager focuses this fragment
    override fun onResume() {
        super.onResume()
        (activity as MainActivity).supportActionBar?.title = getString(
            R.string.fragment_time_chart
        )
    }


}



