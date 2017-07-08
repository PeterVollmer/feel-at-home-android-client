package club.frickel.feelathome

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class PowerIntent : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("BroadcastReceiver", "triggered")
        SendActiveHandler(intent.getBooleanExtra(Constants.EXTRA_SET_ACTIVE, false), intent.dataString, context).execute()

    }
}
