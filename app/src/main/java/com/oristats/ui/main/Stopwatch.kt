package com.oristats.ui.main

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import com.oristats.MainActivity
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
        (activity as MainActivity).supportActionBar?.title = getString(R.string.app_name)

        // start: Is it starting over (0:00)?
        var start = true
        // isWorking: Is the client working/studying/etc?
        var isWorking = false

        fabPlayPause?.setOnClickListener(
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

                        // fabStop now visible; fabPlayPause to the left
                        fabStop.setVisibility(View.VISIBLE)
                        val mConstraintSet = ConstraintSet()
                        mConstraintSet.clone(stopwatch_constraintLayout)
                        mConstraintSet.connect(R.id.fabPlayPause, ConstraintSet.END, R.id.guideline, ConstraintSet.START)
                        mConstraintSet.applyTo(stopwatch_constraintLayout)
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

                    // Swap fabPlayPause Play<->Pause
                    fabPlayPause.setImageDrawable(if (isWorking) getResources().getDrawable(R.drawable.ic_pause_black_24dp, context?.getTheme()) else getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp, context?.getTheme()))
                }
            })

        fabStop?.setOnClickListener(
            object : View.OnClickListener {
                override fun onClick(v: View) {
                    if(!start) {
                        start = true
                        isWorking = false
                        crono_work.stop()
                        crono_break.stop()

                        // guarantee fabPlayPause now is Play
                        fabPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp, context?.getTheme()))
                        // fabStop now invisible; fabPlayPause to center
                        fabStop.setVisibility(View.INVISIBLE)
                        val mConstraintSet = ConstraintSet()
                        mConstraintSet.clone(stopwatch_constraintLayout)
                        mConstraintSet.connect(R.id.fabPlayPause, ConstraintSet.END, PARENT_ID, ConstraintSet.END)
                        mConstraintSet.applyTo(stopwatch_constraintLayout)
                    }
                }
            })
    }
}