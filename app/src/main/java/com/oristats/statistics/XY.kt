package com.oristats.statistics

import android.graphics.DashPathEffect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.androidplot.util.PixelUtils
import com.androidplot.xy.CatmullRomInterpolator
import com.androidplot.xy.LineAndPointFormatter
import com.androidplot.xy.SimpleXYSeries
import com.androidplot.xy.XYSeries
import com.oristats.MainActivity
import com.oristats.R
import com.oristats.db.DB_ViewModel
import kotlinx.android.synthetic.main.xy_plot.*


class XY : Fragment() {
    private lateinit var db_ViewModel: DB_ViewModel
    companion object {
        fun newInstance() = XY()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View=inflater.inflate(R.layout.xy_plot, container, false)

        db_ViewModel = (getActivity() as MainActivity).db_ViewModel

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.title = getString(
            R.string.fragment_xy
        )

//Part2
        // create a couple arrays of y-values to plot:
        // create a couple arrays of y-values to plot:
        val domain= ArrayList<Number>()
        val value= ArrayList<Number>()
        // turn the above arrays into XYSeries':
        // (Y_VALS_ONLY means use the element index as the x value)

        // turn the above arrays into XYSeries':
        // (Y_VALS_ONLY means use the element index as the x value)
        db_ViewModel.allRaws.observe(viewLifecycleOwner, Observer {db_raw_entities ->
            db_raw_entities?.let{
                if(db_raw_entities.isEmpty()){
                    Log.d("debug1","it's empty")
                }
                else {
                    for(i in db_raw_entities.indices) {
                        if(i>0) {
                           value.add(db_raw_entities[i].millis-db_raw_entities[i-1].millis)
                            db_raw_entities[i].id?.let { it1 -> domain.add(it1) }
                        }
                    }
                    Log.d("debug12", db_raw_entities[0].millis.toString())
                    val series1: XYSeries = SimpleXYSeries(domain,value, "Series1")
                    val series1Format = LineAndPointFormatter(context, R.xml.line_point_formatter_with_labels)
                    series1Format.interpolationParams = CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal)
                    plot.addSeries(series1, series1Format)

                }
            }
        })
        //val series1: XYSeries = SimpleXYSeries(arrayListOf<Number>(1, 4, 2, 8, 4, 16, 8, 32, 16, 64), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series1")
       // val series2: XYSeries = SimpleXYSeries(series2Numbers, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series2")

        // create formatters to use for drawing a series using LineAndPointRenderer
        // and configure them from xml:

        // create formatters to use for drawing a series using LineAndPointRenderer
        // and configure them from xml:


       // val series1Format =
        //    LineAndPointFormatter(context, R.xml.line_point_formatter_with_labels)

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
        //series1Format.interpolationParams = CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal)

        series2Format.interpolationParams = CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal)

        // add a new series' to the xyplot:

        // add a new series' to the xyplot:
        //plot.addSeries(series1, series1Format)
        //plot.addSeries(series2, series2Format)
    }
}