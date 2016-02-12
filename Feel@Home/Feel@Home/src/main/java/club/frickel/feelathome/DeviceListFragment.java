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


import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

public class DeviceListFragment extends ListFragment {
    ObjectMapper mapper;


    private class DeviceArrayAdapter extends ArrayAdapter<Device> {



        private ArrayList<Device> deviceList;

        public DeviceArrayAdapter(Context context, int textViewResourceId, ArrayList<Device> deviceList) {
            super(context, textViewResourceId, deviceList);
            this.deviceList = deviceList;
        }

        private class ViewHolder {
            TextView deviceName;
            Switch active;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            //Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {

                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                convertView = inflater.inflate(R.layout.device_list_element_layout_switch, null);
                holder = new ViewHolder();
                holder.deviceName = (TextView) convertView.findViewById(R.id.deviceListElementTextView);
                holder.active = (Switch) convertView.findViewById(R.id.deviceListElementActiveSwitch);
                convertView.setTag(holder);

                View.OnClickListener myListener = new View.OnClickListener(){
                    public void onClick(View v){
                        Device device;
                        switch (v.getId()){
                            case (R.id.deviceListElementActiveSwitch):
                                Switch sw = (Switch) v;
                                device = (Device) sw.getTag();
                                device.setActive(sw.isChecked());
                                new SendActiveHandler(sw.isChecked(), device.getId(), getActivity()).execute();
                                break;
                            case R.id.deviceListElementTextView:
                                TextView textView = (TextView) v;
                                device = (Device)textView.getTag();
                                DeviceFragment deviceFragment = new DeviceFragment();
                                Bundle arguments = new Bundle();
                                arguments.putString(Constants.DEVICE_ID, device.getId());
                                deviceFragment.setArguments(arguments);
                                ((Main)getActivity()).replaceFragmentAndAddToBackstack(deviceFragment);
                                break;

                        }
                    }
                };
                holder.active.setOnClickListener(myListener);
                holder.deviceName.setOnClickListener(myListener);

            } else {

                holder = (ViewHolder) convertView.getTag();
            }

            Device device = deviceList.get(position);
            holder.deviceName.setTag(device);
            holder.deviceName.setText(device.getName());
            holder.active.setChecked(device.isActive());
            holder.active.setTag(device);

            return convertView;

        }

    }

    private class PrivateDeviceHandler extends DeviceHandler {


        public PrivateDeviceHandler(Context context) {
            super(context);
        }

        @Override
        protected void onPostExecute(ArrayList<Device> deviceList) {
            if (deviceList == null) {
                Toast.makeText(context, "Can't get answer from server! Wrong server?", Toast.LENGTH_SHORT).show();
            } else {
                updateDeviceListFragment(deviceList);
            }
        }
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapper = new ObjectMapper();


    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        if (savedInstanceState == null){
            updateDeviceListFragment(new ArrayList<Device>());
        }
        new PrivateDeviceHandler(getActivity().getApplicationContext()).execute();

        return super.onCreateView(inflater,container,savedInstanceState);

    }

    @Override
    public void onResume(){
        super.onResume();
        new PrivateDeviceHandler(getActivity().getApplicationContext()).execute();
    }


    public void updateDeviceListFragment(ArrayList<Device> deviceList) {
        setListAdapter(new DeviceArrayAdapter(getActivity(), R.layout.device_list_layout, deviceList));
    }

}