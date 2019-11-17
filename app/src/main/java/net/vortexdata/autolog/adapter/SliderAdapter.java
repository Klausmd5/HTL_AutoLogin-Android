package net.vortexdata.autolog.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import net.vortexdata.autolog.MainPageFragment;
import net.vortexdata.autolog.NewsFragment;
import net.vortexdata.autolog.SettingsPage;

public class SliderAdapter extends FragmentStatePagerAdapter {

    int tabs = 3;
    public static SettingsPage settings;
    public static NewsFragment news;
    public static MainPageFragment mainPage;

    public SliderAdapter(FragmentManager fm) {
        super(fm);
        settings = new SettingsPage();
        news = new NewsFragment();
        mainPage = new MainPageFragment();

    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 2: return settings;
            case 0: return news;
            case 1: return mainPage;
            default: return mainPage;
        }
    }

    @Override
    public int getCount() {
        return tabs;
    }

}
