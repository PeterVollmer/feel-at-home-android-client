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

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FireEffectConfigFragment extends Fragment implements TextWatcher, SeekBar.OnSeekBarChangeListener {

    private String delay = "50";

    private int cooling = 20;
    private int spark = 50;

    private EditText delayTextEdit;

    TextView coolingSeekbarDisplayTextView;
    TextView sparkSeekbarDisplayTextView;

    private SeekBar coolingSeekBar;
    private SeekBar sparkSeekBar;

    private String deviceID;
    Effect effect;
    Map<String, Object> effectConfig;

    SendStateHandler sendStateHandler;


    private void updateState(int cooling, int spark, String delay) {
        Log.d("SendState", "state sent");
        if (sendStateHandler == null || sendStateHandler.getStatus() == AsyncTask.Status.FINISHED) {
            this.delay = delay;
            this.cooling = cooling;
            this.spark = spark;


            if (effectConfig != null){
                effectConfig.put(Constants.DELAY, delay + "ms");
                effectConfig.put(Constants.COOLING, cooling);
                effectConfig.put(Constants.SPARK, spark);
                effect.setConfig(effectConfig);
            }
            sendStateHandler = new SendStateHandler(effect, deviceID, getActivity());
            sendStateHandler.execute();
        } else {
            updateFireEffectView();
        }
    }



    private void updateFireEffectView(){
        coolingSeekbarDisplayTextView.setText(String.valueOf(this.cooling));
        sparkSeekbarDisplayTextView.setText(String.valueOf(this.spark));
        coolingSeekBar.setProgress(this.cooling);
        sparkSeekBar.setProgress(this.cooling);

        delayTextEdit.setText(this.delay);
    }

    public FireEffectConfigFragment(){}


    private void extractStateFromBundle (Bundle savedState){

        if (savedState != null){
            effect = (Effect)savedState.getSerializable(Constants.EFFECT_STRING);
            deviceID = savedState.getString(Constants.DEVICE_ID);

            if (effect.getConfig() != null) {
                effectConfig = effect.getConfig();

                String delayString = effectConfig.get(Constants.DELAY).toString();

                Pattern pattern = Pattern.compile("(-)?(\\d+)");
                Matcher matcher = pattern.matcher(delayString);
                if (matcher.find()) {
                    delay =  matcher.group(0);
                }
                cooling = Integer.parseInt(effectConfig.get(Constants.COOLING).toString());
                spark = Integer.parseInt(effectConfig.get(Constants.SPARK).toString());

            } else {
                effectConfig = new HashMap<>();
            }
        }


    }


    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            extractStateFromBundle(getArguments());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        if(savedInstanceState != null){
            extractStateFromBundle(savedInstanceState);
        }

        final View view = inflater.inflate(R.layout.fire_effect_config_layout, container, false);
        delayTextEdit = (EditText) view.findViewById(R.id.fire_effect_config_delay_editText);
        coolingSeekBar = (SeekBar) view.findViewById(R.id.fire_effect_config_cooling_seekbar);
        sparkSeekBar = (SeekBar) view.findViewById(R.id.fire_effect_config_spark_seekbar);

        coolingSeekbarDisplayTextView = (TextView) view.findViewById(R.id.fire_effect_config_cooling_seekbar_display_textview);
        sparkSeekbarDisplayTextView = (TextView) view.findViewById(R.id.fire_effect_config_spark_seekbar_display_textview);

        coolingSeekBar.setOnSeekBarChangeListener(this);
        sparkSeekBar.setOnSeekBarChangeListener(this);

        updateFireEffectView();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        delayTextEdit.addTextChangedListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        delayTextEdit.removeTextChangedListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(Constants.DEVICE_ID, deviceID);
        outState.putSerializable(Constants.EFFECT_STRING,effect);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        updateState(cooling,spark,s.toString());
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
        switch (seekBar.getId()){
            case R.id.fire_effect_config_cooling_seekbar:
                coolingSeekbarDisplayTextView.setText(String.valueOf(seekBar.getProgress()+20));
                break;
            case R.id.fire_effect_config_spark_seekbar:
                sparkSeekbarDisplayTextView.setText(String.valueOf(seekBar.getProgress()+50));
                break;
        }

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
        switch (seekBar.getId()){
            case R.id.fire_effect_config_cooling_seekbar:
                updateState(seekBar.getProgress(),spark, delay);
                break;
            case R.id.fire_effect_config_spark_seekbar:
                updateState(cooling,seekBar.getProgress(),delay);
                break;
        }

    }
}