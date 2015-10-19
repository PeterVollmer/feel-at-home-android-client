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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


public class Settings extends Fragment {

    EditText urlEditText;

    SharedPreferences sharedPreferences;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(Constants.SHAREDPREFERENCES,Main.MODE_PRIVATE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.settings, container, false);

        String serverURL;
        if (savedInstanceState != null && savedInstanceState.containsKey(Constants.SERVER_URL)){
            serverURL = savedInstanceState.getString(Constants.SERVER_URL,null);
        } else {
            serverURL = sharedPreferences.getString(Constants.SERVER_URL, "");
        }
        if (serverURL==null) {
            serverURL = "";
        }
        urlEditText = (EditText) view.findViewById(R.id.urlEditText);
        urlEditText.setText(serverURL);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sharedPreferences.edit().putString(Constants.SERVER_URL,urlEditText.getText().toString()).apply();
        ((Main)getActivity()).restartApplication();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(Constants.SERVER_URL, urlEditText.getText().toString());
        super.onSaveInstanceState(outState);

    }

}