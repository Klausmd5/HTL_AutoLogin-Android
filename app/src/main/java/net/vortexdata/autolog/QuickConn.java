package net.vortexdata.autolog;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.util.List;

public class QuickConn extends AppCompatActivity {

    private ImageView quickConn;
    private Thread quickconnThread;
    private String inUsername;
    private String inPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_conn);

        quickConn = findViewById(R.id.quickConn);
        loadData();

        String networkSSID = "HTBLA";
        String networkPass = "htlgrieskirchen";

        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"" + networkSSID + "\"";
        conf.preSharedKey = "\""+ networkPass +"\"";
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
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
                Snackbars.SnackbarLong(getWindow().getDecorView().getRootView(), "Processing... Please wait.", "#7b7b7b");
                quickconnThread.sleep(6000);
                LoginPost.quickSend(inUsername, inPassword, this);
                quickconnThread.sleep(200);
                finish();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        quickconnThread.start();


    }

    private void loadData() {
        SharedPreferences prefs = getSharedPreferences("userData", MODE_PRIVATE);
        inUsername = prefs.getString("user", "");
        inPassword = prefs.getString("pw", "");

    }


    public void error(final String error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Snackbars.Snackbar(getWindow().getDecorView().getRootView(), "Oops! Some connection error occured. Wrong Wifi?", "#eb3b5a");

            }
        });
    }

    public void ok(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if(message.equals("Successfully logged in!")) {
                    Snackbars.Snackbar(getWindow().getDecorView().getRootView(), message, "#00d873");
                } else {
                    Snackbars.Snackbar(getWindow().getDecorView().getRootView(), message, "#eb3b5a");
                }

            }
        });

    }
}
