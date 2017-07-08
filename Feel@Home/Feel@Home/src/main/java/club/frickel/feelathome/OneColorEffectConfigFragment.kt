package club.frickel.feelathome

import android.app.Fragment
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.util.HashMap


class OneColorEffectConfigFragment : Fragment(), View.OnClickListener {

    private var staticColor = 0

    private var staticColorView: View? = null

    internal var deviceID: String = ""
    internal var effect: Effect = Effect("", "", emptyMap())
    internal var effectConfig: Map<String, Any> = emptyMap<String, Any>().toMutableMap()

    fun updateOneColorEffectView() {
        staticColorView!!.setBackgroundColor(staticColor)
    }

    private fun extractStateFromBundle(savedState: Bundle?) {
        if (savedState != null) {
            effect = savedState.getSerializable(Constants.EFFECT_STRING) as Effect
            deviceID = savedState.getString(Constants.DEVICE_ID)
            extractDataFromEffect()

        }
    }

    private fun extractDataFromEffect() {
        if (effect.config != null) {
            effectConfig = effect.config
            val topColorString = effectConfig["Color"].toString()
            if (topColorString.length > 0) {
                try {
                    this.staticColor = Integer.parseInt(topColorString.substring(1), 16) or 0xFF000000.toInt()
                } catch (e: NumberFormatException) {

                }

            }
        } else {
            effectConfig = HashMap<String, Any>()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arguments = arguments
        if (arguments != null) {
            extractStateFromBundle(arguments)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        if (savedInstanceState != null) {
            extractStateFromBundle(savedInstanceState)
        } else {
            extractDataFromEffect()
        }
        val view = inflater.inflate(R.layout.static_color_effect_config_layout, container, false)

        staticColorView = view.findViewById(R.id.static_color_effect_config_color)
        staticColorView!!.setOnClickListener(this)
        val staticColorButton = view.findViewById(R.id.static_color_effect_config_color_button)
        staticColorButton.setOnClickListener(this)

        updateOneColorEffectView()

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(Constants.DEVICE_ID, deviceID)
        outState.putSerializable(Constants.EFFECT_STRING, effect)
        super.onSaveInstanceState(outState)
    }

    override fun onClick(v: View) {
        when (v.id) {

            R.id.static_color_effect_config_color_button, R.id.static_color_effect_config_color -> {
                staticColor = (staticColorView!!.background as ColorDrawable).color
                val colorPickerFragment = ColorPickerFragment()
                val bundle = Bundle()
                bundle.putString(Constants.DEVICE_ID, deviceID)
                bundle.putSerializable(Constants.COLOR_PICKABLE, ColorPickableStatic(effect))
                colorPickerFragment.arguments = bundle
                (activity as Main).replaceFragmentAndAddToBackstack(colorPickerFragment)
            }
        }

    }
}