package dev.l3m4rk.ridango.tickets.di

import android.util.Log
import timber.log.Timber

@Suppress("unused")
class ReleaseLogger : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG) {
            return
        }

        if (t != null) {
            when (priority) {
                Log.WARN -> {
                    // Analytics.logWarning(t)
                }
                Log.ERROR -> {
                    // Analytics.logError(t)
                }
            }
        }
    }
}