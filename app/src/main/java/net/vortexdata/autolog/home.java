package net.vortexdata.autolog;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import net.vortexdata.autolog.adapter.SliderAdapter;
import net.vortexdata.autolog.configs.Cfg;

public class home extends AppCompatActivity {

    public static home main;

    public ViewPager vp;

    public SliderAdapter sl;
    public int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        vp = findViewById(R.id.slideViewPager);
        sl = new SliderAdapter(getSupportFragmentManager());
        vp.setAdapter(sl);
        loadApkData();

        vp.setCurrentItem(1);
        main = this;
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
        if(Qconn.done) {
            finishAndRemoveTask();
        }

        if(currentPage == 1) {
            super.onBackPressed();
        } else {
            vp.setCurrentItem(1, true);
        }
    }
}
