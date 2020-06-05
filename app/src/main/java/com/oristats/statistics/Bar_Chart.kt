    package com.oristats.statistics

    import android.graphics.Color
    import android.os.Bundle
    import android.util.Log
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
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
    import kotlin.collections.ArrayList


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

          /*  val barEntries= ArrayList<BarEntry>()
            db_ViewModel.allMains.observe(viewLifecycleOwner, Observer { db_main_entities ->
                db_main_entities?.let {

                    if (db_main_entities.isEmpty()) {
                        Log.d("debug1", "it's empty")
                    } else {
                        var tag1=db_main_entities[1].tag_id
                        Log.d("Checkup de tags", tag1.toString())
                    }



                    db_ViewModel.allRaws.observe(viewLifecycleOwner, Observer { db_raw_entities ->
                        db_raw_entities?.let {
                            if (db_raw_entities.isEmpty()) {
                                Log.d("debug1", "it's empty")
                            } else {
                                var pausa = 0f
                                var work = 0f
                                var total=0f
                                for(j in db_main_entities.indices ) {
                                    for (i in db_raw_entities.indices) {
                                        if (i == 0) {
                                            work += db_raw_entities[1].millis - db_raw_entities[0].millis
                                        }
                                        if (db_main_entities[j].start_raw_id < db_raw_entities[i].id!! && db_raw_entities[i].id!!< db_main_entities[j].end_raw_id)
                                            if (i > 0 && i % 2 == 0 && db_raw_entities[i].millis - db_raw_entities[i - 1].millis > 0) {
                                                work += db_raw_entities[i].millis - db_raw_entities[i - 1].millis
                                            }
                                        if (i > 0 && i % 2 != 0 && db_raw_entities[i].millis - db_raw_entities[i - 1].millis > 0) {
                                          pausa += db_raw_entities[i].millis - db_raw_entities[i - 1].millis
                                        }


                                    }
                                    total+=work
                                    barEntries.add(BarEntry(day.toFloat(), work))
                                }
                                //barEntries.add(BarEntry(day.toFloat(), work))
                                val barDataSet = BarDataSet(barEntries, "Cells")
                                val data = BarData(barDataSet)
                                barchart.data = data // set the data and list of lables into chart
                                //barchart.setDescription("Set Bar Chart Description")  // set the description
                                barchart.getData().notifyDataChanged();
                                // In Percentage
                                //barDataSet.setColors(resources.getColor(R.color.colorPrimaryDark), resources.getColor(R.color.Red),resources.getColor(R.color.Blue),resources.getColor(R.color.Black))

                                val barSpace = 0.02f
                                val groupSpace = 0.3f
                                val groupCount = 4
                                //IMPORTANT
                                data.setBarWidth(0.15f);
                                barchart.getXAxis().setAxisMinimum(0f);
                                barchart.getXAxis().setAxisMaximum(0 + barchart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
                                barchart.groupBars(0f, groupSpace, barSpace); // perform the "explicit" grouping
                                //IMPORTANT
                                barchart.animateY(3000)
                                barchart.notifyDataSetChanged();
                                barchart.invalidate();


                            }
                        }


                    })

                }})*/



    setBarChart()

        }
        private fun setBarChart() {
            val cal = Calendar.getInstance()
            val tz = cal.timeZone
            Log.d("Time zone: ", tz.displayName)
            val sdf = SimpleDateFormat("dd/mm/yyyy")
            val currentDate = sdf.format(Date())
            val day=cal.get(Calendar.DAY_OF_MONTH);
    /* log the system time */
            val aaa=1591393440360
           val date= Date(aaa)
            cal.time = date;
    /* log the system time */
          //  Log.d("TEMPO QUE SA BARCHART: ", cal.get(Calendar.Da).toString())

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
                var total=0f

                    for (i in db_ViewModel.currentRaws.indices) {
                        if (i == 0) {
                            work += db_ViewModel.currentRaws[1].millis - db_ViewModel.currentRaws[0].millis
                        }
                        if (i > 0 && i % 2 == 0 && db_ViewModel.currentRaws[i].millis - db_ViewModel.currentRaws[i - 1].millis > 0) {
                                work += db_ViewModel.currentRaws[i].millis - db_ViewModel.currentRaws[i - 1].millis
                            }
                        if (i > 0 && i % 2 != 0 && db_ViewModel.currentRaws[i].millis - db_ViewModel.currentRaws[i - 1].millis > 0) {
                            pausa += db_ViewModel.currentRaws[i].millis - db_ViewModel.currentRaws[i - 1].millis
                        }


                    total+=work


                }

                yValueGroup1.add(BarEntry(day.toFloat(), work/60000))
                yValueGroup2.add(BarEntry(day.toFloat(), pausa/60000))
                xAxisValues.add(cal.get(Calendar.DAY_OF_WEEK).toString())


            //val entries = ArrayList<BarEntry>()
            //entries.add(BarEntry(day.toFloat(), 60f))
           // entries.add(BarEntry(2f, floatArrayOf(8f, 12f)))
            //entries.add(BarEntry(5f, 2f))

            //entries.add(BarEntry(20f, 3f))
            //entries.add(BarEntry(15f, 4f))
            //entries.add(BarEntry(19f, 5f))




            xAxisValues.add("Jan")
            xAxisValues.add("Feb")
            xAxisValues.add("Mar")
            xAxisValues.add("Apr")
            xAxisValues.add("May")




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

            legenedEntries.add(LegendEntry("Work", Legend.LegendForm.SQUARE, 8f, 8f, null, Color.BLUE))
            legenedEntries.add(LegendEntry("Pause", Legend.LegendForm.SQUARE, 8f, 8f, null, Color.RED))

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
    }