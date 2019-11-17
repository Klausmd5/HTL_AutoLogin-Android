package net.vortexdata.autolog;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import net.vortexdata.autolog.adapter.SliderAdapter;
import net.vortexdata.autolog.configs.Cfg;

public class home extends AppCompatActivity {

    public static home main;
    ConstraintLayout bg;

    public ViewPager vp;

    public SliderAdapter sl;
    public int currentPage;

    View v;

    private boolean connecting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        vp = (ViewPager) findViewById(R.id.slideViewPager);
        sl = new SliderAdapter(getSupportFragmentManager());
        vp.setAdapter(sl);
        loadApkData();

        vp.setCurrentItem(1);
        main = this;
    }

    public static void setFancyBackgrounds(ConstraintLayout[] c, Context context) {
        for(ConstraintLayout bg : c) {
            setFancyBackground(bg, context);
        }
    }

    public static void setFancyBackground(ConstraintLayout bg, Context c) {
        bg.setBackground(ContextCompat.getDrawable(c, R.drawable.anim_background));
        AnimationDrawable an =(AnimationDrawable) bg.getBackground();
        an.setEnterFadeDuration(5000);
        an.setExitFadeDuration(2000);
        an.start();
    }

    public static void removeFancyBackgrounds(ConstraintLayout[] c, Context context) {
        for(ConstraintLayout bg : c) {
            removeFancyBackground(bg, context);
        }
    }

    public static void removeFancyBackground(ConstraintLayout bg, Context c) {
        bg.setBackground(ContextCompat.getDrawable(c, R.color.backgroundBlack));
    }

    private void loadApkData() {
        SharedPreferences prefs = getSharedPreferences("apkData", 0);
        Cfg.easteregg = prefs.getBoolean("easteregg", Cfg.easteregg);
        Cfg.fancyBackground = prefs.getBoolean("fancyBackground", Cfg.fancyBackground);
        Cfg.fancyBGinQConn = prefs.getBoolean("QConnBg", Cfg.fancyBGinQConn);
        Cfg.autoConnect = prefs.getBoolean("connectToWifi", Cfg.autoConnect);
        Cfg.openTab = prefs.getBoolean("openTab", Cfg.openTab);
    }



    @Override
    public void onBackPressed() {
        if(currentPage == 1) {
            super.onBackPressed();
        } else {
            vp.setCurrentItem(1, true);
        }
    }
}
