package com.oristats.statistics

import android.graphics.DashPathEffect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.androidplot.util.PixelUtils
import com.androidplot.xy.CatmullRomInterpolator
import com.androidplot.xy.LineAndPointFormatter
import com.androidplot.xy.PointLabelFormatter
import com.androidplot.xy.SimpleXYSeries
import com.androidplot.xy.XYSeries
import com.oristats.MainActivity
import com.oristats.R
import kotlinx.android.synthetic.main.xy_plot.*


class XY : Fragment() {
    companion object {
        fun newInstance() = XY()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.xy_plot, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.title = getString(
            R.string.fragment_xy
        )

//Part2
        // create a couple arrays of y-values to plot:
        // create a couple arrays of y-values to plot:
        val domainLabels =
            arrayOf<Number>(1, 2, 3, 6, 7, 8, 9, 10, 13, 14)
        val series1Numbers =
            arrayListOf<Number>(1, 4, 2, 8, 4, 16, 8, 32, 16, 64)
        val series2Numbers =
            arrayListOf<Number>(5, 2, 10, 5, 20, 10, 40, 20, 80, 40)

        // turn the above arrays into XYSeries':
        // (Y_VALS_ONLY means use the element index as the x value)

        // turn the above arrays into XYSeries':
        // (Y_VALS_ONLY means use the element index as the x value)
        val series1: XYSeries = SimpleXYSeries(arrayListOf<Number>(1, 4, 2, 8, 4, 16, 8, 32, 16, 64), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series1")
        val series2: XYSeries = SimpleXYSeries(
            series2Numbers, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series2"
        )

        // create formatters to use for drawing a series using LineAndPointRenderer
        // and configure them from xml:

        // create formatters to use for drawing a series using LineAndPointRenderer
        // and configure them from xml:


        val series1Format =
            LineAndPointFormatter(context, R.xml.line_point_formatter_with_labels)

        val series2Format =
            LineAndPointFormatter(context, R.xml.line_point_formatter_with_labels_2)

        // add an "dash" effect to the series2 line:

        // add an "dash" effect to the series2 line:
        series2Format.linePaint.pathEffect = DashPathEffect(
            floatArrayOf( // always use DP when specifying pixel sizes, to keep things consistent across devices:
                PixelUtils.dpToPix(20F),
                PixelUtils.dpToPix(15F)
            ), 0F
        )

        // just for fun, add some smoothing to the lines:
        // see: http://androidplot.com/smooth-curves-and-androidplot/

        // just for fun, add some smoothing to the lines:
        // see: http://androidplot.com/smooth-curves-and-androidplot/
        series1Format.interpolationParams = CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal)

        series2Format.interpolationParams = CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal)

        // add a new series' to the xyplot:

        // add a new series' to the xyplot:
        plot.addSeries(series1, series1Format)
        plot.addSeries(series2, series2Format)
    }
}