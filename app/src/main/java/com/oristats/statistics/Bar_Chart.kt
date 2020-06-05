package com.oristats.statistics

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.PercentFormatter
import com.oristats.MainActivity
import com.oristats.R
import com.oristats.db.DB_ViewModel
import kotlinx.android.synthetic.main.bar_chart_fragment.*
import kotlinx.android.synthetic.main.pie_chart_fragment.*
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

/* log the system time */Log.d("TEMPO QUE SA BARCHART: ", day.toString())



       /* val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(day.toFloat(), 60f))
        entries.add(BarEntry(2f, floatArrayOf(8f, 12f)))
        entries.add(BarEntry(5f, 2f))

        entries.add(BarEntry(20f, 3f))
        entries.add(BarEntry(15f, 4f))
        entries.add(BarEntry(19f, 5f))*/
        val barEntries= ArrayList<BarEntry>()
        val barEntries1= ArrayList<BarEntry>()
        barEntries.add(BarEntry(day.toFloat(), 989.21f))
        barEntries.add(BarEntry(2f, 420.22f))
        barEntries.add(BarEntry(3f, 758f))

        barEntries1.add(BarEntry(day.toFloat(),950f))
        barEntries1.add(BarEntry(2f, 791f))
        barEntries1.add(BarEntry(3f, 630f))

        val barDataSet = BarDataSet(barEntries, "Cells")
        val barDataSet2 = BarDataSet(barEntries1, "Cells2")
        val labels = ArrayList<String>()
        labels.add("18-Jan")
        labels.add("19-Jan")
        labels.add("20-Jan")
        labels.add("21-Jan")
        labels.add("22-Jan")
        labels.add("23-Jan")
        val data = BarData(barDataSet,barDataSet2)
        barchart.data = data // set the data and list of lables into chart
        //barchart.setDescription("Set Bar Chart Description")  // set the description
        barchart.getData().notifyDataChanged();

       // barDataSet.setColors(ColorTemplate.COLORFUL_COLORS)
        barDataSet.color = resources.getColor(R.color.Red)
        barDataSet2.color = resources.getColor(R.color.Blue)


        val barSpace = 0.02f
        val groupSpace = 0.3f
        val groupCount = 4
        //IMPORTANT *****
        data.setBarWidth(0.15f);
        barchart.getXAxis().setAxisMinimum(0f);
        barchart.getXAxis().setAxisMaximum(0 + barchart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        barchart.groupBars(0f, groupSpace, barSpace); // perform the "explicit" grouping
        //***** IMPORTANT
        barchart.animateY(3000)
        barchart.notifyDataSetChanged();
        barchart.invalidate();

    }
    }