package net.vortexdata.autolog;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
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

        main = this;

    }


    private void loadApkData() {
        SharedPreferences prefs = getSharedPreferences("apkData", 0);
        Cfg.easteregg = prefs.getBoolean("easteregg", false);
        Cfg.fancyBackground = prefs.getBoolean("fancyBackground", false);
        Cfg.fancyBGinQConn = prefs.getBoolean("QConnBg", false);
        Cfg.autoConnect = prefs.getBoolean("connectToWifi", true);
        Cfg.openTab = prefs.getBoolean("openTab", false);
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
