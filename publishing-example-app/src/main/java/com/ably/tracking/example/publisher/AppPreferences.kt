package com.ably.tracking.example.publisher

import android.content.Context
import androidx.preference.PreferenceManager
import com.ably.tracking.Accuracy

class AppPreferences(context: Context) {
    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val LOCATION_SOURCE_KEY = context.getString(R.string.preferences_location_source_key)
    private val SIMULATION_CHANNEL_KEY =
        context.getString(R.string.preferences_simulation_channel_name_key)
    private val S3_FILE_KEY = context.getString(R.string.preferences_s3_file_key)
    private val RESOLUTION_ACCURACY_KEY = context.getString(R.string.preferences_resolution_accuracy_key)
    private val RESOLUTION_DESIRED_INTERVAL_KEY =
        context.getString(R.string.preferences_resolution_desired_interval_key)
    private val RESOLUTION_MINIMUM_DISPLACEMENT_KEY =
        context.getString(R.string.preferences_resolution_minimum_displacement_key)
    private val DEFAULT_LOCATION_SOURCE = context.getString(R.string.default_location_source)
    private val DEFAULT_SIMULATION_CHANNEL = context.getString(R.string.default_simulation_channel)
    private val DEFAULT_S3_FILE = ""
    private val DEFAULT_RESULTION_ACCURACY = Accuracy.BALANCED.name
    private val DEFAULT_RESULTION_DESIRED_INTERVAL = 1000L
    private val DEFAULT_RESULTION_MINIMUM_DISPLACEMENT = 1.0f

    fun getLocationSource(): String =
        preferences.getString(LOCATION_SOURCE_KEY, DEFAULT_LOCATION_SOURCE)!!

    fun getSimulationChannel() =
        preferences.getString(SIMULATION_CHANNEL_KEY, DEFAULT_SIMULATION_CHANNEL)!!

    fun getS3File() =
        preferences.getString(S3_FILE_KEY, DEFAULT_S3_FILE)!!

    fun getResolutionAccuracy(): Accuracy =
        preferences.getString(RESOLUTION_ACCURACY_KEY, DEFAULT_RESULTION_ACCURACY)!!.let { Accuracy.valueOf(it) }

    fun getResolutionDesiredInterval(): Long =
        preferences.getString(RESOLUTION_DESIRED_INTERVAL_KEY, null)?.toLong()
            ?: DEFAULT_RESULTION_DESIRED_INTERVAL

    fun getResolutionMinimumDisplacement(): Float =
        preferences.getString(RESOLUTION_MINIMUM_DISPLACEMENT_KEY, null)?.toFloat()
            ?: DEFAULT_RESULTION_MINIMUM_DISPLACEMENT
}
