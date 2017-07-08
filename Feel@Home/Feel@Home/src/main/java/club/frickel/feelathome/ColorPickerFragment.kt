package club.frickel.feelathome

import android.app.Fragment
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.larswerkman.holocolorpicker.ColorPicker
import com.larswerkman.holocolorpicker.SVBar


class ColorPickerFragment : Fragment(), ColorPicker.OnColorChangedListener {

    //private int staticColor;

    private var deviceID: String? = null
    internal lateinit var colorPickable: ColorPickable

    internal var sendStateHandler: SendStateHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arguments = arguments
        if (arguments != null) {
            colorPickable = arguments.getSerializable(Constants.COLOR_PICKABLE) as ColorPickable
            deviceID = arguments.getString(Constants.DEVICE_ID)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if (savedInstanceState != null) {
            this.deviceID = savedInstanceState.getString(Constants.DEVICE_ID)
            this.colorPickable = savedInstanceState.getSerializable(Constants.EFFECT_COLOR_PICKABLE) as ColorPickableStatic
        }

        val view = inflater.inflate(R.layout.color_picker_layout, container, false)
        val picker = view.findViewById(R.id.picker) as ColorPicker
        val svBar = view.findViewById(R.id.svbar) as SVBar

        svBar.setColorPicker(picker)

        picker.addSVBar(svBar)

        picker.color = colorPickable.getColor()
        picker.showOldCenterColor = false
        picker.onColorChangedListener = this

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(Constants.DEVICE_ID, deviceID)
        outState.putSerializable(Constants.EFFECT_COLOR_PICKABLE, colorPickable)
        super.onSaveInstanceState(outState)
    }

    private fun updateState(newColor: Int) {
        if (sendStateHandler == null || sendStateHandler!!.status === AsyncTask.Status.FINISHED) {
            colorPickable.setColor(newColor)
            sendStateHandler = SendStateHandler(colorPickable.effect, deviceID, activity)
            sendStateHandler!!.execute()
        }

    }

    override fun onColorChanged(i: Int) {
        updateState(i)
    }
}