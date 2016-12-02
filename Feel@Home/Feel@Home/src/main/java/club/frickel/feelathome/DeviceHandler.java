package club.frickel.feelathome;

import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * User: Peter Vollmer
 * Date: 01.08.14
 * Time: 13:39
 */
public abstract class DeviceHandler extends AsyncTask<Void, String, ArrayList<Device>> {

    Context context;

    public DeviceHandler(Context context){
        this.context = context;
    }

    protected ArrayList<Device> doInBackground(Void... params) {
        ArrayList<Device> deviceList = null;

        String urlString = PreferenceManager.getDefaultSharedPreferences(context).getString(Constants.SERVER_URL, null);
        if (urlString != null) {
            try {
                URL url = new URL(urlString + "/devices");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(2000);
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                deviceList = new ObjectMapper().readValue(in, new TypeReference<ArrayList<Device>>() {
                });
            } catch (IOException e) {
                //e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                Log.d("DeviceHandler", "doInBackground: "+e.getMessage());
                return null;
            }

        }
        return deviceList;
    }

    protected void onProgressUpdate(String... error) {
        Toast.makeText(context, error[0], Toast.LENGTH_SHORT).show();
    }


    protected abstract void onPostExecute(ArrayList<Device> deviceList);
}
