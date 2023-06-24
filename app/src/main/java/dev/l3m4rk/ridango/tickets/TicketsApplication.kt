package dev.l3m4rk.ridango.tickets

import android.app.Application
import dagger.Lazy
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class TicketsApplication : Application() {

    @Inject
    lateinit var loggingTree: Lazy<Timber.Tree>

    override fun onCreate() {
        super.onCreate()
        setupLogging()
        Timber.i("Tickets app started")
    }

    private fun setupLogging() {
        Timber.plant(loggingTree.get())
    }
}
