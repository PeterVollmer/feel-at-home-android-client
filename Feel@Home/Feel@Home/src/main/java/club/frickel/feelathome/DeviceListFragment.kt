package club.frickel.feelathome

import android.app.AlertDialog
import android.app.ListFragment
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Switch
import android.widget.TextView
import com.fasterxml.jackson.databind.ObjectMapper
import java.util.*

class DeviceListFragment : ListFragment() {
    internal lateinit var mapper: ObjectMapper
    internal var serverNotAvailDialog: AlertDialog? = null
    internal var serverNotAvailDialogWasOn = false

    private inner class DeviceArrayAdapter(context: Context, textViewResourceId: Int, private val deviceList: ArrayList<Device>) : ArrayAdapter<Device>(context, textViewResourceId, deviceList) {

        private inner class ViewHolder {
            internal var deviceName: TextView? = null
            internal var active: Switch? = null
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var convertView = convertView

            val holder: ViewHolder
            //Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {

                val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

                convertView = inflater.inflate(R.layout.device_list_element_layout_switch, null)
                holder = ViewHolder()
                holder.deviceName = convertView!!.findViewById(R.id.deviceListElementTextView) as TextView
                holder.active = convertView.findViewById(R.id.deviceListElementActiveSwitch) as Switch
                convertView.tag = holder

                val myListener = View.OnClickListener { v ->
                    val device: Device
                    when (v.id) {
                        R.id.deviceListElementActiveSwitch -> {
                            val sw = v as Switch
                            device = sw.tag as Device
                            device.isActive = sw.isChecked
                            SendActiveHandler(sw.isChecked, device.id, activity).execute()
                        }
                        R.id.deviceListElementTextView -> {
                            val textView = v as TextView
                            device = textView.tag as Device
                            val deviceFragment = DeviceFragment()
                            val arguments = Bundle()
                            arguments.putString(Constants.DEVICE_ID, device.id)
                            deviceFragment.arguments = arguments
                            (activity as Main).replaceFragmentAndAddToBackstack(deviceFragment)
                        }
                    }
                }
                holder.active!!.setOnClickListener(myListener)
                holder.deviceName!!.setOnClickListener(myListener)

            } else {

                holder = convertView.tag as ViewHolder
            }

            val device = deviceList[position]
            holder.deviceName!!.tag = device
            holder.deviceName!!.text = device.name
            holder.active!!.isChecked = device.isActive
            holder.active!!.tag = device

            return convertView

        }

    }

    private inner class PrivateDeviceHandler(context: Context) : DeviceHandler(context) {
        override fun onPostExecute(deviceList: ArrayList<Device>) {
            if (deviceList == null) {
                //Toast.makeText(context, "Can't get answer from server! Wrong server?", Toast.LENGTH_SHORT).show();

                serverNotAvailDialog = createServerNotAvailableErrorDialog(context)
                serverNotAvailDialog!!.show()

            } else {
                updateDeviceListFragment(deviceList)
            }
        }


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mapper = ObjectMapper()


    }

    private fun createServerNotAvailableErrorDialog(context: Context): AlertDialog {
        val builder = AlertDialog.Builder(context)

        builder.setTitle(R.string.receive_error_dialog_title)
        builder.setCancelable(false)
        builder.setMessage(R.string.receive_error_dialog_message)
                .setPositiveButton(R.string.receive_error_dialog_retry) { dialog, id -> PrivateDeviceHandler(activity.applicationContext).execute() }
                .setNegativeButton(R.string.receive_error_dialog_settings) { dialog, id -> (activity as Main).replaceFragmentAndAddToBackstack(Settings()) }

        // Create the AlertDialog object and return it

        return builder.create()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (savedInstanceState == null) {
            updateDeviceListFragment(ArrayList<Device>())
        }
        return super.onCreateView(inflater, container, savedInstanceState)

    }

    override fun onStop() {
        super.onStop()
        if (serverNotAvailDialog != null) {
            serverNotAvailDialog!!.cancel()
            serverNotAvailDialogWasOn = true
        }
    }


    override fun onResume() {
        super.onResume()
        if (serverNotAvailDialogWasOn) {
            serverNotAvailDialog = createServerNotAvailableErrorDialog(activity)
            serverNotAvailDialog!!.show()
        } else {
            PrivateDeviceHandler(activity.applicationContext).execute()
        }
    }


    fun updateDeviceListFragment(deviceList: ArrayList<Device>) {
        listAdapter = DeviceArrayAdapter(activity, R.layout.device_list_layout, deviceList)
    }

}