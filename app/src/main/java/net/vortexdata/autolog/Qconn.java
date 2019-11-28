package net.vortexdata.autolog;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.vortexdata.autolog.configs.Cfg;
import net.vortexdata.autolog.configs.Msg;

import java.util.List;

public class Qconn extends AppCompatActivity {

    private String inUsername;
    private String inPassword;

    private ImageView state;
    private TextView stateTxt;
    private TextView underTxt;
    private ProgressBar pb;
    private TextView quitTxt;
    private ConstraintLayout bg;

    private Thread closeThread;
    private Thread timer;
    private Thread timeout;
    private Thread easteregg;
    private Thread quickconnThread;
    private int closeCounter = 3;

    public String response = new String();
    public boolean statePositive = false;
    public static boolean done = false;
    public String status = new String();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qconn);

        state = findViewById(R.id.stateImg);
        stateTxt = findViewById(R.id.stateTxt);
        underTxt = findViewById(R.id.underTxt);
        pb = findViewById(R.id.pbar);
        quitTxt = findViewById(R.id.quitMsg);
        bg = findViewById(R.id.cbackground);

        if(Cfg.fancyBGinQConn) {
            BasicMethods.setFancyBackground(bg, this);
            pb.setIndeterminate(true);
            pb.getIndeterminateDrawable().setColorFilter(0xFFFFFFFF,
                    android.graphics.PorterDuff.Mode.MULTIPLY);
        }

        if(Cfg.easteregg) {
            easteregg = new Thread(() -> {
                while (true) {
                    runOnUiThread(() -> stateTxt.setRotation(stateTxt.getRotation()+5));
                }
            });
        }

        connect();

         timer = new Thread(() -> {
             try {
                 timer.sleep(6000);
                 runOnUiThread(() -> underTxt.setVisibility(View.VISIBLE));
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }

         });
         timer.start();

         timeout = new Thread(() -> {
            try {
                timeout.sleep(Cfg.autoConnect ? 4000 : 2000);
                runOnUiThread(() -> {
                    setBgColor("#C3073F");
                    runOnUiThread(() -> {
                        stateTxt.setText("Timeout!");
                        state.setImageResource(R.drawable.ic_clear_black_24dp);
                        underTxt.setText(Msg.timeout);
                    });
                });
                closeCounter = 5;
                setVisibility();
                closeWindow();
            } catch (InterruptedException e) {
                // can happen on close bevore timeout
            }

        });
        timeout.start();

        if(Cfg.easteregg) {
            easteregg.start();
            Cfg.easteregg = false;
            saveApkData();
        }

    }

    public void setText() {
        timeout.interrupt();
        if(done) {
            if (statePositive) {
                setBgColor("#27AE60");
                runOnUiThread(() -> {
                    stateTxt.setText("Success!");
                    underTxt.setText(Msg.qConnSuccessMsg);
                });
                setVisibility();
                closeWindow();
                if(Cfg.dev) {
                    runOnUiThread(() -> {
                        underTxt.setText("Status Positive:" + statePositive);
                        stateTxt.setText("Resp:" + response);
                    });
                }
                return;

            } else {
                setBgColor("#C3073F");
                runOnUiThread(() -> {
                    stateTxt.setText("Failed!");
                    state.setImageResource(R.drawable.ic_clear_black_24dp);
                    if(status.contains("Wrong")) {
                        underTxt.setText(Msg.qConnFailWrongUser);
                    } else {
                        underTxt.setText(Msg.qConnErr);
                    }
                    if(Cfg.dev) {
                        runOnUiThread(() -> {
                            underTxt.setText("Status Positive:" + statePositive);
                            stateTxt.setText("Resp:" + response);
                        });
                    }
                });
                setVisibility();
                closeWindow();
                return;
            }
        }
    }

    public void closeWindow() {
        runOnUiThread(() -> quitTxt.setVisibility(View.VISIBLE));

        closeThread = new Thread(() -> {
            while (closeCounter > 0) {
                try {
                        runOnUiThread(() -> {
                            quitTxt.setText("Closing in " + closeCounter + " ..");
                            closeCounter--;
                        });

                        closeThread.sleep(1000);
                    } catch(InterruptedException e){
                        e.printStackTrace();
                    }
            }
            finishAndRemoveTask();
        });
        closeThread.start();

    }

    public void setVisibility() {
        runOnUiThread(() -> {
            state.setVisibility(View.VISIBLE);
            pb.setVisibility(View.GONE);
        });
    }

    public void setBgColor(String color) {
        runOnUiThread(() -> {
            bg.setBackgroundColor(Color.parseColor(color));
        });
    }

    public void connect() {

        if(Cfg.autoConnect) {
            String networkSSID = "HTBLA";
            String networkPass = "htlgrieskirchen";

            WifiConfiguration conf = new WifiConfiguration();
            conf.SSID = "\"" + networkSSID + "\"";
            conf.preSharedKey = "\"" + networkPass + "\"";
            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            wifiManager.addNetwork(conf);


            List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
            for (WifiConfiguration i : list) {
                if (i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                    wifiManager.disconnect();
                    wifiManager.enableNetwork(i.networkId, true);

                    wifiManager.reconnect();
                    break;
                }
            }

        }

        quickconnThread = new Thread(() -> {
            try {
                if(Cfg.autoConnect) {
                    quickconnThread.sleep(6000);
                }
                loadData();
                LoginPost l = new LoginPost();
                l.quickSend(inUsername, inPassword, this);
                saveApkData();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        quickconnThread.start();
    }

    private void loadData() {

        SharedPreferences prefs = getSharedPreferences("userData", 0);
        inUsername = prefs.getString("user", "");
        inPassword = prefs.getString("pw", "");

        if(inUsername.equalsIgnoreCase("VortexDebug")) Cfg.dev = true;
        BasicMethods.loadApkData(getApplicationContext());
    }

    public void saveApkData() {
        SharedPreferences.Editor editor = getSharedPreferences("apkData", 0).edit();
        editor.putBoolean("easteregg", Cfg.easteregg);
        editor.apply();
    }
}
