package net.vortexdata.autolog;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import net.vortexdata.autolog.configs.Cfg;

public class Settings extends AppCompatActivity {

    private TextView back;
    private TextView copyright;
    private Switch rgb;
    private Switch rgb2;
    private Switch autoConn;
    private ConstraintLayout background;
    TextView version;

    private int clicked = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        back = findViewById(R.id.back);
        copyright = findViewById(R.id.copyright);
        rgb = findViewById(R.id.rgbMode);
        rgb2 = findViewById(R.id.rgbMode2);
        background = findViewById(R.id.background);
        version = findViewById(R.id.version);
        autoConn = findViewById(R.id.connectToWifi);

        version.setText(Cfg.version);

        if(Cfg.fancyBackground) {
            setFancyBackground(background, this);
            rgb.setChecked(true);
            rgb2.setVisibility(View.VISIBLE);
        }

        rgb2.setChecked(Cfg.fancyBGinQConn);

        rgb.setOnClickListener(view -> {
            if(rgb.isChecked()) {
                Cfg.fancyBackground = true;
                setFancyBackground(background, this);
                rgb2.setVisibility(View.VISIBLE);
                saveApkData();
            }
            if(!rgb.isChecked()) {
                Cfg.fancyBackground = false;
                //Cfg.easteregg = false;
                //rgb.setVisibility(View.GONE);
                removeFancyBackground(background, this);
                rgb2.setVisibility(View.GONE);
                Cfg.fancyBGinQConn = false;
                saveApkData();
            }
        });


        rgb2.setOnClickListener(v -> {
            if(rgb2.isChecked()) {
                Cfg.fancyBGinQConn = true;
                saveApkData();
            } else {
                Cfg.fancyBGinQConn = false;
                saveApkData();
            }
        });

        autoConn.setChecked(Cfg.autoConnect);

        autoConn.setOnClickListener(view -> {
            if(autoConn.isChecked()) {
                Cfg.autoConnect = true;
                saveApkData();
            }
            if(!autoConn.isChecked()) {
                Cfg.autoConnect = false;
                saveApkData();
            }
        });

        back.setOnClickListener(view -> {
            //Intent i = new Intent(getApplicationContext(), MainActivity.class); // makes some errors
            //startActivity(i);
            finish();

        });

        copyright.setOnClickListener(view -> {
            clicked++;
            if(clicked > 4) {
                Cfg.easteregg = true;
                saveApkData();
                clicked = 0;
                if(Cfg.easteregg) {
                    //rgb.setVisibility(View.VISIBLE);
                }
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

    private void saveApkData() {
        SharedPreferences.Editor editor = getSharedPreferences("apkData", 0).edit();
        editor.putBoolean("easteregg", Cfg.easteregg);
        editor.putBoolean("fancyBackground", Cfg.fancyBackground);
        editor.putBoolean("QConnBg", Cfg.fancyBGinQConn);
        editor.putBoolean("connectToWifi", Cfg.autoConnect);
        editor.apply();
    }
}
