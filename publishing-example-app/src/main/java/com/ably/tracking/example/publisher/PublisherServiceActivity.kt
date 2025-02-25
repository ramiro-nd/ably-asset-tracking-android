package com.ably.tracking.example.publisher

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

private const val NO_FLAGS = 0

/**
 * The activity to extend when connection to the publisher service is needed.
 */
abstract class PublisherServiceActivity : AppCompatActivity() {
    protected var publisherService: PublisherService? = null
    private val publisherServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, serviceBinder: IBinder) {
            (serviceBinder as PublisherService.Binder).getService().let { service ->
                publisherService = service
                onPublisherServiceConnected(service)
            }
        }

        /**
         * The Android system calls this when the connection to the service is unexpectedly lost,
         * such as when the service has crashed or has been killed.
         * This is not called when the client unbinds.
         */
        override fun onServiceDisconnected(className: ComponentName) {}
    }

    override fun onStart() {
        super.onStart()
        bindPublisherService()
    }

    override fun onStop() {
        unbindPublisherService()
        publisherService = null
        onPublisherServiceDisconnected()
        super.onStop()
    }

    /**
     * Called when there's a successful connection with [PublisherService].
     * Should be overwritten in the inheriting class if it wants to perform some actions on the service connect.
     */
    protected open fun onPublisherServiceConnected(publisherService: PublisherService) {}

    /**
     * Called when the [PublisherService] is disconnected.
     * Should be overwritten in the inheriting class if it wants to perform some actions on the service disconnect.
     */
    protected open fun onPublisherServiceDisconnected() {}

    /**
     * Creates and starts the [PublisherService].
     */
    protected fun startAndBindPublisherService() {
        ContextCompat.startForegroundService(this, createServiceIntent())
        bindPublisherService()
    }

    /**
     * Stops the [PublisherService].
     */
    protected fun stopPublisherService() {
        stopService(createServiceIntent())
    }

    private fun bindPublisherService() {
        bindService(createServiceIntent(), publisherServiceConnection, NO_FLAGS)
    }

    private fun unbindPublisherService() {
        unbindService(publisherServiceConnection)
    }

    private fun createServiceIntent() = Intent(this, PublisherService::class.java)
}
