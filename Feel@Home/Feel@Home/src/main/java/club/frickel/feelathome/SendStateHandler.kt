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

class SendStateHandler(internal var effect: Effect, internal var deviceID: String?, internal var context: Context) : AsyncTask<Void, Void, String>() {

    override fun doInBackground(vararg params: Void): String {
        var errorMessage: String = ""
        if (deviceID != null) {
            var urlString = PreferenceManager.getDefaultSharedPreferences(context).getString(Constants.SERVER_URL, "")
            if (!urlString.isEmpty()) {
                urlString += "/devices/$deviceID/effect"
            }
            val url: URL

            try {
                url = URL(urlString)
            } catch (e: MalformedURLException) {
                errorMessage = "Malformed URL: " + urlString
                return errorMessage
            }

            var urlConnection: HttpURLConnection? = null
            try {
                urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.doOutput = true
                urlConnection.requestMethod = "PUT"
                val mapper = ObjectMapper()
                mapper.writeValue(urlConnection.outputStream, effect)
                val responce = urlConnection.responseCode
                if (responce != 200) {

                    errorMessage = "Received ErrorCode: " + responce.toString()
                    Log.d(SEND_ERROR, "Received $responce Error while sending")
                    return errorMessage
                }

            } catch (e: IOException) {
                errorMessage = "Server doesn\\'t exist or isn\\'t availible!"
            } finally {
                if (urlConnection != null) urlConnection.disconnect()
            }


        } else {
            errorMessage = "deviceID was null"
        }

        return errorMessage
    }

    override fun onPostExecute(errorMessage: String) {
        if (!errorMessage.isEmpty()) {
            Toast.makeText(context, "Can't get answer from server! Wrong server?", Toast.LENGTH_SHORT).show()
            //showSendErrorDialog(effect, deviceID, main, errorMessage);
        }

    }

    companion object {

        val SEND_ERROR = "Send Error"
    }
}