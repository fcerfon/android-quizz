package om.superquizz.diginamic.superquizz.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkChangeReceiver extends BroadcastReceiver {

    public static final String NETWORK_CHANGE_ACTION = "om.superquizz.diginamic.superquizz.NetworkChangeReceiver";
    private String connectionStatus = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (isOnline(context)) {
            sendInternalBroadcast(context, true);
        }
        else {
            sendInternalBroadcast(context, false);
        }
    }

    private void sendInternalBroadcast(Context context, Boolean status) {
        try {
            Intent intent = new Intent();
            intent.putExtra("status", status);
            intent.setAction(NETWORK_CHANGE_ACTION);
            context.sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static public boolean isOnline(Context context) {
        boolean isOnline = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            isOnline = (netInfo != null && netInfo.isConnected());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isOnline;
    }
}
