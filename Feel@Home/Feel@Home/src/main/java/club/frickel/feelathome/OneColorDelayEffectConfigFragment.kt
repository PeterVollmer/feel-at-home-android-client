package club.frickel.feelathome

import android.app.Fragment
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import java.util.regex.Pattern


class OneColorDelayEffectConfigFragment : Fragment(), View.OnClickListener, TextWatcher {

    private var color = Color.argb(255, 255, 255, 255)
    private var delay = "50"

    private var colorView: View? = null
    private var delayTextEdit: EditText? = null


    internal var deviceID: String = ""
    internal var effect: Effect = Effect("", "", emptyMap())
    internal var effectConfig: MutableMap<String, Any> = emptyMap<String, Any>().toMutableMap()

    internal var sendStateHandler: SendStateHandler? = null

    private fun updateState(delay: String) {
        Log.d("SendState", "state sent")

        if (sendStateHandler == null || sendStateHandler!!.status === AsyncTask.Status.FINISHED) {
            this.delay = delay


            effectConfig.put(Constants.DELAY, delay + "ms")
            effect.config = effectConfig

            sendStateHandler = SendStateHandler(effect, deviceID, activity)
            sendStateHandler!!.execute()
        } else {
            updateOneColorDelayEffectView()
        }
    }


    fun updateOneColorDelayEffectView() {

        colorView!!.setBackgroundColor(color)
        delayTextEdit!!.setText(delay)
    }

    private fun extractStateFromBundle(savedState: Bundle?) {
        if (savedState != null) {
            effect = savedState.getSerializable(Constants.EFFECT_STRING) as Effect
            deviceID = savedState.getString(Constants.DEVICE_ID)
            extractDataFromEffect()
        }

    }

    private fun extractDataFromEffect() {
        effectConfig = effect.config.toMutableMap()
        val colorString = effectConfig["Color"].toString()

        if (colorString.isNotEmpty()) {
            try {
                this.color = Integer.parseInt(colorString.substring(1), 16) or 0xFF000000.toInt()
            } catch (e: NumberFormatException) {

            }

        }

        val delayString = effectConfig[Constants.DELAY].toString()

        val pattern = Pattern.compile("(-)?(\\d+)")
        val matcher = pattern.matcher(delayString)
        if (matcher.find()) {
            delay = matcher.group(0)
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
        val view = inflater.inflate(R.layout.one_color_delay_effect_config_layout, container, false)

        colorView = view.findViewById(R.id.one_color_delay_effect_config_color)
        colorView!!.setOnClickListener(this)
        val colorButton = view.findViewById(R.id.one_color_delay_effect_config_color_button)
        colorButton.setOnClickListener(this)
        delayTextEdit = view.findViewById(R.id.one_color_delay_effect_config_delay_editText) as EditText
        updateOneColorDelayEffectView()

        // Inflate the layout for this fragment
        return view
    }

    override fun onStart() {
        super.onStart()
        delayTextEdit!!.addTextChangedListener(this)
    }

    override fun onStop() {
        super.onStop()
        delayTextEdit!!.removeTextChangedListener(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(Constants.DEVICE_ID, deviceID)
        outState.putSerializable(Constants.EFFECT_STRING, effect)
        super.onSaveInstanceState(outState)
    }

    override fun onClick(v: View) {
        when (v.id) {

            R.id.one_color_delay_effect_config_color_button, R.id.one_color_delay_effect_config_color -> {
                color = (colorView!!.background as ColorDrawable).color
                val colorPickerFragment = ColorPickerFragment()
                val bundle = Bundle()
                bundle.putString(Constants.DEVICE_ID, deviceID)
                bundle.putSerializable(Constants.COLOR_PICKABLE, ColorPickableStatic(effect))
                colorPickerFragment.arguments = bundle
                (activity as Main).replaceFragmentAndAddToBackstack(colorPickerFragment)
            }
        }

    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(s: Editable) {
        //To change body of implemented methods use File | Settings | File Templates.
        updateState(s.toString())
    }
}