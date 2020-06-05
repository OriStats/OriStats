package com.oristats.statistics

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.oristats.MainActivity
import com.oristats.R
import com.oristats.db.DB_ViewModel
import kotlinx.android.synthetic.main.pie_chart_fragment.*


class Pie_chart : Fragment() {

    private lateinit var db_ViewModel: DB_ViewModel
    companion object {
        fun newInstance() = Pie_chart()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View = inflater.inflate(R.layout.pie_chart_fragment, container, false)
        db_ViewModel = (getActivity() as MainActivity).db_ViewModel
        return view

    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.title = getString(
            R.string.fragment_pie_chart
        )

        //val xvalues = ArrayList<PieEntry>()
        val xvalues= mutableListOf<PieEntry>()

        db_ViewModel.allMains.observe(viewLifecycleOwner, Observer { db_main_entities ->
            db_main_entities?.let {

                if (db_main_entities.isEmpty()) {
                    Log.d("debug1", "it's empty")

                }

                else if(db_main_entities.size==1) {
                    Log.d("Checkpoint1", "it's 1")
                    xvalues.add(PieEntry( 1f, "tag1"))
                    piechart.setUsePercentValues(true)
                    val dataSet = PieDataSet(xvalues, "")
                    val data = PieData(dataSet)
                    // In Percentage
                    data.setValueFormatter(PercentFormatter())

                    dataSet.setColors(
                        resources.getColor(R.color.colorPrimaryDark),
                        resources.getColor(R.color.Red),
                        resources.getColor(R.color.Blue),
                        resources.getColor(R.color.Black)
                    )
                    piechart.animateXY(500, 500);
                    piechart.data = data
                    piechart.description.text = ""
                    piechart.isDrawHoleEnabled = false
                    piechart.isRotationEnabled = false
                    data.setValueTextSize(13f)
                    piechart.notifyDataSetChanged();
                }
                else {
                    with(xvalues){
                        clear()}
                    // var tag1=db_main_entities[1].tag_id
                    //Log.d("Checkup de tags", tag1.toString())


                    db_ViewModel.allRaws.observe(viewLifecycleOwner, Observer { db_raw_entities ->
                        db_raw_entities?.let {
                            if (db_raw_entities.isEmpty()) {
                                Log.d("debug1", "it's empty")
                            } else {
                                var pausa = 0f
                                var work = 0f
                                var total = 0f

                                for (j in db_main_entities.indices) {
                                    for (i in db_raw_entities.indices) {
                                        if (i == 0) {
                                            work += db_raw_entities[1].millis - db_raw_entities[0].millis
                                        }
                                        if (db_main_entities[j].start_raw_id < db_raw_entities[i].id!! && db_raw_entities[i].id!! < db_main_entities[j].end_raw_id)
                                            if (i > 0 && i % 2 == 0 && db_raw_entities[i].millis - db_raw_entities[i - 1].millis > 0) {
                                                work += db_raw_entities[i].millis - db_raw_entities[i - 1].millis
                                            }
                                        if (i > 0 && i % 2 != 0 && db_raw_entities[i].millis - db_raw_entities[i - 1].millis > 0) {
                                            pausa += db_raw_entities[i].millis - db_raw_entities[i - 1].millis
                                        }


                                    }
                                    total += work
                                    Log.d("CHECKPOINT DOPIECHART: ", total.toString())
                                    Log.d("CHECKPOINT PIECHART2: ", work.toString())
                                    xvalues.add(PieEntry(work / (total) * 100, "Work_j"))
                                }
                                piechart.setUsePercentValues(true)
                                val dataSet = PieDataSet(xvalues, "")
                                val data = PieData(dataSet)
                                // In Percentage
                                data.setValueFormatter(PercentFormatter())

                                dataSet.setColors(
                                    resources.getColor(R.color.colorPrimaryDark),
                                    resources.getColor(R.color.Red),
                                    resources.getColor(R.color.Blue),
                                    resources.getColor(R.color.Black)
                                )
                                piechart.animateXY(500, 500);
                                piechart.data = data
                                piechart.description.text = ""
                                piechart.isDrawHoleEnabled = false
                                piechart.isRotationEnabled = false
                                data.setValueTextSize(13f)
                                piechart.notifyDataSetChanged();
                                // piechart.invalidate();

                            }
                        }


                    })

                }  }})

    }

    /* piechart.setUsePercentValues(true)
     val xvalues = ArrayList<PieEntry>()
     xvalues.add(PieEntry(34.0f, "London"))
     xvalues.add(PieEntry(28.2f, "Coventry"))
     xvalues.add(PieEntry(37.9f, "Manchester"))
     val dataSet = PieDataSet(xvalues, "")
     val data = PieData(dataSet)
     // In Percentage
     data.setValueFormatter(PercentFormatter())

     piechart.data = data
     piechart.description.text = ""
     piechart.isDrawHoleEnabled = false
     piechart.isRotationEnabled = false
     data.setValueTextSize(13f)
*/






    fun chartDetails(mChart: PieChart, tf: Typeface) {
        mChart.description.isEnabled = true
        mChart.centerText = ""
        mChart.setCenterTextSize(10F)
        mChart.setCenterTextTypeface(tf)
        val l = mChart.legend
        mChart.legend.isWordWrapEnabled = true
        mChart.legend.isEnabled = false
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        l.formSize = 20F
        l.formToTextSpace = 5f
        l.form = Legend.LegendForm.SQUARE
        l.textSize = 12f
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.isWordWrapEnabled = true
        l.setDrawInside(false)
        mChart.setTouchEnabled(false)
        mChart.setDrawEntryLabels(false)
        mChart.legend.isWordWrapEnabled = true
        mChart.setExtraOffsets(20f, 0f, 20f, 0f)
        mChart.setUsePercentValues(true)
        // mChart.rotationAngle = 0f
        mChart.setUsePercentValues(true)
        mChart.setDrawCenterText(false)
        mChart.description.isEnabled = true
        mChart.isRotationEnabled = false


    }



    //Update action bar title when viewpager focuses this fragment
    override fun onResume() {
        super.onResume()
        (activity as MainActivity).supportActionBar?.title = getString(
            R.string.fragment_pie_chart
        )
    }

}

