package club.frickel.feelathome

import android.content.Context
import android.os.AsyncTask
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.BufferedInputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

abstract class DeviceHandler(protected var context: Context) : AsyncTask<Void, String, ArrayList<Device>>() {

    override fun doInBackground(vararg params: Void): ArrayList<Device>? {
        var deviceList: ArrayList<Device>? = null

        val urlString = PreferenceManager.getDefaultSharedPreferences(context).getString(Constants.SERVER_URL, null)
        if (urlString != null) {
            try {
                val url = URL(urlString + "/devices")
                val urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.connectTimeout = 2000
                val `in` = BufferedInputStream(urlConnection.inputStream)
                deviceList = ObjectMapper().readValue<ArrayList<Device>>(`in`, object : TypeReference<ArrayList<Device>>() {

                })
            } catch (e: IOException) {
                //e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                Log.d("DeviceHandler", "doInBackground: " + e.message)
                return null
            }

        }
        return deviceList
    }

    override fun onProgressUpdate(vararg error: String) {
        Toast.makeText(context, error[0], Toast.LENGTH_SHORT).show()
    }


    abstract override fun onPostExecute(deviceList: ArrayList<Device>)
}
