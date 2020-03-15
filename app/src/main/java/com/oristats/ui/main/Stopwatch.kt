package com.oristats.ui.main

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        var start = true
        var isWorking = false

        btStart?.setOnClickListener(
            object : View.OnClickListener {

                var lastPause: Long = 0
                var lastWork: Long = 0

                override fun onClick(v: View) {
                    if(start) {
                        crono_work.setBase(SystemClock.elapsedRealtime())
                        crono_break.setBase(SystemClock.elapsedRealtime())
                        lastWork = SystemClock.elapsedRealtime()
                        crono_break.stop()
                        start = false
                        isWorking = true
                        crono_work.start()
                    } else {
                        if (!isWorking) {
                            lastWork = SystemClock.elapsedRealtime()
                            crono_break.stop()
                            crono_work.base = crono_work.base + SystemClock.elapsedRealtime() - lastPause
                            crono_work.start()
                            isWorking = true
                        } else {
                            lastPause = SystemClock.elapsedRealtime()
                            crono_work.stop()
                            crono_break.base = crono_break.base + SystemClock.elapsedRealtime() - lastWork
                            crono_break.start()
                            isWorking = false
                        }
                    }

                    btStart.setText(if (isWorking) R.string.pause else R.string.resume)
                    //Toast.makeText(this@Stopwatch, getString(if (isWorking) R.string.working else R.string.stopped), Toast.LENGTH_SHORT).show()
                }
            })

        btStop?.setOnClickListener(
            object : View.OnClickListener {
                override fun onClick(v: View) {
                    if(!start) {
                        start = true
                        isWorking = false
                        crono_work.stop()
                        crono_break.stop()
                        btStart.setText(R.string.start)
                    }
                }
            })
    }
}