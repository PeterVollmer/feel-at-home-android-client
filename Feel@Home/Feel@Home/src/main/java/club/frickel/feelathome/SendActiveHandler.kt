package club.frickel.feelathome

import android.content.Context
import android.os.AsyncTask
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*

/**
 * Created by nino on 08.07.17.
 */
class SendActiveHandler(internal var active: Boolean, internal var deviceID: String, internal var context: Context) : AsyncTask<Void, Void, String>() {

    override fun doInBackground(vararg params: Void): String {
        var errorMessage: String = ""
        if (deviceID != null) {
            var urlString = PreferenceManager.getDefaultSharedPreferences(context).getString(Constants.SERVER_URL, null)
            if (urlString != null) {
                urlString += "/devices/$deviceID/active"
            }
            val url: URL

            try {
                url = URL(urlString)
            } catch (e: MalformedURLException) {
                errorMessage = "Malformed URL: " + urlString!!
                return errorMessage
            }

            val urlConnection: HttpURLConnection
            try {
                urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.doOutput = true
                urlConnection.requestMethod = "PUT"
                val mapper = ObjectMapper()
                val activeObject = HashMap<String, Any>()
                activeObject.put("Active", active)
                mapper.writeValue(urlConnection.outputStream, activeObject)
                val responce = urlConnection.responseCode
                if (responce != 200) {

                    errorMessage = "Received ErrorCode: " + responce.toString()
                    Log.d(SEND_ERROR, "Received $responce Error while sending")
                }

            } catch (e: IOException) {
                errorMessage = "Server doesn\\'t exist or isn\\'t availible!"
                return errorMessage
            }


        } else {
            errorMessage = "deviceID was null"
        }

        return errorMessage
    }

    override fun onPostExecute(errorMessage: String) {
        if (!errorMessage.isEmpty()) {
            Log.d("Send Error", "State Error" + errorMessage)
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
        //showSendErrorDialog(active, deviceID, context, errorMessage);
    }

    companion object {

        val SEND_ERROR = "Send Error"
    }
}
