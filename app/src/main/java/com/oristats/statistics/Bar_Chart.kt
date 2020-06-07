package com.oristats.statistics

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.oristats.MainActivity
import com.oristats.R
import com.oristats.db.DB_ViewModel
import kotlinx.android.synthetic.main.bar_chart_fragment.*
import java.text.SimpleDateFormat
import java.util.*


class Bar_Chart: Fragment() {

    private var mode: String = "Week"
    var created = false
    private lateinit var db_ViewModel: DB_ViewModel
    companion object {
        fun newInstance() = Bar_Chart()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View =inflater.inflate(R.layout.bar_chart_fragment, container, false)

        db_ViewModel = (getActivity() as MainActivity).db_ViewModel
        return view

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.title = getString(
            R.string.fragment_bar_chart
        )


        switch1.isChecked = true
        switch1.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                mode = "Week"
                setBarChart()
            } else {
                // The toggle is disabled
                mode = "Month"
                setBarChart()
            }
        })
        setBarChart()
        created = true
    }
    fun updateChart(){
        barchart.notifyDataSetChanged()
        barchart.invalidate()
    }

    fun setBarChart() {
        val cal = Calendar.getInstance()

        val barWidth: Float
        val barSpace: Float
        val groupSpace: Float

        barWidth = 0.45f
        barSpace = 0.02f
        groupSpace = 0.06f

        val xminimum = 0f
        var xmaximum = 0f
        val yminimum = 0f
        var ymaximum = 1f
        var yAxisLabel = "Seconds"

        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        var time: Long = 0
        val now: Long = System.currentTimeMillis()
        if(mode == "Week"){
            time = 518400000 + now - cal.timeInMillis //6 days + time since day began
        }
        else if(mode == "Month"){
            time = 2505600000 + now - cal.timeInMillis //29 days + time since day began
        }

        val WorkPerDay: MutableMap<Float,Float> = mutableMapOf<Float,Float>()
        val PausePerDay: MutableMap<Float,Float> = mutableMapOf<Float,Float>()
        val LegendPerDay: MutableMap<Float,String> = mutableMapOf<Float,String>()
        val workGroup = mutableListOf<BarEntry>()
        val pauseGroup = mutableListOf<BarEntry>()
        val xAxisValues = mutableListOf<String>()

        for(i in db_ViewModel.MainIds.indices){
            val letUntagged = db_ViewModel.viewUntagged && db_ViewModel.currentMains.filter { it.start_time == db_ViewModel.MainIds[i] }[0].tag_id == -1
            var letTag = true && db_ViewModel.currentMains.filter { it.start_time == db_ViewModel.MainIds[i] }[0].tag_id != -1
            if (db_ViewModel.statTags != null) {
                letTag = db_ViewModel.statTags!!.filter { tag ->
                    db_ViewModel.currentMains.filter { it.start_time == db_ViewModel.MainIds[i] }[0].tag_id == tag
                }.isNotEmpty()
            }
            if(db_ViewModel.MainIds[i] > now - time && (letUntagged || letTag)){
                cal.time = Date(db_ViewModel.MainIds[i])
                var day = cal.get(Calendar.DAY_OF_YEAR).toFloat()
                var legend = ""
                if(mode == "Week") {
                    day = cal.get(Calendar.DAY_OF_WEEK).toFloat()
                    val formatter = SimpleDateFormat("EEE")
                    legend = formatter.format(cal.time)

                }
                if (mode == "Month"){
                    day = cal.get(Calendar.DAY_OF_MONTH).toFloat()
                    val formatter = SimpleDateFormat("dd/MM")
                    legend = formatter.format(cal.time)
                }
                val work = (db_ViewModel.MainWorks[i]/1000).toFloat()
                val pause = (db_ViewModel.MainPauses[i]/1000).toFloat()
                if (WorkPerDay.get(day) == null){
                    WorkPerDay.put(day, work)
                    PausePerDay.put(day, pause)
                    LegendPerDay.put(day,legend)
                }
                else{
                    WorkPerDay.set(day,WorkPerDay.get(day)!!+ work)
                    PausePerDay.set(day,PausePerDay.get(day)!!+ pause)
                }
            }
        }

        if (WorkPerDay.isNotEmpty()) {

            WorkPerDay.forEach {
                if(it.value > ymaximum){
                    ymaximum = it.value*1.20f
                }
                xmaximum += 1f
            }


            PausePerDay.forEach {
                if(it.value > ymaximum){
                    ymaximum = it.value*1.20f
                }
            }

            if (ymaximum >= 7200) {
                WorkPerDay.forEach { workGroup.add(BarEntry(it.key, (it.value/3600f))) }
                PausePerDay.forEach { pauseGroup.add(BarEntry(it.key, (it.value/3600f))) }
                yAxisLabel = "Hours"
            }
            else if (ymaximum >= 120){
                WorkPerDay.forEach { workGroup.add(BarEntry(it.key, (it.value/60f))) }
                PausePerDay.forEach { pauseGroup.add(BarEntry(it.key, (it.value/60f)))}
                yAxisLabel = "Minutes"
            }
            else{
                WorkPerDay.forEach { workGroup.add(BarEntry(it.key, it.value)) }
                PausePerDay.forEach { pauseGroup.add(BarEntry(it.key, it.value))}
            }

            if (yAxisLabel == "Hours"){
                ymaximum = ymaximum/3600f
            }
            else if(yAxisLabel == "Minutes"){
                ymaximum = ymaximum/60f
            }

            LegendPerDay.forEach{ xAxisValues.add(it.value) }
        }
        else {
            workGroup.add(BarEntry(0f,0f))
            pauseGroup.add(BarEntry(0f,0f))
        }

        val barDataSet1: BarDataSet
        val barDataSet2: BarDataSet

        ytile.text = yAxisLabel

        barDataSet1 = BarDataSet(workGroup, "Work")
        barDataSet1.setColors(Color.BLUE)

        barDataSet1.setDrawIcons(false)
        barDataSet1.setDrawValues(false)

        barDataSet2 = BarDataSet(pauseGroup, "Pause")
        barDataSet2.setColors(Color.RED)
        barDataSet2.setDrawIcons(false)
        barDataSet2.setDrawValues(false)


        // Pass Both Bar Data Set's in Bar Data.
        val barData = BarData(barDataSet1, barDataSet2)
        barchart.description.isEnabled = false
        barchart.description.textSize = 0f
        barData.setValueFormatter(LargeValueFormatter())
        barchart.setData(barData)
        barchart.getBarData().setBarWidth(barWidth)
        barchart.getXAxis().setAxisMinimum(xminimum)
        barchart.getXAxis().setAxisMaximum(xmaximum)
        barchart.axisLeft.axisMinimum = yminimum
        barchart.axisLeft.axisMaximum = ymaximum
        barchart.groupBars(0f, groupSpace, barSpace)
        //   barChartView.setFitBars(true)
        barchart.getData().setHighlightEnabled(false)
        barchart.invalidate()


        val legend = barchart.legend
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM)
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT)
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL)
        legend.setDrawInside(false)

        val legendEntries = arrayListOf<LegendEntry>()

        legendEntries.add(
            LegendEntry( "Work", Legend.LegendForm.SQUARE, 8f, 8f, null, Color.BLUE )
        )
        legendEntries.add(
            LegendEntry( "Pause", Legend.LegendForm.SQUARE, 8f, 8f, null, Color.RED )
        )

        legend.setCustom(legendEntries)

        legend.setYOffset(10f)
        legend.setXOffset(2f)
        legend.setYEntrySpace(0f)
        legend.setTextSize(5f)


        val xAxis = barchart.getXAxis()
        xAxis.setGranularity(1f)
        xAxis.setGranularityEnabled(true)
        xAxis.setCenterAxisLabels(true)
        xAxis.setDrawGridLines(false)
        xAxis.textSize = 15f

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
        xAxis.setValueFormatter(IndexAxisValueFormatter(xAxisValues))

        xAxis.setCenterAxisLabels(true)
        xAxis.setAvoidFirstLastClipping(true)
        xAxis.spaceMin = 4f
        xAxis.spaceMax = 4f

    }


    //Update action bar title when viewpager focuses this fragment
    override fun onResume() {
        super.onResume()
        (activity as MainActivity).supportActionBar?.title = getString(
            R.string.fragment_bar_chart
        )
    }
}