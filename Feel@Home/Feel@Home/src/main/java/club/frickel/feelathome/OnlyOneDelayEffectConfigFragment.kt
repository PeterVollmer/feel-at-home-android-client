package club.frickel.feelathome

import android.app.Fragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import java.util.*
import java.util.regex.Pattern


class OnlyOneDelayEffectConfigFragment : Fragment(), TextWatcher {

    private var delay = "50"

    private lateinit var delayTextEdit: EditText

    private var deviceID: String? = null

    internal var effect: Effect = Effect("", "", emptyMap())
    internal var effectConfig: MutableMap<String, Any> = emptyMap<String, Any>().toMutableMap()

    private fun sendState() {
        if (effectConfig != null) {
            effect.config = effectConfig
        }
        if (effect != null) SendStateHandler(effect, deviceID, activity).execute()
    }

    private fun updateOneDelayEffectView() {

        effectConfig = HashMap<String, Any>()
        if (effect.config != null) {
            effectConfig = effect.config.toMutableMap()
            val delayString = effectConfig!!["Delay"].toString()
            val pattern = Pattern.compile("(-)?(\\d+)")
            val matcher = pattern.matcher(delayString)
            if (matcher.find()) {
                delay = matcher.group(0)
            }
        }
        delayTextEdit.setText(delay)

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arguments = arguments
        if (arguments != null) {
            effect = arguments.getSerializable(Constants.EFFECT_STRING) as Effect
            deviceID = arguments.getString(Constants.DEVICE_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        if (savedInstanceState != null) {
            this.deviceID = savedInstanceState.getString(Constants.DEVICE_ID)
            this.effect = savedInstanceState.getSerializable(Constants.EFFECT_STRING) as Effect
        }

        val view = inflater.inflate(R.layout.only_one_delay_config_layout, container, false)
        delayTextEdit = view.findViewById(R.id.effect_config_delay_editText) as EditText

        updateOneDelayEffectView()

        return view
    }

    override fun onStart() {
        super.onStart()
        delayTextEdit.addTextChangedListener(this)
    }

    override fun onStop() {
        super.onStop()
        delayTextEdit.removeTextChangedListener(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(Constants.DEVICE_ID, deviceID)
        outState.putSerializable(Constants.EFFECT_STRING, effect)
        super.onSaveInstanceState(outState)
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        delay = s.toString()
        effectConfig.put("Delay", delay + "ms")
        sendState()
    }

    override fun afterTextChanged(s: Editable) {

    }
}