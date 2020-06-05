package com.oristats.statistics

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.oristats.MainActivity
import com.oristats.R
import com.oristats.db.DB_ViewModel
import kotlinx.android.synthetic.main.time_chart_fragment.*


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
        val work = ArrayList<Entry>()
        val pause=ArrayList<Entry>()
        //val aa=db_ViewModel.raw_load_id(ides)
        val ab=db_ViewModel.get_millis()
        val aa= ab.value?.get(0)?.toFloat()
        //the one I want
        db_ViewModel.allRaws.observe(viewLifecycleOwner, Observer {db_raw_entities ->
            db_raw_entities?.let{
                if(db_raw_entities.isEmpty()){
                    Log.d("debug1","it's empty")
                }
                else {
                    Log.d("debug3", db_raw_entities[0].id.toString())
                    for(i in db_raw_entities.indices) {
                        if(i==0) {
                            db_raw_entities[i].id?.toFloat()?.let { it1 ->
                                Entry(
                                    it1,
                                    (db_raw_entities[1].millis-db_raw_entities[0].millis).toFloat()
                                )
                            }
                                ?.let { it2 -> work.add(it2) }
                        }

                        if(i>0 && i%2==0 && db_raw_entities[i].millis-db_raw_entities[i-1].millis>0) {
                            db_raw_entities[i].id?.toFloat()?.let { it1 ->
                                Entry(
                                    it1,
                                    (db_raw_entities[i].millis-db_raw_entities[i-1].millis).toFloat()
                                )
                            }
                                ?.let { it2 -> work.add(it2) }
                        }
                        if(i>0 && i%2!=0 && db_raw_entities[i].millis-db_raw_entities[i-1].millis>0) {
                            db_raw_entities[i].id?.toFloat()?.let { it1 ->
                                Entry(
                                    it1,
                                    (db_raw_entities[i].millis-db_raw_entities[i-1].millis).toFloat()
                                )
                            }
                                ?.let { it2 -> pause.add(it2) }
                        }
                    }
                    //entries.add(Entry(1f, 2f))
                    //entries.add(Entry(2f, db_raw_entities[1].millis.div(1000).toFloat()))
                    //entries.add(Entry(3f, db_raw_entities[2].millis.div(1000).toFloat()))
                    //entries.add(Entry(4f, db_raw_entities[3].millis.div(1000).toFloat()))
                    //entries.add(Entry(5f, db_raw_entities[4].millis.div(1000).toFloat()))
                    //entries.add(Entry(6f, db_raw_entities[5].millis.div(1000).toFloat()))

                   // entries.add(Entry(5f, 16f))
                    val lines = ArrayList<LineDataSet>()
                    val vl = LineDataSet(work, "Work")
                    vl.setDrawValues(false)
                    vl.setDrawFilled(true)
                    vl.lineWidth = 3f
                    vl.color = R.color.colorPrimaryDark
                    vl.fillAlpha = R.color.Red
                    lineChart.data = LineData(vl)

                    val vl1 = LineDataSet(pause, "Pause")
                    vl1.setDrawValues(false)
                    vl1.setDrawFilled(true)
                    vl1.lineWidth = 3f
                   // vl1.color = R.color.Red
                    vl1.fillAlpha = R.color.Red
                    lines.add(vl)
                    lines.add(vl1)
                    lineChart.data = LineData(lines as List<ILineDataSet>?)

                }
            }
        })



    }


    //Update action bar title when viewpager focuses this fragment
    override fun onResume() {
        super.onResume()
        (activity as MainActivity).supportActionBar?.title = getString(
            R.string.fragment_time_chart
        )
    }


}



