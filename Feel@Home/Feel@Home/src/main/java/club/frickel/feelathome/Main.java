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
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.net.Authenticator;
import java.net.PasswordAuthentication;


public class Main extends Activity {

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String username = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.USERNAME, "username");
        final String password = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.PASSWORD, "password");

        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password.toCharArray());
            }
        });
        setContentView(R.layout.main);
        if (savedInstanceState == null){
            replaceFragment(new DeviceListFragment());
        }


    }

    public void restartApplication (){
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        overridePendingTransition(0, 0);
        finish();
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void replaceFragment(Fragment fragment){
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    public void replaceFragmentAndAddToBackstack(Fragment fragment) {

        getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fragment_container, fragment).commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuitem_about:
                Toast.makeText(getApplicationContext(), "about", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menuitem_settings:
                replaceFragmentAndAddToBackstack(new Settings());
                return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }


}