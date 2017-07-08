package club.frickel.feelathome

import android.app.Fragment
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import java.util.*
import java.util.regex.Pattern


class FireEffectConfigFragment : Fragment(), TextWatcher, SeekBar.OnSeekBarChangeListener {

    private var delay = "50"

    private var cooling = 20
    private var spark = 50

    private var delayTextEdit: EditText? = null

    internal lateinit var coolingSeekbarDisplayTextView: TextView
    internal lateinit var sparkSeekbarDisplayTextView: TextView

    private var coolingSeekBar: SeekBar? = null
    private var sparkSeekBar: SeekBar? = null

    private var deviceID: String = ""
    internal var effect: Effect = Effect("", "", emptyMap())
    internal var effectConfig: MutableMap<String, Any> = emptyMap<String, Any>().toMutableMap()

    internal lateinit var sendStateHandler: SendStateHandler


    private fun updateState(cooling: Int, spark: Int, delay: String) {
        Log.d("SendState", "state sent")
        if (sendStateHandler == null || sendStateHandler.status === AsyncTask.Status.FINISHED) {
            this.delay = delay
            this.cooling = cooling
            this.spark = spark

            effectConfig.put(Constants.DELAY, delay + "ms")
            effectConfig.put(Constants.COOLING, cooling)
            effectConfig.put(Constants.SPARK, spark)
            effect.config = effectConfig

            sendStateHandler = SendStateHandler(effect, deviceID, activity)
            sendStateHandler.execute()
        } else {
            updateFireEffectView()
        }
    }


    private fun updateFireEffectView() {
        coolingSeekbarDisplayTextView.text = this.cooling.toString()
        sparkSeekbarDisplayTextView.text = this.spark.toString()
        coolingSeekBar!!.progress = this.cooling
        sparkSeekBar!!.progress = this.cooling

        delayTextEdit!!.setText(this.delay)
    }


    private fun extractStateFromBundle(savedState: Bundle?) {

        if (savedState != null) {
            effect = savedState.getSerializable(Constants.EFFECT_STRING) as Effect
            deviceID = savedState.getString(Constants.DEVICE_ID)

            if (effect.config != null) {
                effectConfig = effect.config.toMutableMap()

                val delayString = effectConfig[Constants.DELAY].toString()

                val pattern = Pattern.compile("(-)?(\\d+)")
                val matcher = pattern.matcher(delayString)
                if (matcher.find()) {
                    delay = matcher.group(0)
                }
                cooling = Integer.parseInt(effectConfig[Constants.COOLING].toString())
                spark = Integer.parseInt(effectConfig[Constants.SPARK].toString())

            } else {
                effectConfig = HashMap<String, Any>()
            }
        }


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            extractStateFromBundle(arguments)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        if (savedInstanceState != null) {
            extractStateFromBundle(savedInstanceState)
        }

        val view = inflater.inflate(R.layout.fire_effect_config_layout, container, false)
        delayTextEdit = view.findViewById(R.id.fire_effect_config_delay_editText) as EditText
        coolingSeekBar = view.findViewById(R.id.fire_effect_config_cooling_seekbar) as SeekBar
        sparkSeekBar = view.findViewById(R.id.fire_effect_config_spark_seekbar) as SeekBar

        coolingSeekbarDisplayTextView = view.findViewById(R.id.fire_effect_config_cooling_seekbar_display_textview) as TextView
        sparkSeekbarDisplayTextView = view.findViewById(R.id.fire_effect_config_spark_seekbar_display_textview) as TextView

        coolingSeekBar!!.setOnSeekBarChangeListener(this)
        sparkSeekBar!!.setOnSeekBarChangeListener(this)

        updateFireEffectView()

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

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable) {
        updateState(cooling, spark, s.toString())
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int,
                                   fromUser: Boolean) {
        when (seekBar.id) {
            R.id.fire_effect_config_cooling_seekbar -> coolingSeekbarDisplayTextView.text = (seekBar.progress + 20).toString()
            R.id.fire_effect_config_spark_seekbar -> sparkSeekbarDisplayTextView.text = (seekBar.progress + 50).toString()
        }

    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
        // Notify that the user has started a touch gesture.
        //textView.setText(textView.getText()+"\n"+"SeekBar Touch Started");
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        // Notify that the user has finished a touch gesture.
        //textView.setText(textView.getText()+"\n"+"SeekBar Touch Stopped");
        when (seekBar.id) {
            R.id.fire_effect_config_cooling_seekbar -> updateState(seekBar.progress, spark, delay)
            R.id.fire_effect_config_spark_seekbar -> updateState(cooling, seekBar.progress, delay)
        }

    }
}