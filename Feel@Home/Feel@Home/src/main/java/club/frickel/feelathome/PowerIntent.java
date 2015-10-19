package club.frickel.feelathome;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * User: Peter Vollmer
 * Date: 28.07.14
 * Time: 13:57
 */
public class PowerIntent extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("BroadcastReceiver","triggered");
        new SendActiveHandler(intent.getBooleanExtra(Constants.EXTRA_SET_ACTIVE,false), intent.getDataString(), context).execute();

    }
}
