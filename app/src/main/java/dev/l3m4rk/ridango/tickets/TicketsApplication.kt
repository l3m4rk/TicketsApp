package dev.l3m4rk.ridango.tickets

import android.app.Application
import dagger.Lazy
import dagger.hilt.android.HiltAndroidApp
import dev.l3m4rk.ridango.tickets.data.network.FakeWebServer
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class TicketsApplication : Application() {

    @Inject
    lateinit var loggingTree: Lazy<Timber.Tree>

    @Inject
    lateinit var webServer: FakeWebServer

    override fun onCreate() {
        super.onCreate()
        setupLogging()
        Timber.i("Tickets app started")
        setupFakeApiServer()
    }

    private fun setupLogging() {
        Timber.plant(loggingTree.get())
    }

    private fun setupFakeApiServer() {
        webServer.start()
    }
}
