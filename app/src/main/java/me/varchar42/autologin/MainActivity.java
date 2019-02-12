package me.varchar42.autologin;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;



public class MainActivity extends AppCompatActivity implements ResponseReceiver {

    private MainActivity main;

    private EditText inUsername;
    private EditText inPassword;
    private Button saveButton;
    private FloatingActionButton connectButton;
    private Button connectWifi;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inUsername = (EditText) findViewById(R.id.inUsername);
        inPassword = findViewById(R.id.inPassword);
        saveButton = findViewById(R.id.savebutton);
        connectButton = findViewById(R.id.connectButton);
        connectWifi = findViewById(R.id.connectWifi);

        main = this;
        loadData();
        saveButton(saveButton);
        connectButton(connectButton);
        connectWifi(connectWifi);



    }





    private void saveButton(Button b) {
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = getSharedPreferences("userData", MODE_PRIVATE).edit();
                editor.putString("user", inUsername.getText().toString());
                editor.putString("pw", inPassword.getText().toString());
                editor.apply();
                //Toast.makeText(getBaseContext(), "Data saved!!", Toast.LENGTH_LONG).show();
                Snackbars.Snackbar(view, "Data saved!", "#00D89B");



            }
        });
    }

    private boolean connecting = false;
    private Toast toast;
    private void connectButton(FloatingActionButton b) {
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(toast != null) toast.cancel();
                if(connecting) {
                    //toast = Toast.makeText(main, "Be patient!", Toast.LENGTH_LONG);
                    //toast.show();
                    Snackbars.Snackbar(view, "Be patient!", "#fc5c65");
                    return;
                }
                //toast = Toast.makeText(main, "Sending request!", Toast.LENGTH_LONG);
                //toast.show();
                Snackbars.Snackbar(view, "Sending request..", "#7b7b7b");
                connecting = true;
                LoginPost.send(inUsername.getText().toString(), inPassword.getText().toString(), main);

            }
        });
    }

    private void connectWifi(Button b) {
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String networkSSID = "HTBLA";
                String networkPass = "htlgrieskirchen";

                WifiConfiguration conf = new WifiConfiguration();
                conf.SSID = "\"" + networkSSID + "\"";
                conf.preSharedKey = "\""+ networkPass +"\"";
                WifiManager wifiManager = (WifiManager) main.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                wifiManager.addNetwork(conf);


                List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
                for( WifiConfiguration i : list ) {
                    if(i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                        wifiManager.disconnect();
                        wifiManager.enableNetwork(i.networkId, true);

                        wifiManager.reconnect();
                        if(toast != null) toast.cancel();
                        //toast = Toast.makeText(main, "Connecting to "+i.SSID+"...", Toast.LENGTH_LONG);
                        //toast.show();
                        Snackbars.Snackbar(view, "Connecting to "+i.SSID+"...", "#7b7b7b");


                        break;
                    }
                }
            }
        });
    }


    private void loadData() {
        SharedPreferences prefs = getSharedPreferences("userData", MODE_PRIVATE);
        inUsername.setText(prefs.getString("user", ""));
        inPassword.setText(prefs.getString("pw", ""));

    }

    @Override
    public void error(final String error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                connecting = false;
                AlertDialog.Builder builder = new AlertDialog.Builder(main);
                builder.setMessage(error)
                        .setTitle("Error!!");

                builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });



                builder.setCancelable(false);


                AlertDialog dialog = builder.create();

                dialog.show();

            }
        });
    }

    @Override
    public void ok(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                connecting = false;
                Toast.makeText(main, message, Toast.LENGTH_LONG).show();
            }
        });



    }
}
