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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OneColorDelayEffectConfigFragment extends Fragment implements View.OnClickListener, TextWatcher {

    private int color = Color.argb(255,255,255,255);
    private String delay = "50";

    private View colorView;
    private EditText delayTextEdit;


    String deviceID;
    Effect effect;
    Map<String, Object> effectConfig;

    SendStateHandler sendStateHandler;

    public OneColorDelayEffectConfigFragment(){}

    private void updateState(String delay) {
        Log.d("SendState", "state sent");

        if (sendStateHandler == null || sendStateHandler.getStatus() == AsyncTask.Status.FINISHED) {
            this.delay = delay;


            if (effectConfig != null){
                effectConfig.put(Constants.DELAY, delay + "ms");
                effect.setConfig(effectConfig);
            }
            sendStateHandler = new SendStateHandler(effect, deviceID, getActivity());
            sendStateHandler.execute();
        } else {
            updateOneColorDelayEffectView();
        }
    }


    public void updateOneColorDelayEffectView() {

        colorView.setBackgroundColor(color);
        delayTextEdit.setText(delay);
    }

    private void extractStateFromBundle (Bundle savedState) {
        if (savedState != null){
            effect = (Effect)savedState.getSerializable(Constants.EFFECT_STRING);
            deviceID = savedState.getString(Constants.DEVICE_ID);
            extractDataFromEffect();
        }

    }

    private void extractDataFromEffect(){
        if (effect.getConfig() != null) {
            effectConfig = effect.getConfig();
            String colorString = effectConfig.get("Color").toString();

            if (colorString.length() > 0) {
                try {
                    this.color = Integer.parseInt(colorString.substring(1), 16) | 0xFF000000;
                } catch (NumberFormatException e) {

                }
            }

            String delayString = effectConfig.get(Constants.DELAY).toString();

            Pattern pattern = Pattern.compile("(-)?(\\d+)");
            Matcher matcher = pattern.matcher(delayString);
            if (matcher.find()) {
                delay =  matcher.group(0);
            }
        } else {
            effectConfig = new HashMap<>();
        }
    }

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null){
            extractStateFromBundle(arguments);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if(savedInstanceState != null){
            extractStateFromBundle(savedInstanceState);
        } else {
            extractDataFromEffect();
        }
        final View view = inflater.inflate(R.layout.one_color_delay_effect_config_layout, container, false);

        colorView = view.findViewById(R.id.one_color_delay_effect_config_color);
        colorView.setOnClickListener(this);
        View colorButton = view.findViewById(R.id.one_color_delay_effect_config_color_button);
        colorButton.setOnClickListener(this);
        delayTextEdit = (EditText) view.findViewById(R.id.one_color_delay_effect_config_delay_editText);
        updateOneColorDelayEffectView();

        // Inflate the layout for this fragment
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
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.one_color_delay_effect_config_color_button:
            case R.id.one_color_delay_effect_config_color:
                color = ((ColorDrawable) colorView.getBackground()).getColor();
                ColorPickerFragment colorPickerFragment = new ColorPickerFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.DEVICE_ID, deviceID);
                bundle.putSerializable(Constants.COLOR_PICKABLE,new ColorPickableStatic(effect));
                colorPickerFragment.setArguments(bundle);
                ((Main)getActivity()).replaceFragmentAndAddToBackstack(colorPickerFragment);
                break;
        }

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
        //To change body of implemented methods use File | Settings | File Templates.
        updateState(s.toString());
    }
}