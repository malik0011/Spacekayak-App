package com.malikstudios.spacekayakapp

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.FirebaseApp
import com.malikstudios.spacekayakapp.data.worker.BillingWorker
import com.malikstudios.spacekayakapp.data.worker.BootWorker
import com.malikstudios.spacekayakapp.utils.NotificationHelper
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class MyApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
        NotificationHelper.createNotificationChannel(this)
        // Delay scheduling slightly to let Hilt initialize everything
        Handler(Looper.getMainLooper()).post {
            scheduleWorkers(this)
        }
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}



/**
 * This function schedules the BootWorker and BillingWorker using WorkManager.
 * The BootWorker runs once at app startup to transition all servers from PENDING to RUNNING state.
 * The BillingWorker runs periodically every 15 minutes to handle billing for running servers.
 */
fun scheduleWorkers(context: Context) {
    val bootWork = OneTimeWorkRequestBuilder<BootWorker>().build()
    val billingWork = PeriodicWorkRequestBuilder<BillingWorker>(
        15, TimeUnit.MINUTES
    ).build()

    WorkManager.getInstance(context).enqueueUniqueWork(
        "boot_worker",
        ExistingWorkPolicy.KEEP,
        bootWork
    )

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "billing_worker",
        ExistingPeriodicWorkPolicy.KEEP,
        billingWork
    )
}

