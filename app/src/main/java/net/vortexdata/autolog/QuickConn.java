package net.vortexdata.autolog;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.util.List;

public class QuickConn {

    private ImageView quickConn;
    private Thread quickconnThread;
    private String inUsername;
    private String inPassword;

   public QuickConn(Context c) {

        loadData(c);

        String networkSSID = "HTBLA";
        String networkPass = "htlgrieskirchen";

        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"" + networkSSID + "\"";
        conf.preSharedKey = "\""+ networkPass +"\"";
        WifiManager wifiManager = (WifiManager) c.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.addNetwork(conf);


        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for( WifiConfiguration i : list ) {
            if(i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                wifiManager.disconnect();
                wifiManager.enableNetwork(i.networkId, true);

                wifiManager.reconnect();


                break;
            }
        }
        quickconnThread = new Thread(() -> {
            try {
                //Snackbars.SnackbarLong(getWindow().getDecorView().getRootView(), "Processing... Please wait.", "#7b7b7b");
                quickconnThread.sleep(6000);
                LoginPost.quickSend(inUsername, inPassword);
                //quickconnThread.sleep(200);
                //finish();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        quickconnThread.start();


    }

    private void loadData(Context context) {

        SharedPreferences prefs = context.getSharedPreferences("userData", 0);
        inUsername = prefs.getString("user", "");
        inPassword = prefs.getString("pw", "");

    }

}
