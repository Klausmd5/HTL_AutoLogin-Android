package net.vortexdata.autolog;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

public class Settings extends AppCompatActivity {

    private TextView back;
    private TextView copyright;
    private Switch rgb;
    private Switch wifi;
    private ConstraintLayout background;
    TextView version;

    private int clicked = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        back = findViewById(R.id.name);
        copyright = findViewById(R.id.copyright);
        rgb = findViewById(R.id.rgbMode);
        background = findViewById(R.id.background);
        wifi = findViewById(R.id.connectToHTBLA);
        version = findViewById(R.id.version);

        version.setText(Cfg.version);

        if(Cfg.fancyBackground) {
            setFancyBackground(background, this);
            rgb.setChecked(true);
        }

        if(Cfg.connectToWifi) wifi.setChecked(true);

        wifi.setOnClickListener(v -> {
            if(wifi.isChecked()) {
                Cfg.connectToWifi = true;
            }

            if(!wifi.isChecked()) {
                Cfg.connectToWifi = false;
            }
        });

        if(Cfg.easteregg) {
            rgb.setVisibility(View.VISIBLE);
            rgb.setOnClickListener(view -> {
                if(rgb.isChecked()) {
                    Cfg.fancyBackground = true;
                    setFancyBackground(background, this);
                }
                if(!rgb.isChecked()) {
                    Cfg.fancyBackground = false;
                    Cfg.easteregg = false;
                    rgb.setVisibility(View.GONE);
                    removeFancyBackground(background, this);
                }
            });
        }



        back.setOnClickListener(view -> {

            finish();

        });

        copyright.setOnClickListener(view -> {
            clicked++;
            if(clicked > 4) {
                Cfg.easteregg = true;
                clicked = 0;
            }


        });

    }

    public static void removeFancyBackground(ConstraintLayout bg, Context c) {
        bg.setBackground(ContextCompat.getDrawable(c, R.color.backgroundBlack));
    }

    public static void setFancyBackground(ConstraintLayout bg, Context c) {
        bg.setBackground(ContextCompat.getDrawable(c, R.drawable.anim_background));
        AnimationDrawable an =(AnimationDrawable) bg.getBackground();
        an.setEnterFadeDuration(5000);
        an.setExitFadeDuration(2000);
        an.start();
    }
}
