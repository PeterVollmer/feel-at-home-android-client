package club.frickel.feelathome

import android.app.AlertDialog
import android.app.Fragment

object ErrorDialogs {


    fun showReceiveErrorDialog(possibleNextFragment: Fragment, main: Main) {
        val mPossibleNextFragment = possibleNextFragment
        val mMain = main
        val builder = AlertDialog.Builder(mMain)
        builder.setTitle(R.string.receive_error_dialog_title)
        builder.setCancelable(false)
        builder.setMessage(R.string.receive_error_dialog_message)
                .setPositiveButton(R.string.receive_error_dialog_retry) { dialog, id ->
                    val fm = mMain.fragmentManager

                    if (fm.backStackEntryCount == 0) {

                        mMain.replaceFragment(mPossibleNextFragment)
                    } else {
                        fm.popBackStack()
                        mMain.replaceFragmentAndAddToBackstack(mPossibleNextFragment)
                    }
                }
                .setNegativeButton(R.string.receive_error_dialog_settings) { dialog, id ->
                    mMain.fragmentManager.popBackStack()
                    mMain.replaceFragmentAndAddToBackstack(Settings())
                }

        // Create the AlertDialog object and return it

        val alertDialog = builder.create()
        alertDialog.show()
    }

    fun showSendErrorDialog(mActive: Boolean, mDeviceID: String, mMain: Main, errorMessage: String) {
        val active = mActive
        val deviceID = mDeviceID
        val main = mMain
        val builder = AlertDialog.Builder(mMain)
        builder.setTitle(R.string.send_error_dialog_title)
        builder.setCancelable(false)
        builder.setMessage(errorMessage + R.string.send_error_dialog_message)
                .setPositiveButton(R.string.receive_error_dialog_retry) { dialog, id -> SendActiveHandler(active, deviceID, main).execute() }
                .setNegativeButton(R.string.send_error_dialog_settings) { dialog, id ->
                    main.fragmentManager.popBackStack()
                    main.replaceFragment(Settings())
                }

        // Create the AlertDialog object and return it

        val alertDialog = builder.create()
        alertDialog.show()
    }

    fun showSendErrorDialog(mEffect: Effect, mDeviceID: String, mMain: Main, errorMessage: String) {
        val effect = mEffect
        val deviceID = mDeviceID
        val main = mMain
        val builder = AlertDialog.Builder(mMain)
        builder.setTitle(R.string.send_error_dialog_title)
        builder.setCancelable(false)
        builder.setMessage(errorMessage + R.string.send_error_dialog_message)
                .setPositiveButton(R.string.receive_error_dialog_retry) { dialog, id -> SendStateHandler(effect, deviceID, main).execute() }
                .setNegativeButton(R.string.send_error_dialog_settings) { dialog, id ->
                    main.fragmentManager.popBackStack()
                    main.replaceFragment(Settings())
                }

        // Create the AlertDialog object and return it

        val alertDialog = builder.create()
        alertDialog.show()
    }

    fun showErrorDialog(mMain: Main, errorMessage: String) {
        val builder = AlertDialog.Builder(mMain)
        builder.setTitle(R.string.error_dialog_title)
        builder.setCancelable(false)
        builder.setMessage(errorMessage)
                .setNegativeButton(R.string.ok) { dialog, id -> }


        val alertDialog = builder.create()
        alertDialog.show()
    }
}
