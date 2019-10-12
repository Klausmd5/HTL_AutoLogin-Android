package net.vortexdata.autolog;

import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.vortexdata.autolog.configs.Cfg;
import net.vortexdata.autolog.configs.Msg;

public class Qconn extends AppCompatActivity {

    private ImageView state;
    private TextView stateTxt;
    private TextView underTxt;
    private ProgressBar pb;
    private TextView quitTxt;
    private ConstraintLayout bg;

    private Thread closeThread;
    private Thread timer;
    private Thread timeout;
    private Thread connect;
    private Thread easteregg;
    private int closeCounter = 3;

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

        QuickConn q = new QuickConn(getApplicationContext(), this);

        if(Cfg.fancyBGinQConn) {
            Settings.setFancyBackground(bg, this);
            pb.setIndeterminate(true);
            pb.getIndeterminateDrawable().setColorFilter(0xFFFFFFFF,
                    android.graphics.PorterDuff.Mode.MULTIPLY);
        }

        if(Cfg.easteregg) {
            easteregg = new Thread(() -> {
                while (true) {
                    runOnUiThread(() -> {

                            stateTxt.setRotation(stateTxt.getRotation()+5);
                    });
                }
            });
        }

         timer = new Thread(() -> {
             try {
                 timer.sleep(6000);
                 runOnUiThread(() -> {
                     underTxt.setVisibility(View.VISIBLE);
                 });
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }

         });
         timer.start();

         timeout = new Thread(() -> {
            try {
                timer.sleep(40000);
                connect.interrupt();
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
                e.printStackTrace();
            }

        });
        timeout.start();

        connect = new Thread(() -> {
            while (true) {
                if(q.done) {
                    if (q.statePositive) {
                        setBgColor("#27AE60");
                        runOnUiThread(() -> {
                            stateTxt.setText("Success!");
                            underTxt.setText(Msg.qConnSuccessMsg);
                        });
                        setVisibility();
                        closeWindow();
                        if(q.MobileDebug) {
                            runOnUiThread(() -> {
                                underTxt.setText("Status Positive:" + q.statePositive);
                                stateTxt.setText("Resp:" + q.response);
                            });
                        }
                        return;

                    } else {
                        setBgColor("#C3073F");
                        runOnUiThread(() -> {
                            stateTxt.setText("Failed!");
                            state.setImageResource(R.drawable.ic_clear_black_24dp);
                            if(q.state.contains("Wrong")) {
                                underTxt.setText(Msg.qConnFailWrongUser);
                            } else {
                                underTxt.setText(Msg.qConnErr);
                            }
                            if(q.MobileDebug) {
                                runOnUiThread(() -> {
                                    underTxt.setText("Status Positive:" + q.statePositive);
                                    stateTxt.setText("Resp:" + q.response);
                                });
                            }
                        });
                        setVisibility();
                        closeWindow();
                        return;
                    }
                }
            }

        });
        connect.start();

        if(Cfg.easteregg) {
            easteregg.start();
            Cfg.easteregg = false;
            q.saveApkData(this);
        }

    }

    public void closeWindow() {
        runOnUiThread(() -> {
            quitTxt.setVisibility(View.VISIBLE);
        });

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

            //finish();
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
}
