package com.vungn.backvietlibrary

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.vungn.backvietlibrary.worker.GetAllBooksWorker
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class BackVietApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory
    private lateinit var getAllBooksWorkerRequest: WorkRequest

    override fun onCreate() {
        super.onCreate()
        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)
        setupWorkers()
        startWorkers()
    }

    private fun setupWorkers() {
        getAllBooksWorkerRequest =
            OneTimeWorkRequestBuilder<GetAllBooksWorker>()
                .build()
    }

    private fun startWorkers() {
        WorkManager.getInstance(this).enqueue(getAllBooksWorkerRequest)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder().setWorkerFactory(workerFactory).build()
}