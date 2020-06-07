package com.oristats.statistics

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ViewPortHandler
import com.oristats.MainActivity
import com.oristats.R
import com.oristats.db.DB_ViewModel
import kotlinx.android.synthetic.main.pie_chart_fragment.*
import java.text.DecimalFormat


class Pie_chart : Fragment() {

    private lateinit var db_ViewModel: DB_ViewModel
    var created = false

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
        (activity as MainActivity).supportActionBar?.title = getString( R.string.fragment_pie_chart )

        created = true
        Pie_chart()
    }

    fun Pie_chart(){
        val entries: MutableList<PieEntry> = ArrayList()
        val entries_temp: MutableMap<String,Long> = mutableMapOf()
        var total: Long = 0
        for(i in db_ViewModel.MainIds.indices){
            val main = db_ViewModel.currentMains.filter{it.start_time == db_ViewModel.MainIds[i]}[0]
            val letUntagged = db_ViewModel.viewUntagged && main.tag_id == -1
            var letTag = main.tag_id != -1
            if (db_ViewModel.statTags != null) {
                letTag = db_ViewModel.statTags!!.filter { main.tag_id == it }.isNotEmpty()
            }
            if(letUntagged || letTag) {
                var tag_name = ""
                if (letUntagged){
                    tag_name = "Untagged"
                }
                else if(letTag){
                    tag_name = db_ViewModel.currentTags.filter { it.id == main.tag_id }[0].path_name
                }
                total += db_ViewModel.MainWorks[i]
                if(entries_temp.get(tag_name) == null){
                    entries_temp.put(tag_name,db_ViewModel.MainWorks[i])
                }
                else{
                    entries_temp.set(tag_name,entries_temp.get(tag_name)!! + db_ViewModel.MainWorks[i])
                }
            }
        }
        entries_temp.forEach{
            entries.add(PieEntry(100f*((it.value).toDouble()/total.toDouble()).toFloat(),it.key))
        }

        piechart.setUsePercentValues(true)
        val dataSet = PieDataSet(entries, "")
        // dataSet.filter{it.}
        val data = PieData(dataSet)
        // In Percentage
        data.setValueFormatter(PercentFormatter(piechart))
        piechart.setUsePercentValues(true)

        dataSet.setColors(
            ContextCompat.getColor(context!!,R.color.Red),
            ContextCompat.getColor(context!!,R.color.Green),
            ContextCompat.getColor(context!!,R.color.Blue),
            ContextCompat.getColor(context!!,R.color.colorPrimaryDark),
            ContextCompat.getColor(context!!,R.color.Black),
            ContextCompat.getColor(context!!,R.color.Purple),
            ContextCompat.getColor(context!!,R.color.Orange),
            ContextCompat.getColor(context!!,R.color.Yellow)
        )
        piechart.animateXY(500, 500)
        piechart.data = data
        piechart.description.text = ""
        piechart.isDrawHoleEnabled = false
        piechart.isRotationEnabled = false
        data.setValueTextSize(15f)
        data.setValueTextColor(ContextCompat.getColor(context!!,R.color.White))
    }

    fun updateChart(){
        piechart.notifyDataSetChanged()
        piechart.invalidate()
    }

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










