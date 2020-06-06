package com.oristats.statistics

import android.graphics.Color
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
        val view: View = inflater.inflate(R.layout.pie_chart_fragment, container, false)
        db_ViewModel = (getActivity() as MainActivity).db_ViewModel
        return view

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.title = getString(
            R.string.fragment_pie_chart
        )

        //val xvalues = ArrayList<PieEntry>()
        val xvalues = mutableListOf<PieEntry>()
        val dataSet = PieDataSet(xvalues, "")
        var currency_mains =
            db_ViewModel.currentMains.filter { it.tag_id == -1 }


        if (db_ViewModel.currentMains.isEmpty()) {
            Log.d("debug1", "it's empty")

        } else {
            if (db_ViewModel.statTags == null) {
                if (db_ViewModel.currentMains.size == 1) {
                    with(xvalues) {
                        clear()
                    }
                    Log.d("Checkpoint1", "it's 1")
                    //xvalues.add(PieEntry(1f,  db_ViewModel.currentTags[0].path_name))
                    xvalues.add(PieEntry(100f, "caminhando"))
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
                } else {
                    with(xvalues) {
                        clear()
                    }
                    // var tag1=db_ViewModel.currentMains[1].tag_id
                    //Log.d("Checkup de tags", tag1.toString())

                    //var filtered_mains =
                      //  db_ViewModel.currentMains.filter { it.tag_id != -1 }
                    if (db_ViewModel.currentRaws.isEmpty()) {
                        Log.d("debug1", "it's empty")
                    } else {

                        with(xvalues) {
                            clear()
                        }
                        var pausa = 0f
                        var work = 0f
                        var total = 0f
                        var p = 0
                        for (j in db_ViewModel.currentMains.indices) {
                            for (i in db_ViewModel.currentRaws.indices) {
                                if (i == 0) {
                                    work += db_ViewModel.currentRaws[1].millis - db_ViewModel.currentRaws[0].millis
                                }
                                if (db_ViewModel.currentMains[j].start_raw_id < db_ViewModel.currentRaws[i].id!! && db_ViewModel.currentRaws[i].id!! < db_ViewModel.currentMains[j].end_raw_id)
                                    if (i > 0 && i % 2 == 0 && db_ViewModel.currentRaws[i].millis - db_ViewModel.currentRaws[i - 1].millis > 0) {
                                        work += db_ViewModel.currentRaws[i].millis - db_ViewModel.currentRaws[i - 1].millis
                                    }
                                if (i > 0 && i % 2 != 0 && db_ViewModel.currentRaws[i].millis - db_ViewModel.currentRaws[i - 1].millis > 0) {
                                    pausa += db_ViewModel.currentRaws[i].millis - db_ViewModel.currentRaws[i - 1].millis
                                }
                                total += work


                            }
                            if(db_ViewModel.currentMains[j].tag_id==-1) {
                                xvalues.add(PieEntry(work / (total) * 100, "Untagged"))
                            }
                            else{
                                xvalues.add(PieEntry(work / (total) * 100, db_ViewModel.currentTags.filter{it.id==db_ViewModel.currentMains[j].tag_id}[0].path_name))
                            }
                            Log.d("CHECKPOINT DOPIECHART: ", total.toString())
                            Log.d("CHECKPOINT PIECHART2: ", work.toString())



                            //    db_ViewModel.currentTags.forEach{
                            //      it.id=currency_mains[j].tag_id
                            // }


                            // }

                            /*      val main = it.tag_id
                                if(db_ViewModel.currentTags.filter { it.id == main }.isEmpty() && main != -1){
                                    xvalues.add(PieEntry(work / (total) * 100, "Untagged"))
                                }
                                else{
                                    xvalues.add(PieEntry(work / (total) * 100, db_ViewModel.currentTags[j].path_name))

                                }
                            }*/
                            p += j
                            dataSet.setColors(resources.getColor(R.color.colorPrimaryDark),
                            resources.getColor(R.color.Red),
                            resources.getColor(R.color.Blue),
                            resources.getColor(R.color.Black))
                        }

                    }
                    piechart.setUsePercentValues(true)
                    val data = PieData(dataSet)
                    // In Percentage
                    data.setValueFormatter(PercentFormatter())
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


             else {
                with(xvalues) {
                    clear()
                }
                Log.d("DEbugg das tags", 234321.toString())
                var filtered_mains =
                    db_ViewModel.currentMains.filter { db_ViewModel.statTags!!.contains(it.tag_id) && it.tag_id != -1 }

                Log.d("DEBUGG", filtered_mains.indices.toString())

                if (db_ViewModel.currentRaws.isEmpty()) {
                    Log.d("debug1", "it's empty")
                } else {
                    with(xvalues) {
                        clear()
                    }
                    var pausa = 0f
                    var work = 0f
                    var total = 0f
                    var p=0
                    for (j in filtered_mains.indices) {
                        for (i in db_ViewModel.currentRaws.indices) {
                            if (i == 0) {
                                work += db_ViewModel.currentRaws[1].millis - db_ViewModel.currentRaws[0].millis
                            }
                            if (filtered_mains[j].start_raw_id < db_ViewModel.currentRaws[i].id!! && db_ViewModel.currentRaws[i].id!! < filtered_mains[j].end_raw_id)
                                if (i > 0 && i % 2 == 0 && db_ViewModel.currentRaws[i].millis - db_ViewModel.currentRaws[i - 1].millis > 0) {
                                    work += db_ViewModel.currentRaws[i].millis - db_ViewModel.currentRaws[i - 1].millis
                                }
                            if (i > 0 && i % 2 != 0 && db_ViewModel.currentRaws[i].millis - db_ViewModel.currentRaws[i - 1].millis > 0) {
                                pausa += db_ViewModel.currentRaws[i].millis - db_ViewModel.currentRaws[i - 1].millis
                            }


                        }
                        total += work
                        Log.d("CHECKPOINT DOPIECHART: ", total.toString())
                        Log.d("CHECKPOINT PIECHART2: ", work.toString())
                        xvalues.add(
                            PieEntry(
                                work / (total) * 100,
                                db_ViewModel.currentTags.filter{it.id==filtered_mains[j].tag_id}[0].path_name)
                        )
                        p+=j

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
                    piechart.notifyDataSetChanged()
                    piechart.invalidate()


                }

            }

        }


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










