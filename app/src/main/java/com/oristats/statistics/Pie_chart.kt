package com.oristats.statistics

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.oristats.MainActivity
import com.oristats.R
import kotlinx.android.synthetic.main.pie_chart_fragment.*


class Pie_chart : Fragment() {

    companion object {
        fun newInstance() = Pie_chart()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.pie_chart_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.title = getString(
            R.string.fragment_pie_chart
        )



        piechart.setUsePercentValues(true)
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
        data.setValueTextSize(13f)



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

