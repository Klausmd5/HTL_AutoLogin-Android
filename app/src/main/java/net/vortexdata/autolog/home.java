package net.vortexdata.autolog;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import net.vortexdata.autolog.adapter.SliderAdapter;

public class home extends AppCompatActivity {

    public static home main;
    public ViewPager vp;
    public SliderAdapter sl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        vp = findViewById(R.id.slideViewPager);
        sl = new SliderAdapter(getSupportFragmentManager());
        vp.setAdapter(sl);
        BasicMethods.loadApkData(getApplicationContext());

        vp.setCurrentItem(0);
        main = this;
    }

    public void copyURL(String url) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Upadte URL", url);
        clipboard.setPrimaryClip(clip);
    }

}
