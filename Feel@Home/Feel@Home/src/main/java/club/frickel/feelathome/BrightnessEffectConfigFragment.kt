package club.frickel.feelathome

import android.app.Fragment
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView

class BrightnessEffectConfigFragment : Fragment(), SeekBar.OnSeekBarChangeListener {


    internal lateinit var seekBar: SeekBar
    internal lateinit var textView: TextView

    private var brightness = 0

    private var deviceID: String = ""
    internal var effect: Effect = Effect("", "", emptyMap())
    internal var effectConfig: MutableMap<String, Any> = emptyMap<String, Any>().toMutableMap()
    internal var sendStateHandler: SendStateHandler? = null

    private fun updateState(brightness: Int) {
        if (sendStateHandler == null || sendStateHandler!!.status === AsyncTask.Status.FINISHED) {
            this.brightness = brightness


            effectConfig.put(Constants.BRIGHTNESS, brightness)
            effect.config = effectConfig

            sendStateHandler = SendStateHandler(effect, deviceID, activity)
            sendStateHandler!!.execute()
        } else {
            textView.text = this.brightness.toString()
            seekBar.progress = this.brightness
        }
    }

    private fun extractStateFromBundle(savedState: Bundle?) {
        if (savedState != null) {
            effect = savedState.getSerializable(Constants.EFFECT_STRING) as Effect
            deviceID = savedState.getString(Constants.DEVICE_ID)


            effectConfig = effect.config.toMutableMap()
            brightness = effectConfig[Constants.BRIGHTNESS] as Int

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arguments = arguments
        if (arguments != null) {
            extractStateFromBundle(getArguments())
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        if (savedInstanceState != null) {
            extractStateFromBundle(savedInstanceState)
        }
        val view = inflater.inflate(R.layout.brightness_effect_config_layout, container, false)

        seekBar = view.findViewById(R.id.seekbar) as SeekBar
        textView = view.findViewById(R.id.brightness_textview) as TextView
        seekBar.progress = brightness
        seekBar.setOnSeekBarChangeListener(this)

        textView.text = brightness.toString()


        return view
    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(Constants.DEVICE_ID, deviceID)
        outState.putSerializable(Constants.EFFECT_STRING, effect)
        super.onSaveInstanceState(outState)
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int,
                                   fromUser: Boolean) {
        textView.text = seekBar.progress.toString()
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
        // Notify that the user has started a touch gesture.
        //textView.setText(textView.getText()+"\n"+"SeekBar Touch Started");
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        // Notify that the user has finished a touch gesture.
        //textView.setText(textView.getText()+"\n"+"SeekBar Touch Stopped");
        updateState(seekBar.progress)
    }

}