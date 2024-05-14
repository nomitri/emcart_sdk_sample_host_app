package com.nomitri.nomitrisampleapp

import android.app.Application
import android.util.Log
import androidx.annotation.NonNull
import timber.log.Timber

class NomitriSampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val tree = if (BuildConfig.DEBUG) Timber.DebugTree() else CrashReportingTree()
        Timber.plant(tree)
    }

    /** A tree which logs important information for crash reporting.  */
    private class CrashReportingTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, @NonNull message: String, t: Throwable?) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return
            }
        }
    }

}
