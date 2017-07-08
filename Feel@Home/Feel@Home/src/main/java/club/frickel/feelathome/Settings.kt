package club.frickel.feelathome

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.EditTextPreference
import android.preference.PreferenceFragment

class Settings : PreferenceFragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)

        updateSummary()
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences
                .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences
                .unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as Main).restartApplication()
    }


    private fun updateSummary() {
        val editTextServerURL = findPreference(Constants.SERVER_URL) as EditTextPreference
        editTextServerURL.summary = editTextServerURL.text
        val editTextUsername = findPreference(Constants.USERNAME) as EditTextPreference
        editTextUsername.summary = editTextUsername.text
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, s: String) {
        if (s == Constants.SERVER_URL || s == Constants.USERNAME) {
            updateSummary()
        }

    }
}