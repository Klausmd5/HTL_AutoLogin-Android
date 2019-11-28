package net.vortexdata.autolog;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

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

        vp.setCurrentItem(1);
        main = this;
    }

}
