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


        /*   switch1?.setOnCheckedChangeListener { _, isChecked ->
               val message = if (isChecked) "Week Records" else "Month Records"
               Toast.makeText(context, message,
                   Toast.LENGTH_SHORT).show()

           }*/
        setBarChart1()
        switch1.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                val message ="Week Records"
                Toast.makeText(context, message,
                    Toast.LENGTH_SHORT).show()
                setBarChart()
            } else {
                // The toggle is disabled
                val message ="Month Records"
                Toast.makeText(context, message,
                    Toast.LENGTH_SHORT).show()
                setBarChart1()
            }
        })


    }
    private fun setBarChart() {
        val cal = Calendar.getInstance()
        val tz = cal.timeZone
        Log.d("Time zone: ", tz.displayName)
        val sdf = SimpleDateFormat("dd/mm/yyyy")
        val currentDate = System.currentTimeMillis()
        val day = cal.get(Calendar.DAY_OF_MONTH);
        /* log the system time */
        val aaa = 1591393440360
        val date = Date(aaa)
        cal.time = date;
        /* log the system time */
        Log.d("TEMPO QUE SA BARCHART: ", currentDate.toString())

        val barWidth: Float
        val barSpace: Float
        val groupSpace: Float
        val groupCount = 12

        barWidth = 0.15f
        barSpace = 0.07f
        groupSpace = 0.56f

        var x = mutableListOf<Pair<Float,Float>>()
        var y = mutableListOf<Pair<Float,Float>>()
        var yValueGroup1 = mutableListOf<BarEntry>()
        var yValueGroup2 = mutableListOf<BarEntry>()
        var xAxisValues = mutableListOf<String>()

        if (db_ViewModel.currentMains.isEmpty()) {
            Log.d("debug1", "it's empty")
        } else {
            // var pausa = 0f
            // var work = 0f
            var total = 0f
            var day=0
            for(j in db_ViewModel.currentMains.indices) {
                var pausa = 0f
                var work = 0f
                Log.d("debug1", (db_ViewModel.currentMains[j].start_time).toString())
                if (currentDate - db_ViewModel.currentMains[j].start_time < 604800000) {
                    for (i in db_ViewModel.currentRaws.indices) {
                        Log.d("debug1", (db_ViewModel.currentRaws[i].millis).toString())
                        Log.d("debug1", (currentDate-db_ViewModel.currentRaws[i].millis).toString())
                        if (i == 0) {
                            work += db_ViewModel.currentRaws[1].millis - db_ViewModel.currentRaws[0].millis
                        }
                        if (i > 0 && i % 2 == 0 && db_ViewModel.currentRaws[i].millis - db_ViewModel.currentRaws[i - 1].millis > 0) {
                            pausa += db_ViewModel.currentRaws[i].millis - db_ViewModel.currentRaws[i - 1].millis
                        }
                        if (i > 0 && i % 2 != 0 && db_ViewModel.currentRaws[i].millis - db_ViewModel.currentRaws[i - 1].millis > 0) {
                            work += db_ViewModel.currentRaws[i].millis - db_ViewModel.currentRaws[i - 1].millis
                        }
                    }
                    total += work

                    val date = Date(db_ViewModel.currentMains[j].start_time)

                    cal.time = date;
                    day=cal.get(Calendar.DAY_OF_WEEK)
                    x.add(Pair(day.toFloat(),total / 60000))
                    y.add(Pair(day.toFloat(),pausa/ 60000))
                    val mapa=mutableMapOf(day.toFloat() to work/60000)

                }
            }


            var ae=x.groupingBy { it.first }.eachCount().filter { it.value > 1 }
            Log.d("aksjbhfcabd",x.groupingBy { it.first }.eachCount().filter { it.value > 1 }.toString())
            var t=0f
            for(j in 0 until 4 step 1){
                t+=x[j].second
            }

            yValueGroup1.add(BarEntry(6f,t))
            yValueGroup2.add(BarEntry(ae.keys.toFloatArray()[0],t))
            xAxisValues.add(day.toString())
            cal.get(Calendar.DAY_OF_MONTH)




            var barDataSet1: BarDataSet
            var barDataSet2: BarDataSet


            barDataSet1 = BarDataSet(yValueGroup1, "Work")
            barDataSet1.setColors(Color.BLUE)

            barDataSet1.setDrawIcons(false)
            barDataSet1.setDrawValues(false)

            barDataSet2 = BarDataSet(yValueGroup2, "Pause")
            barDataSet2.setColors(Color.RED)
            barDataSet2.setDrawIcons(false)
            barDataSet2.setDrawValues(false)


            // Pass Both Bar Data Set's in Bar Data.
            var barData = BarData(barDataSet1, barDataSet2)
            barchart.description.isEnabled = false
            barchart.description.textSize = 0f
            barData.setValueFormatter(LargeValueFormatter())
            barchart.setData(barData)
            barchart.getBarData().setBarWidth(barWidth)
            barchart.getXAxis().setAxisMinimum(0f)
            barchart.getXAxis().setAxisMaximum(12f)
            barchart.groupBars(0f, groupSpace, barSpace)
            //   barChartView.setFitBars(true)
            barchart.getData().setHighlightEnabled(false)
            barchart.invalidate()


            var legend = barchart.legend
            legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM)
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT)
            legend.setOrientation(Legend.LegendOrientation.HORIZONTAL)
            legend.setDrawInside(false)

            var legenedEntries = arrayListOf<LegendEntry>()

            legenedEntries.add(
                LegendEntry(
                    "Work",
                    Legend.LegendForm.SQUARE,
                    8f,
                    8f,
                    null,
                    Color.BLUE
                )
            )
            legenedEntries.add(
                LegendEntry(
                    "Pause",
                    Legend.LegendForm.SQUARE,
                    8f,
                    8f,
                    null,
                    Color.RED
                )
            )

            legend.setCustom(legenedEntries)

            legend.setYOffset(2f)
            legend.setXOffset(2f)
            legend.setYEntrySpace(0f)
            legend.setTextSize(5f)


            val xAxis = barchart.getXAxis()
            xAxis.setGranularity(1f)
            xAxis.setGranularityEnabled(true)
            xAxis.setCenterAxisLabels(true)
            xAxis.setDrawGridLines(false)
            xAxis.textSize = 9f

            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
            xAxis.setValueFormatter(IndexAxisValueFormatter(xAxisValues))

            xAxis.setLabelCount(12)
            xAxis.mAxisMaximum = 12f
            xAxis.setCenterAxisLabels(true)
            xAxis.setAvoidFirstLastClipping(true)
            xAxis.spaceMin = 4f
            xAxis.spaceMax = 4f


        }


    }

    private fun setBarChart1() {
        val cal = Calendar.getInstance()
        val tz = cal.timeZone
        Log.d("Time zone: ", tz.displayName)
        val sdf = SimpleDateFormat("dd/mm/yyyy")
        val currentDate = System.currentTimeMillis()
        // val day = cal.get(Calendar.DAY_OF_MONTH);
        /* log the system time */
        // val aaa = 1591393440360
        //val date = Date(aaa)
        //cal.time = date;
        /* log the system time */
        Log.d("TEMPO QUE SA BARCHART: ", currentDate.toString())

        val barWidth: Float
        val barSpace: Float
        val groupSpace: Float
        val groupCount = 12

        barWidth = 0.15f
        barSpace = 0.07f
        groupSpace = 0.56f


        var yValueGroup1 = mutableListOf<BarEntry>()
        var yValueGroup2 = mutableListOf<BarEntry>()
        var xAxisValues = mutableListOf<String>()

        if (db_ViewModel.currentMains.isEmpty()) {
            Log.d("debug1", "it's empty")
        } else {
            var pausa = 0f
            var work = 0f
            var total = 0f
            for(j in db_ViewModel.currentMains.indices) {
                var pausa = 0f
                var work = 0f
                Log.d("debug1", (db_ViewModel.currentMains[j].start_time).toString())
                if (currentDate - db_ViewModel.currentMains[j].start_time < 2629743830 ) {
                    for (i in db_ViewModel.currentRaws.indices) {
                        Log.d("debug1", (db_ViewModel.currentRaws[i].millis).toString())
                        Log.d("debug1", (currentDate-db_ViewModel.currentRaws[i].millis).toString())
                        if (i == 0) {
                            work += db_ViewModel.currentRaws[1].millis - db_ViewModel.currentRaws[0].millis
                        }
                        if (i > 0 && i % 2 == 0 && db_ViewModel.currentRaws[i].millis - db_ViewModel.currentRaws[i - 1].millis > 0) {
                            pausa += db_ViewModel.currentRaws[i].millis - db_ViewModel.currentRaws[i - 1].millis
                        }
                        if (i > 0 && i % 2 != 0 && db_ViewModel.currentRaws[i].millis - db_ViewModel.currentRaws[i - 1].millis > 0) {
                            work += db_ViewModel.currentRaws[i].millis - db_ViewModel.currentRaws[i - 1].millis
                        }
                    }
                    total += work


                }

                val date = Date(db_ViewModel.currentMains[j].start_time)
                cal.time = date;
                val day=cal.get(Calendar.DAY_OF_MONTH)
                val mapa=mutableMapOf(day.toFloat() to work/60000)
                yValueGroup1.add(BarEntry(day.toFloat(), work/60000))
                yValueGroup2.add(BarEntry(day.toFloat(), pausa / 60000))
                xAxisValues.add(cal.get(Calendar.DAY_OF_MONTH).toString())
                cal.get(Calendar.DAY_OF_MONTH)
            }

            val start = 0f
            barchart.getXAxis().setAxisMinValue(start)

            var barDataSet1: BarDataSet
            var barDataSet2: BarDataSet

            barDataSet1 = BarDataSet(yValueGroup1, "Work")
            //barDataSet1.Setcolors(ColorTemplate.COLORFUL_COLORS)
            barDataSet1.setColors(Color.BLUE)
            barDataSet1.setDrawIcons(false)
            barDataSet1.setDrawValues(false)
            barDataSet2 = BarDataSet(yValueGroup2, "Pause")
            barDataSet2.setColors(Color.RED)
            barDataSet2.setDrawIcons(false)
            barDataSet2.setDrawValues(false)


            // Pass Both Bar Data Set's in Bar Data.
            var barData = BarData(barDataSet1, barDataSet2)
            barchart.description.isEnabled = false
            barchart.description.textSize = 0f
            barData.setValueFormatter(LargeValueFormatter())
            barchart.setData(barData)
            barchart.getBarData().setBarWidth(barWidth)
            barchart.getXAxis().setAxisMinimum(0f)
            barchart.getXAxis().setAxisMaximum(12f)
            barchart.groupBars(0f, groupSpace, barSpace)
            //   barChartView.setFitBars(true)
            barchart.getData().setHighlightEnabled(false)
            barchart.invalidate()


            var legend = barchart.legend
            legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM)
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT)
            legend.setOrientation(Legend.LegendOrientation.HORIZONTAL)
            legend.setDrawInside(false)

            var legenedEntries = arrayListOf<LegendEntry>()

            legenedEntries.add(
                LegendEntry(
                    "Work",
                    Legend.LegendForm.SQUARE,
                    8f,
                    8f,
                    null,
                    Color.BLUE
                )
            )
            legenedEntries.add(
                LegendEntry(
                    "Pause",
                    Legend.LegendForm.SQUARE,
                    8f,
                    8f,
                    null,
                    Color.RED
                )
            )

            legend.setCustom(legenedEntries)

            legend.setYOffset(2f)
            legend.setXOffset(2f)
            legend.setYEntrySpace(0f)
            legend.setTextSize(5f)


            val xAxis = barchart.getXAxis()
            xAxis.setGranularity(1f)
            xAxis.setGranularityEnabled(true)
            xAxis.setCenterAxisLabels(true)
            xAxis.setDrawGridLines(false)
            xAxis.textSize = 9f

            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
            xAxis.setValueFormatter(IndexAxisValueFormatter(xAxisValues))

            xAxis.setLabelCount(12)
            xAxis.mAxisMaximum = 12f
            xAxis.setCenterAxisLabels(true)
            xAxis.setAvoidFirstLastClipping(true)
            xAxis.spaceMin = 4f
            xAxis.spaceMax = 4f


        }


    }

    //Update action bar title when viewpager focuses this fragment
    override fun onResume() {
        super.onResume()
        (activity as MainActivity).supportActionBar?.title = getString(
            R.string.fragment_bar_chart
        )
    }
}