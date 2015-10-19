package club.frickel.feelathome;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.app.Fragment;
import android.app.FragmentManager;


/**
 * User: Peter Vollmer
 * Date: 3/14/13
 * Time: 1:31 PM
 */
public class ErrorDialogs {


    public static void showReceiveErrorDialog(Fragment possibleNextFragment, Main main) {
        final Fragment mPossibleNextFragment = possibleNextFragment;
        final Main mMain = main;
        AlertDialog.Builder builder = new AlertDialog.Builder(mMain);
        builder.setTitle(R.string.receive_error_dialog_title);
        builder.setCancelable(false);
        builder.setMessage(R.string.receive_error_dialog_message)
                .setPositiveButton(R.string.receive_error_dialog_retry, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FragmentManager fm = mMain.getFragmentManager();

                        if (fm.getBackStackEntryCount() == 0) {

                            mMain.replaceFragment(mPossibleNextFragment);
                        } else {
                            fm.popBackStack();
                            mMain.replaceFragmentAndAddToBackstack(mPossibleNextFragment);
                        }

                    }
                })
                .setNegativeButton(R.string.receive_error_dialog_settings, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mMain.getFragmentManager().popBackStack();
                        mMain.replaceFragmentAndAddToBackstack(new Settings());
                    }
                });

        // Create the AlertDialog object and return it

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static void showSendErrorDialog(boolean mActive, String mDeviceID, Main mMain, String errorMessage) {
        final boolean active = mActive;
        final String deviceID = mDeviceID;
        final Main main = mMain;
        AlertDialog.Builder builder = new AlertDialog.Builder(mMain);
        builder.setTitle(R.string.send_error_dialog_title);
        builder.setCancelable(false);
        builder.setMessage(errorMessage + R.string.send_error_dialog_message)
                .setPositiveButton(R.string.receive_error_dialog_retry, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new SendActiveHandler(active, deviceID, main).execute();
                    }
                })
                .setNegativeButton(R.string.send_error_dialog_settings, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        main.getFragmentManager().popBackStack();
                        main.replaceFragment(new Settings());
                    }
                });

        // Create the AlertDialog object and return it

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public static void showSendErrorDialog(Effect mEffect, String mDeviceID, Main mMain, String errorMessage) {
        final Effect effect = mEffect;
        final String deviceID = mDeviceID;
        final Main main = mMain;
        AlertDialog.Builder builder = new AlertDialog.Builder(mMain);
        builder.setTitle(R.string.send_error_dialog_title);
        builder.setCancelable(false);
        builder.setMessage(errorMessage + R.string.send_error_dialog_message)
                .setPositiveButton(R.string.receive_error_dialog_retry, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new SendStateHandler(effect, deviceID, main).execute();
                    }
                })
                .setNegativeButton(R.string.send_error_dialog_settings, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        main.getFragmentManager().popBackStack();
                        main.replaceFragment(new Settings());
                    }
                });

        // Create the AlertDialog object and return it

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static void showErrorDialog(Main mMain, String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mMain);
        builder.setTitle(R.string.error_dialog_title);
        builder.setCancelable(false);
        builder.setMessage(errorMessage)
                .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });


        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
