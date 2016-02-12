/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package club.frickel.feelathome;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DeviceFragment extends ListFragment {

    private String deviceID = null;
    private ObjectMapper mapper;

    private class EffectsHandler extends AsyncTask<Void, Void, ArrayList<Effect>> {

        private URL url = null;
        private HttpURLConnection urlConnection = null;
        private ArrayList<Effect> result = null;
        private String urlString;

        protected ArrayList<Effect> doInBackground(Void... params) {
            urlString = getActivity().getSharedPreferences(Constants.SHAREDPREFERENCES, Activity.MODE_PRIVATE).getString(Constants.SERVER_URL, null);
            if (urlString != null) {
                try {
                    url = new URL(urlString + "/devices/" + deviceID + "/available");
                    urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    result = mapper.readValue(in, new TypeReference<ArrayList<Effect>>() {
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }

        protected void onPostExecute(ArrayList<Effect> result) {
            if (result == null) {
                Toast.makeText(getActivity().getApplicationContext(), "Can't get answer from server! Wrong server?", Toast.LENGTH_SHORT).show();
            } else {
                updateEffectsFragment(result);
            }

        }

    }

    private class CurrentEffectConfigHandler extends AsyncTask<Void, Void, Effect> {

        private Effect standardConfigEffect;

        CurrentEffectConfigHandler(Effect standardConfigEffect) {
            this.standardConfigEffect = standardConfigEffect;
        }


        @Override
        protected Effect doInBackground(Void... params) {
            Effect currentEffect = null;
            String urlString = getActivity().getSharedPreferences(Constants.SHAREDPREFERENCES, Activity.MODE_PRIVATE).getString(Constants.SERVER_URL, null);
            if (urlString != null) {
                try {
                    URL url = new URL(urlString + "/devices/" + deviceID + "/effect");
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    currentEffect = mapper.readValue(in, new TypeReference<Effect>() {
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return currentEffect;
        }


        protected void onPostExecute(Effect currentEffect) {

            if (currentEffect != null && standardConfigEffect.getName().equals(currentEffect.getName()))
                onEffectSelected(currentEffect);
            else onEffectSelected(standardConfigEffect);
        }
    }

    public DeviceFragment() {
        mapper = new ObjectMapper();
    }

    private void onEffectSelected(Effect effect) {
        Fragment effectConfigFragment = null;
        assert effect.getConfig() != null;
        switch (effect.getName()) {
            case "Wheel":
            case "Wheel2":
            case "Rainbow":
            case "Sunrise":
            case "Stroboscope":
            case "Whitefade":
            case "Random Brightness":
            case "Random Fade":
            case "Random Color":
                effectConfigFragment = new OnlyOneDelayEffectConfigFragment();
                break;
            case "Color":
            case "Clock Color":
                effectConfigFragment = new OneColorEffectConfigFragment();
                break;
            case "Brightness":
            case "Brightness Scaling":
                effectConfigFragment = new BrightnessEffectConfigFragment();
                break;
            case "Colorfade":
                effectConfigFragment = new OneColorDelayEffectConfigFragment();
                break;
            case "Fire":
                effectConfigFragment = new FireEffectConfigFragment();
                break;
            case "Power": {
                effect.getConfig().put("Power", true);
                break;
            }
        }
        new SendStateHandler(effect, deviceID, getActivity()).execute();

        if (effectConfigFragment != null) {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.DEVICE_ID, deviceID);
            bundle.putSerializable(Constants.EFFECT_STRING, effect);
            effectConfigFragment.setArguments(bundle);
            ((Main)getActivity()).replaceFragmentAndAddToBackstack(effectConfigFragment);
        }
    }

    public void updateEffectsFragment(ArrayList<Effect> effects) {
        // We need to use a different list item layout for devices older than Honeycomb
        int layout = android.R.layout.simple_list_item_activated_1;

        // Create an array adapter for the list view, using the Ipsum headlines array
        setListAdapter(new ArrayAdapter<>(getActivity(), layout, effects));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null){
            deviceID = arguments.getString(Constants.DEVICE_ID);
        }

        updateEffectsFragment(new ArrayList<Effect>());
        new EffectsHandler().execute();

    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(Constants.DEVICE_ID, this.deviceID);
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        new CurrentEffectConfigHandler(((ArrayAdapter<Effect>) getListAdapter()).getItem(position)).execute();

        // Set the item as checked to be highlighted when in two-pane layout
        getListView().setItemChecked(position, true);
    }
}