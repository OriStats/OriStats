package com.oristats.ui.main

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel

class Stopwatch_ViewModel(application: Application) : AndroidViewModel(application) {

    // Keep info/chrono even if app is closed.
    val stopwatch_save = application.getSharedPreferences("com.oristats.STOPWATCH_SAVE", Context.MODE_PRIVATE)

    // Start: Is it starting over (0:00)?
    fun setStart(start : Boolean) {
        with (stopwatch_save.edit()) {
            putBoolean("Start", start)
            apply()
        }
    }
    fun getStart(): Boolean {
        return stopwatch_save.getBoolean("Start", true)
    }

    // IsWorking: Is the client working/studying/etc?
    fun setIsWorking(isWorking : Boolean) {
        with (stopwatch_save.edit()) {
            putBoolean("IsWorking", isWorking)
            apply()
        }
    }
    fun getIsWorking(): Boolean {
        return stopwatch_save.getBoolean("IsWorking", false)
    }

    // # of breaks
    fun setBreaks(breaks : Int) {
        with (stopwatch_save.edit()) {
            putInt("Breaks", breaks)
            apply()
        }
    }
    fun getBreaks(): Int {
        return stopwatch_save.getInt("Breaks", 0)
    }

    // Current Session main_table PrimaryKey: start_time
    fun setMainStartTime(mainStartTime: Long) {
        with (stopwatch_save.edit()) {
            putLong("MainStartTime", mainStartTime)
            apply()
        }
    }
    fun getMainStartTime(): Long {
        return stopwatch_save.getLong("MainStartTime", 0)
    }

    // store info between pause<->resume
    fun setLastPlay(lastPlay : Long) {
        with (stopwatch_save.edit()) {
            putLong("LastPlay", lastPlay)
            apply()
        }
    }
    fun getLastPlay(): Long {
        return stopwatch_save.getLong("LastPlay", 0)
    }
    fun setLastPause(lastPause : Long) {
        with (stopwatch_save.edit()) {
            putLong("LastPause", lastPause)
            apply()
        }
    }
    fun getLastPause(): Long {
        return stopwatch_save.getLong("LastPause", 0)
    }

    // store info for RestoreChrono()
    fun setLastWorkBase(lastWorkBase : Long) {
        with (stopwatch_save.edit()) {
            putLong("LastWorkBase", lastWorkBase)
            apply()
        }
    }
    fun getLastWorkBase(): Long {
        return stopwatch_save.getLong("LastWorkBase", 0)
    }
    fun setLastBreakBase(lastBreakBase : Long) {
        with (stopwatch_save.edit()) {
            putLong("LastBreakBase", lastBreakBase)
            apply()
        }
    }
    fun getLastBreakBase(): Long {
        return stopwatch_save.getLong("LastBreakBase", 0)
    }

    // KEEP COUNTING AFTER REBOOT:
    // See MainActivity.kt : RebootDetector(). Stopwatch.kt : MillisForDB()

    // LastBootCompare is the millisecond timestamp in a hour of last boot: (System.currentTimeMillis()-SystemClock.elapsedRealtime())%3600000
    // Why using %3600000? Because the hour can change because of Summer Time or Timezone...
    // There will be used a margin of +-5000 milliseconds for eventual clock updates based on internet.
    // If right after you boot a phone you use OriStats and reboot the system and manage to do you it all within 5 seconds it won't detect it. But does it need to? A 5second error in a session is not important.
    fun setLastBootCompare(lastBootCompare : Long) {
        with (stopwatch_save.edit()) {
            putLong("LastBootCompare", lastBootCompare)
            apply()
        }
    }
    fun getLastBootCompare(): Long {
        return stopwatch_save.getLong("LastBootCompare", 0)
    }
    // LastBoot is: System.currentTimeMillis()-SystemClock.elapsedRealtime()
    fun setLastBoot(lastBoot : Long) {
        with (stopwatch_save.edit()) {
            putLong("LastBoot", lastBoot)
            apply()
        }
    }
    fun getLastBoot(): Long {
        return stopwatch_save.getLong("LastBoot", 0)
    }
    fun setRebootCorrection(rebootCorrection : Long) {
        with (stopwatch_save.edit()) {
            putLong("RebootCorrection", rebootCorrection)
            apply()
        }
    }
    fun getRebootCorrection(): Long {
        return stopwatch_save.getLong("RebootCorrection", 0)
    }

}