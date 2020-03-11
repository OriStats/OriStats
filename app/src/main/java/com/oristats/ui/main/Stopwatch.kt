package com.oristats.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.oristats.R
import kotlinx.android.synthetic.main.stopwatch_fragment.*


class Stopwatch : Fragment() {

    companion object {
        fun newInstance() = Stopwatch()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.stopwatch_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //val meter =R.id.c_meter//gets chronometer from the XML using the proper id
        //val btn =R.id.btStart//gets the button from the XML

        btStart?.setOnClickListener(
            object : View.OnClickListener {

                var isWorking = false

                override fun onClick(v: View) {
                    if (!isWorking) {
                        c_meter.start()
                        //print("entrou")
                        isWorking = true
                    } else {
                        c_meter.stop()
                        isWorking = false
                    }

                    btStart.setText(if (isWorking) R.string.start else R.string.stop)
                    //Toast.makeText(this@Stopwatch, getString(if (isWorking) R.string.working else R.string.stopped), Toast.LENGTH_SHORT).show()

                }
            })
    }
}