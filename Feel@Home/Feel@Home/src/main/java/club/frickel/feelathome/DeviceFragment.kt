package club.frickel.feelathome

import android.app.Activity
import android.app.Fragment
import android.app.ListFragment


import android.os.AsyncTask
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.BufferedInputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

public class DeviceFragment : ListFragment() {

    private var deviceID: String = ""
    private var mapper: ObjectMapper = ObjectMapper()

    private inner class EffectsHandler : AsyncTask<Void, Void, ArrayList<Effect>>() {

        override fun doInBackground(vararg params: Void): ArrayList<Effect>? {
            val urlString: String = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(Constants.SERVER_URL, "")

            var result: ArrayList<Effect>? = null
            if (urlString.isNotEmpty()) {
                try {
                    val url: URL = URL("$urlString/devices/$deviceID/available")
                    val urlConnection = url.openConnection() as HttpURLConnection
                    val `in` = BufferedInputStream(urlConnection.inputStream)
                    result = mapper.readValue<ArrayList<Effect>>(`in`, object : TypeReference<ArrayList<Effect>>() {

                    })
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            return result
        }

        override fun onPostExecute(result: ArrayList<Effect>?) {
            if (result == null) {
                Toast.makeText(getActivity().getApplicationContext(), "Can't get answer from server! Wrong server?", Toast.LENGTH_SHORT).show()
            } else {
                updateEffectsFragment(result)
            }

        }

    }

    private inner class CurrentEffectConfigHandler internal constructor(private val standardConfigEffect: Effect) : AsyncTask<Void, Void, Effect>() {


        override fun doInBackground(vararg params: Void): Effect? {
            var currentEffect: Effect? = null
            val urlString = getActivity().getSharedPreferences(Constants.SHAREDPREFERENCES, Activity.MODE_PRIVATE).getString(Constants.SERVER_URL, null)
            if (urlString != null) {
                try {
                    val url = URL(urlString + "/devices/" + deviceID + "/effect")
                    val urlConnection = url.openConnection() as HttpURLConnection
                    val `in` = BufferedInputStream(urlConnection.inputStream)
                    currentEffect = mapper.readValue<Effect>(`in`, object : TypeReference<Effect>() {

                    })
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            return currentEffect
        }


        override fun onPostExecute(currentEffect: Effect?) {

            if (currentEffect != null && standardConfigEffect.name == currentEffect.name)
                onEffectSelected(currentEffect)
            else
                onEffectSelected(standardConfigEffect)
        }
    }

    private fun onEffectSelected(effect: Effect) {
        var effectConfigFragment: Fragment? = null
        assert(effect.config != null)
        when (effect.name) {
            "Wheel", "Wheel2", "Rainbow", "Sunrise", "Stroboscope", "Whitefade", "Random Brightness", "Random Fade", "Random Color" -> effectConfigFragment = OnlyOneDelayEffectConfigFragment()
            "Color", "Clock Color" -> effectConfigFragment = OneColorEffectConfigFragment()
            "Brightness", "Brightness Scaling" -> effectConfigFragment = BrightnessEffectConfigFragment()
            "Colorfade" -> effectConfigFragment = OneColorDelayEffectConfigFragment()
            "Fire" -> effectConfigFragment = FireEffectConfigFragment()
            "Power" -> {
                effect.config.toMutableMap().put("Power", true)
            }
        }
        SendStateHandler(effect, deviceID, getActivity()).execute()

        if (effectConfigFragment != null) {
            val bundle = Bundle()
            bundle.putString(Constants.DEVICE_ID, deviceID)
            bundle.putSerializable(Constants.EFFECT_STRING, effect)
            effectConfigFragment.arguments = bundle
            (getActivity() as Main).replaceFragmentAndAddToBackstack(effectConfigFragment)
        }
    }

    fun updateEffectsFragment(effects: ArrayList<Effect>) {
        // We need to use a different list item layout for devices older than Honeycomb
        val layout = android.R.layout.simple_list_item_activated_1

        // Create an array adapter for the list view, using the Ipsum headlines array
        setListAdapter(ArrayAdapter(getActivity(), layout, effects))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        deviceID = arguments.getString(Constants.DEVICE_ID, "")


        updateEffectsFragment(ArrayList<Effect>())
        EffectsHandler().execute()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(Constants.DEVICE_ID, this.deviceID)
        super.onSaveInstanceState(outState)
    }


    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {

        CurrentEffectConfigHandler((getListAdapter() as ArrayAdapter<Effect>).getItem(position)).execute()

        // Set the item as checked to be highlighted when in two-pane layout
        getListView().setItemChecked(position, true)
    }
}