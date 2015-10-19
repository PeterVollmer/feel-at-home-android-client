package club.frickel.feelathome;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Peter Vollmer
 * Date: 3/13/13
 * Time: 1:39 PM
 */
public class SendActiveHandler extends AsyncTask<Void, Void, String> {
    Boolean active;
    String deviceID;
    Context context;

    public static final String SEND_ERROR = "Send Error";

    public SendActiveHandler(Boolean active, String deviceID, Context context) {
        this.active = active;
        this.context = context;
        this.deviceID = deviceID;
    }

    protected String doInBackground(Void... params) {
        String errorMessage = null;
        if (deviceID != null) {
            String urlString = context.getSharedPreferences(Constants.SHAREDPREFERENCES, Activity.MODE_PRIVATE).getString(Constants.SERVER_URL, null) + "/devices/" + deviceID + "/active";
            URL url;

            try {
                url = new URL(urlString);
            } catch (MalformedURLException e) {
                errorMessage =  "Malformed URL: " + urlString;
                return errorMessage;
            }

            HttpURLConnection urlConnection;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("PUT");
                ObjectMapper mapper = new ObjectMapper();
                Map<String,Object> activeObject = new HashMap<>();
                activeObject.put("Active",active);
                mapper.writeValue(urlConnection.getOutputStream(), activeObject);
                int responce = urlConnection.getResponseCode();
                if (responce != 200) {

                    errorMessage = "Received ErrorCode: " + String.valueOf(responce);
                    Log.d(SEND_ERROR, "Received " + responce + " Error while sending");
                }

            } catch (IOException e) {
                errorMessage = "Server doesn\\'t exist or isn\\'t availible!";
                return errorMessage;
            }


        } else {
            errorMessage = "deviceID was null";
        }

        return errorMessage;
    }

    protected void onPostExecute(String errorMessage) {
        if (errorMessage != null){
            Log.d("Send Error", "State Error"+ errorMessage);
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
            //showSendErrorDialog(active, deviceID, context, errorMessage);
        }

    }
}
