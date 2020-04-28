package cz.zcu.kiv.chatbot.connection;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;

/**
 * Utils for internet connection management.
 *
 * @author Martin Matas
 * @version 1.0
 * created on 2020-28-04
 */
public class InternetConnection {

    /**
     * Method retrieves info about WiFI and mobile data connection and returns True if at least one
     * of them is connected to the internet.
     *
     * @param context - application context
     * @return - connection status (True - connected, False - disconnected)
     */
    public static boolean check(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            return (mobile != null && mobile.isConnectedOrConnecting())
                    || (wifi != null && wifi.isConnectedOrConnecting());
        }

        return false;
    }

    /**
     * Displays alert dialog that internet connection is not available.
     *
     * @param activityContext - context of activity where the dialog shows
     */
    public static void noConnectionDialog(final AppCompatActivity activityContext) {
        AlertDialog alertDialog = new AlertDialog.Builder(activityContext).create();
        alertDialog.setTitle("No connection");
        alertDialog.setMessage("Internet not available, check your internet connectivity and try again.");
        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                activityContext.finish();
            }
        });

        alertDialog.show();
    }

}
