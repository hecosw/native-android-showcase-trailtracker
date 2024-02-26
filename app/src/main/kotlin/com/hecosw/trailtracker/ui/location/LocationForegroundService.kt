package com.hecosw.trailtracker.ui.location

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ServiceCompat
import com.hecosw.trailtracker.R
import com.hecosw.trailtracker.common.logging.Logger
import com.hecosw.trailtracker.di.DispatcherModule
import com.hecosw.trailtracker.domain.usecase.StartLocationUpdatesUseCase
import com.hecosw.trailtracker.domain.usecase.StopLocationUpdatesUseCase
import com.hecosw.trailtracker.ui.screen.trail.TrailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LocationForegroundService : Service() {

    @Inject
    lateinit var log: Logger

    @Inject
    lateinit var startLocationUpdatesUseCase: StartLocationUpdatesUseCase

    @Inject
    lateinit var stopLocationUpdatesUseCase: StopLocationUpdatesUseCase

    @Inject
    @DispatcherModule.IODispatcher
    lateinit var ioDispatcher: CoroutineDispatcher

    private lateinit var serviceScope: CoroutineScope

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log.info("Location foreground service started")
        try {
            createNotificationChannel()
            val notificationId = 1
            val notification = createNotification()
            ServiceCompat.startForeground(
                this, notificationId, notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
            )
            with(NotificationManagerCompat.from(this)) {
                if (ActivityCompat.checkSelfPermission(
                        this@LocationForegroundService,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return@with
                }
                notify(notificationId, notification)
            }
            serviceScope = CoroutineScope(SupervisorJob() + ioDispatcher)
            serviceScope.launch {
                startLocationUpdatesUseCase.execute()
            }
        } catch (e: Exception) {
            log.error("App not in a valid state to start foreground service")
        }
        return START_STICKY
    }

    override fun onDestroy() {
        log.info("Location foreground service stopped")
        serviceScope.launch {
            stopLocationUpdatesUseCase.execute()
        }
        super.onDestroy()
    }

    private fun createNotificationChannel() {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
            description = CHANNEL_DESCRIPTION
        }
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun createNotification(): Notification {
        log.debug("Location foreground notification created")
        val intent = Intent(this, TrailActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Location Service Running")
            .setContentText("Your location is being tracked until you stop the session in the app.")
            .setCategory(Notification.CATEGORY_SERVICE)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        return builder.build()
    }

    private companion object {
        const val CHANNEL_ID = "hckcChannelId"
        const val CHANNEL_NAME = "hckcChannelName"
        const val CHANNEL_DESCRIPTION = "hckcChannelDescription"
    }
}
