package club.frickel.feelathome;

/*
 * Copyright (C) 2008 OpenIntents.org
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

//package org.openintents.widget;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class BrightnessEffectConfigFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {


    SeekBar seekBar;
    TextView textView;

    private int brightness = 0;

    private String deviceID;
    Effect effect;
    Map<String, Object> effectConfig;

    SendStateHandler sendStateHandler;

    private void updateState(int brightness) {
        if (sendStateHandler == null || sendStateHandler.getStatus() == AsyncTask.Status.FINISHED) {
            this.brightness = brightness;

            if (effectConfig != null){
                effectConfig.put(Constants.BRIGHTNESS, brightness);
                effect.setConfig(effectConfig);
            }
            sendStateHandler = new SendStateHandler(effect, deviceID, getActivity());
            sendStateHandler.execute();
        } else {
            textView.setText(String.valueOf(this.brightness));
            seekBar.setProgress(this.brightness);
        }
    }

    private void extractStateFromBundle(Bundle savedState){
        if (savedState != null){
            effect = (Effect)savedState.getSerializable(Constants.EFFECT_STRING);
            deviceID = savedState.getString(Constants.DEVICE_ID);

            if (effect.getConfig() != null) {
                effectConfig = effect.getConfig();
                brightness = (Integer)effectConfig.get(Constants.BRIGHTNESS);
            } else {
                effectConfig = new HashMap<>();
            }
        }
    }

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null){
            extractStateFromBundle(getArguments());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (savedInstanceState != null) {
            extractStateFromBundle(savedInstanceState);
        }
        final View view = inflater.inflate(R.layout.brightness_effect_config_layout, container, false);

        seekBar = (SeekBar) view.findViewById(R.id.seekbar);
        textView = (TextView) view.findViewById(R.id.brightness_textview);
        seekBar.setProgress(brightness);
        seekBar.setOnSeekBarChangeListener(this);

        textView.setText(String.valueOf(brightness));


        return view;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(Constants.DEVICE_ID, deviceID);
        outState.putSerializable(Constants.EFFECT_STRING,effect);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
        textView.setText(String.valueOf(seekBar.getProgress()));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // Notify that the user has started a touch gesture.
        //textView.setText(textView.getText()+"\n"+"SeekBar Touch Started");
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // Notify that the user has finished a touch gesture.
        //textView.setText(textView.getText()+"\n"+"SeekBar Touch Stopped");
        updateState(seekBar.getProgress());
    }

}