package com.hecosw.trailtracker.common.logging

import android.util.Log
import com.hecosw.trailtracker.BuildConfig
import timber.log.Timber
import javax.inject.Inject

class TrailTrackerLogger @Inject constructor(): Logger {

    init {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(ReleaseTree())
        }
    }

    override fun verbose(message: String) {
        Timber.v(message)
    }

    override fun debug(message: String) {
        Timber.d(message)
    }

    override fun info(message: String) {
        Timber.i(message)
    }

    override fun warning(message: String) {
        Timber.v(message)
    }

    override fun error(message: String, throwable: Throwable?) {
        if (throwable != null) {
            Timber.e(throwable, message)
        } else {
            Timber.e(message)
        }
    }

    override fun assert(message: String) {
        Timber.v(message)
    }

    internal class ReleaseTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            when (priority) {
                Log.VERBOSE, Log.DEBUG, Log.INFO -> return // Leave out of release builds.
                Log.WARN, Log.ERROR -> return // TODO: Log to network.
            }
            Log.println(priority, tag, message)
        }
    }

}
