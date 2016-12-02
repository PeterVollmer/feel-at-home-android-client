package club.frickel.feelathome;

import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * User: Peter Vollmer
 * Date: 3/13/13
 * Time: 1:39 PM
 */
public class SendStateHandler extends AsyncTask<Void, Void, String> {
    Effect effect;
    String deviceID;
    Context context;

    public static final String SEND_ERROR = "Send Error";

    public SendStateHandler(Effect effect, String deviceID, Context context) {
        this.effect = effect;
        this.context = context;
        this.deviceID = deviceID;
    }

    protected String doInBackground(Void... params) {
        String errorMessage = null;
        if (deviceID != null) {
            String urlString = PreferenceManager.getDefaultSharedPreferences(context).getString(Constants.SERVER_URL, null);
            if (urlString != null) {
                urlString += "/devices/" + deviceID + "/effect";
            }
            URL url;

            try {
                url = new URL(urlString);
            } catch (MalformedURLException e) {
                errorMessage =  "Malformed URL: " + urlString;
                return errorMessage;
            }

            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("PUT");
                ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(urlConnection.getOutputStream(), effect);
                int responce = urlConnection.getResponseCode();
                if (responce != 200) {

                    errorMessage = "Received ErrorCode: " + String.valueOf(responce);
                    Log.d(SEND_ERROR, "Received " + responce + " Error while sending");
                    return errorMessage;                }

            } catch (IOException e) {
                errorMessage = "Server doesn\\'t exist or isn\\'t availible!";
            } finally {
                if (urlConnection != null) urlConnection.disconnect();
            }


        } else {
            errorMessage = "deviceID was null";
        }

        return errorMessage;
    }

    protected void onPostExecute(String errorMessage) {
        if (errorMessage != null){
            Toast.makeText(context, "Can't get answer from server! Wrong server?", Toast.LENGTH_SHORT).show();
            //showSendErrorDialog(effect, deviceID, main, errorMessage);
        }

    }
}
