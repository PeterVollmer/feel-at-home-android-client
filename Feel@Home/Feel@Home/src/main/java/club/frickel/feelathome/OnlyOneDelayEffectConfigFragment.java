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
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;


public class OnlyOneDelayEffectConfigFragment extends Fragment implements TextWatcher {
    Main main;

    private String delay = "50 ms";

    private EditText delayTextEdit;

    private String deviceID;
    Effect effect;
    Map<String, Object> effectConfig;

    public OnlyOneDelayEffectConfigFragment(){}

    private void sendState() {
        if (effectConfig != null){
            effect.setConfig(effectConfig);
        }
        new SendStateHandler(effect, deviceID, main).execute();
    }

    private void updateOneDelayEffectView() {

        effectConfig = new HashMap<>();
        if (effect.getConfig() != null) {
            effectConfig = effect.getConfig();
            delay = effectConfig.get("Delay").toString();

        }
        delayTextEdit.setText(delay);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        main = (Main) activity;
    }

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null){
            effect = (Effect)arguments.getSerializable(Constants.EFFECT_STRING);
            deviceID = arguments.getString(Constants.DEVICE_ID);
        }
        sendState();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        if(savedInstanceState != null){
            this.deviceID = savedInstanceState.getString(Constants.DEVICE_ID);
            this.effect = (Effect)savedInstanceState.getSerializable(Constants.EFFECT_STRING);
        }

        final View view = inflater.inflate(R.layout.only_one_delay_config_layout, container, false);
        delayTextEdit = (EditText) view.findViewById(R.id.effect_config_delay_editText);

        updateOneDelayEffectView();

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
        delay = s.toString();
        effectConfig.put("Delay", delay);
        sendState();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}