package net.vortexdata.autolog;

import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.vortexdata.autolog.configs.Msg;

public class Qconn extends AppCompatActivity {

    private ImageView state;
    private TextView stateTxt;
    private TextView underTxt;
    private ProgressBar pb;
    private TextView quitTxt;

    private Thread closeThread;
    private Thread timer;
    private int closeCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qconn);

        state = findViewById(R.id.stateImg);
        stateTxt = findViewById(R.id.stateTxt);
        underTxt = findViewById(R.id.underTxt);
        pb = findViewById(R.id.pbar);
        quitTxt = findViewById(R.id.quitMsg);

        QuickConn q = new QuickConn(getApplicationContext(), this);

         timer = new Thread(() -> {
             try {
                 timer.sleep(4000);
                 runOnUiThread(() -> {
                     underTxt.setVisibility(View.VISIBLE);
                 });
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }

         });
         timer.start();

        Thread t = new Thread(() -> {
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
        t.start();

    }

    public void closeWindow() {
        runOnUiThread(() -> {
            quitTxt.setVisibility(View.VISIBLE);
        });

        closeCounter = 3;
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
            ConstraintLayout bg = findViewById(R.id.cbackground);
            bg.setBackgroundColor(Color.parseColor(color));
        });
    }
}
