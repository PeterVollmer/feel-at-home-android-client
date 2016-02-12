package club.frickel.feelathome;

import android.app.Fragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Peter Vollmer
 * Date: 3/15/13
 * Time: 2:35 AM
 */
public class OneColorEffectConfigFragment extends Fragment implements View.OnClickListener {

    private int staticColor = 0;

    private View staticColorView;

    String deviceID;
    Effect effect;
    Map<String, Object> effectConfig;

    public OneColorEffectConfigFragment(){}

    public void updateOneColorEffectView() {
        staticColorView.setBackgroundColor(staticColor);
    }

    private void extractStateFromBundle (Bundle savedState){
        if (savedState != null){
            effect = (Effect)savedState.getSerializable(Constants.EFFECT_STRING);
            deviceID = savedState.getString(Constants.DEVICE_ID);

            if (effect.getConfig() != null) {
                effectConfig = effect.getConfig();
                String topColorString = effectConfig.get("Color").toString();
                if (topColorString.length() > 0) {
                    try {
                        this.staticColor = Integer.parseInt(topColorString.substring(1), 16) | 0xFF000000;
                    } catch (NumberFormatException e) {

                    }
                }
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
            extractStateFromBundle(arguments);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        if(savedInstanceState != null){
            extractStateFromBundle(savedInstanceState);
        }
        final View view = inflater.inflate(R.layout.static_color_effect_config_layout, container, false);

        staticColorView = view.findViewById(R.id.static_color_effect_config_color);
        staticColorView.setOnClickListener(this);
        View staticColorButton = view.findViewById(R.id.static_color_effect_config_color_button);
        staticColorButton.setOnClickListener(this);

        updateOneColorEffectView();

        return view;
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

            case R.id.static_color_effect_config_color_button:
            case R.id.static_color_effect_config_color:
                staticColor = ((ColorDrawable) staticColorView.getBackground()).getColor();
                ColorPickerFragment colorPickerFragment = new ColorPickerFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.DEVICE_ID, deviceID);
                bundle.putSerializable(Constants.COLOR_PICKABLE,new ColorPickableStatic(effect));
                colorPickerFragment.setArguments(bundle);
                ((Main)getActivity()).replaceFragmentAndAddToBackstack(colorPickerFragment);
                break;
        }

    }
}
