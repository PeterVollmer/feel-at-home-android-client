package club.frickel.feelathome;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SVBar;

/**
 * User: Peter Vollmer
 * Date: 8/13/13
 * Time: 6:33 PM
 */
public class ColorPickerFragment extends Fragment implements ColorPicker.OnColorChangedListener {

    //private int staticColor;

    private String deviceID;
    ColorPickable colorPickable;

    SendStateHandler sendStateHandler;

    public ColorPickerFragment(){
        super();
    }

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null){
            colorPickable = (ColorPickable)arguments.getSerializable(Constants.COLOR_PICKABLE);
            deviceID = arguments.getString(Constants.DEVICE_ID);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(savedInstanceState != null){
            this.deviceID = savedInstanceState.getString(Constants.DEVICE_ID);
            this.colorPickable = (ColorPickableStatic) savedInstanceState.getSerializable(Constants.EFFECT_COLOR_PICKABLE);
        }

        final View view = inflater.inflate(R.layout.color_picker_layout, container, false);
        final ColorPicker picker = (ColorPicker) view.findViewById(R.id.picker);
        final SVBar svBar = (SVBar) view.findViewById(R.id.svbar);

        svBar.setColorPicker(picker);

        picker.addSVBar(svBar);

        picker.setColor(colorPickable.getColor());
        picker.setShowOldCenterColor(false);
        picker.setOnColorChangedListener(this);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(Constants.DEVICE_ID, deviceID);
        outState.putSerializable(Constants.EFFECT_COLOR_PICKABLE, colorPickable);
        super.onSaveInstanceState(outState);
    }

    private void updateState(int newColor) {
        if (sendStateHandler == null || sendStateHandler.getStatus() == AsyncTask.Status.FINISHED) {
            colorPickable.setColor(newColor);
            sendStateHandler = new SendStateHandler(colorPickable.getEffect(), deviceID, getActivity());
            sendStateHandler.execute();
        }

    }

    @Override
    public void onColorChanged(int i) {
        updateState(i);
    }
}