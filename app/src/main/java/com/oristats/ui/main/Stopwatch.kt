package com.oristats.ui.main

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import com.oristats.MainActivity
import com.oristats.R
import com.oristats.db.DB_Raw_Entity
import com.oristats.db.DB_ViewModel
import kotlinx.android.synthetic.main.stopwatch_fragment.*


class Stopwatch : Fragment() {

    private lateinit var stopwatch_ViewModel: Stopwatch_ViewModel
    private lateinit var db_ViewModel: DB_ViewModel

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

        stopwatch_ViewModel = (getActivity() as MainActivity).stopwatch_ViewModel
        db_ViewModel = (getActivity() as MainActivity).db_ViewModel

        if (!stopwatch_ViewModel.getStart()){
            RestoreChrono()
        } else {
            ZeroChrono()
        }

        fabPlayPause?.setOnClickListener( object : View.OnClickListener {
                override fun onClick(v: View) {
                    if(stopwatch_ViewModel.getStart()) StartChrono() else {
                        if (stopwatch_ViewModel.getIsWorking()) PauseChrono() else ResumeChrono()
                    }
                }
            })

        fabStop?.setOnClickListener( object : View.OnClickListener {
                override fun onClick(v: View) {
                    if(!stopwatch_ViewModel.getStart()) StopChrono()
                }
            })
    }

    // Maintenance Functions

    private fun RestoreChrono () {
        // Restore Chronometer when onViewCreated() is called and it was already counting.
        WorkBase(stopwatch_ViewModel.getLastWorkBase())
        BreakBase(stopwatch_ViewModel.getLastBreakBase())
        if (stopwatch_ViewModel.getIsWorking()) {
            crono_break.setBase( stopwatch_ViewModel.getLastBreakBase() + SystemClock.elapsedRealtime() - stopwatch_ViewModel.getLastPlay())    // Only for display purposes. The right value will be used when Pause Pressed, using BreakBase().
            crono_work.start()
            crono_break.stop()
            updateButtons("start") //This line is not a mistake.
        } else {
            crono_work.setBase( stopwatch_ViewModel.getLastWorkBase() + SystemClock.elapsedRealtime() - stopwatch_ViewModel.getLastPause())     // Only for display purposes. The right value will be used when Play Pressed, using WorkBase().
            crono_work.stop()
            crono_break.start()
            updateButtons("start") //This line is not a mistake.
            updateButtons("pause")
        }
    }

    private fun ZeroChrono() { // Not necessary to use. Simply to set 0.00 all views. You can use StartChrono() without using this.
        // Clean Reboot Correction
        stopwatch_ViewModel.setRebootCorrection(0)  // Necessary

        WorkBase(SystemClock.elapsedRealtime())
        BreakBase(SystemClock.elapsedRealtime())
        crono_work.stop()
        crono_break.stop()
        // reset # of breaks
        stopwatch_ViewModel.setBreaks(0)
        // Update State Vars & Buttons
        stopwatch_ViewModel.setStart(true)
        stopwatch_ViewModel.setIsWorking(false)
        updateButtons("beginning")
    }

    // Maintenance && Stopwatch Functions

    private fun WorkBase (base: Long) {
        crono_work.setBase(base)
        stopwatch_ViewModel.setLastWorkBase(base)
    }

    private fun BreakBase (base: Long) {
        crono_break.setBase(base)
        stopwatch_ViewModel.setLastBreakBase(base)
    }

    // Stopwatch Functions

    private fun StartChrono() {
        // Clean Reboot Correction
        stopwatch_ViewModel.setRebootCorrection(0)  // Necessary

        stopwatch_ViewModel.setLastPlay(SystemClock.elapsedRealtime())
        WorkBase(SystemClock.elapsedRealtime())
        BreakBase(SystemClock.elapsedRealtime())
        crono_work.start()
        crono_break.stop()
        // reset # of breaks
        stopwatch_ViewModel.setBreaks(0)
        // Update State Vars & Buttons
        stopwatch_ViewModel.setStart(false)
        stopwatch_ViewModel.setIsWorking(true)
        updateButtons("start")

        // Store to DB
        db_ViewModel.raw_insert(DB_Raw_Entity(MillisForDB()))
    }

    private fun PauseChrono() {
        stopwatch_ViewModel.setLastPause(SystemClock.elapsedRealtime())
        BreakBase( stopwatch_ViewModel.getLastBreakBase() + SystemClock.elapsedRealtime() - stopwatch_ViewModel.getLastPlay())
        crono_work.stop()
        crono_break.start()
        // Add 1 break
        stopwatch_ViewModel.setBreaks(stopwatch_ViewModel.getBreaks() + 1)
        // Update State Vars & Buttons
        stopwatch_ViewModel.setIsWorking(false)
        updateButtons("pause")

        // Store to DB
        db_ViewModel.raw_insert(DB_Raw_Entity(MillisForDB()))
    }

    private fun ResumeChrono() {
        stopwatch_ViewModel.setLastPlay(SystemClock.elapsedRealtime())
        WorkBase( stopwatch_ViewModel.getLastWorkBase() + SystemClock.elapsedRealtime() - stopwatch_ViewModel.getLastPause())
        crono_work.start()
        crono_break.stop()
        // Update State Vars & Buttons
        stopwatch_ViewModel.setIsWorking(true)
        updateButtons("resume")

        // Store to DB
        db_ViewModel.raw_insert(DB_Raw_Entity(MillisForDB()))
    }

    private fun StopChrono() {
        if (stopwatch_ViewModel.getIsWorking()) crono_work.stop() else crono_break.stop()
        // Update State Vars & Buttons
        stopwatch_ViewModel.setStart(true)
        stopwatch_ViewModel.setIsWorking(false)
        updateButtons("stop")

        // Store to DB
        db_ViewModel.raw_insert(DB_Raw_Entity(MillisForDB()))
    }

    // Visual Functions

    private fun updateButtons(situation: String) { // Also updates break_textView.
        if(situation=="start") {
            // fabStop now visible
            fabStop.setVisibility(View.VISIBLE)
            // fabPlayPause to the left
            val mConstraintSet = ConstraintSet()
            mConstraintSet.clone(stopwatch_constraintLayout)
            mConstraintSet.connect(R.id.fabPlayPause, ConstraintSet.END, R.id.guideline, ConstraintSet.START)
            mConstraintSet.applyTo(stopwatch_constraintLayout)
            // fabPlayPause Play->Pause
            fabPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_colorsecondary_24dp, context?.getTheme()))
        }
        if(situation=="pause"){ // Assumes you applied situation=="start" before; Only admitting situation=="resume" or situation=="pause" in between. If not, just run: updateButtons("start") ; updateButtons("pause").
            // fabPlayPause Pause->Play
            fabPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_colorsecondary_24dp, context?.getTheme()))
        }
        if(situation=="resume"){ // Assumes you applied situation=="start" before; Only admitting situation=="resume" or situation=="pause" in between. If not, just run: updateButtons("start") ; updateButtons("resume").
            // fabPlayPause Play->Pause
            fabPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_colorsecondary_24dp, context?.getTheme()))
        }
        if(situation=="stop"){
            // fabStop now invisible
            fabStop.setVisibility(View.INVISIBLE)
            // fabPlayPause to center
            val mConstraintSet = ConstraintSet()
            mConstraintSet.clone(stopwatch_constraintLayout)
            mConstraintSet.connect(R.id.fabPlayPause, ConstraintSet.END, PARENT_ID, ConstraintSet.END)
            mConstraintSet.applyTo(stopwatch_constraintLayout)
            // fabPlayPause is now Play w/ Circle Outline: meaning/remembering it will start a new session.
            fabPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_circle_outline_colorsecondary_24dp, context?.getTheme()))
        }
        if(situation=="beginning"){ // EQUAL TO "STOP", but fabPlayPause is simply Play. Configuration when opening the app, defined in stopwatch_fragment.xml.
            // fabStop now invisible
            fabStop.setVisibility(View.INVISIBLE)
            // fabPlayPause to center
            val mConstraintSet = ConstraintSet()
            mConstraintSet.clone(stopwatch_constraintLayout)
            mConstraintSet.connect(R.id.fabPlayPause, ConstraintSet.END, PARENT_ID, ConstraintSet.END)
            mConstraintSet.applyTo(stopwatch_constraintLayout)
            // fabPlayPause is now Play
            fabPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_colorsecondary_24dp, context?.getTheme()))
        } /*else {
            // Only updates # of breaks
        }*/
        // Update # of breaks displayed on break_textView
        break_textView.setText(resources.getQuantityString(R.plurals.break_time_w_counter, stopwatch_ViewModel.getBreaks(), stopwatch_ViewModel.getBreaks()))
    }

    // Because of Reboots:

    private fun MillisForDB() : Long { // Milliseconds since last boot before the beginning of the session. USE DIRECTLY THIS FUNCTION TO STORE TIMESTAMPS TO DB.
        return SystemClock.elapsedRealtime() + stopwatch_ViewModel.getRebootCorrection()
    }
}