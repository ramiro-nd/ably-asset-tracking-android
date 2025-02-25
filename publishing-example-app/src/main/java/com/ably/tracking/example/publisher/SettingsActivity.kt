package com.ably.tracking.example.publisher

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.ably.tracking.Accuracy
import java.util.Locale

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, SettingsFragment())
            .commit()
    }
}

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings_preferences)
        setupResolutionPreferences()
        setupS3Preference()
    }

    private fun setupS3Preference() {
        (findPreference(getString(R.string.preferences_s3_file_key)) as ListPreference?)?.let { s3Preference ->
            S3Helper.fetchLocationHistoryFilenames(
                onListLoaded = { filenamesWithSizes, filenames ->
                    s3Preference.entries = filenamesWithSizes.toTypedArray()
                    s3Preference.entryValues = filenames.toTypedArray()
                },
                onUninitialized = {
                    Toast.makeText(
                        requireContext(),
                        "S3 not initialized - cannot fetch files",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
    }

    private fun setupResolutionPreferences() {
        val appPreferences = AppPreferences(requireContext())
        (findPreference(getString(R.string.preferences_resolution_accuracy_key)) as ListPreference?)?.apply {
            entries = Accuracy.values()
                .map { it.name.toLowerCase(Locale.getDefault()).capitalize(Locale.getDefault()) }
                .toTypedArray()
            entryValues = Accuracy.values().map { it.name }.toTypedArray()
            value = appPreferences.getResolutionAccuracy().name
        }
        (findPreference(getString(R.string.preferences_resolution_desired_interval_key)) as EditTextPreference?)?.apply {
            text = appPreferences.getResolutionDesiredInterval().toString()
            setIntNumberInputType()
        }
        (findPreference(getString(R.string.preferences_resolution_minimum_displacement_key)) as EditTextPreference?)?.apply {
            text = appPreferences.getResolutionMinimumDisplacement().toString()
            setFloatNumberInputType()
        }
    }
}
