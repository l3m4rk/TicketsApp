package dev.l3m4rk.ridango.tickets

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TicketsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}