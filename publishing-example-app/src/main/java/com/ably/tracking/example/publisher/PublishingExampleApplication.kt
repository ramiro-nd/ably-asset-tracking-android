package com.ably.tracking.example.publisher

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.multidex.MultiDexApplication

const val NOTIFICATION_CHANNEL_ID = "PublisherServiceChannelId"

class PublishingExampleApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        S3Helper.init(this)
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getSystemService(NotificationManager::class.java).apply {
                createNotificationChannel(
                    NotificationChannel(
                        NOTIFICATION_CHANNEL_ID,
                        "Publisher Service Channel",
                        NotificationManager.IMPORTANCE_LOW
                    )
                )
            }
        }
    }
}
