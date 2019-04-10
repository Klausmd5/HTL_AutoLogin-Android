package net.vortexdata.autolog;

import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Qconn extends AppCompatActivity {

    private ImageView state;
    private TextView stateTxt;
    private TextView underTxt;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qconn);

        state = findViewById(R.id.stateImg);
        stateTxt = findViewById(R.id.stateTxt);
        underTxt = findViewById(R.id.underTxxt);
        pb = findViewById(R.id.pbar);

        QuickConn q = new QuickConn(getApplicationContext());

        Thread t = new Thread(() -> {
            while (q.state == null) {
                if(q.done) {
                    if (q.statePositive) {
                        setBgColor("#27AE60");
                        runOnUiThread(() -> {
                            stateTxt.setText("Success!");
                            underTxt.setText("You should now have full access to the Internet.");
                        });
                        setVisibility();

                    } else {
                        setBgColor("#C3073F");
                        runOnUiThread(() -> {
                            stateTxt.setText("Failed!");
                            state.setImageResource(R.drawable.ic_clear_black_24dp);
                            underTxt.setText("No accesspoint found.");
                        });
                        setVisibility();
                    }
                }
            }
        });
        t.start();

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
