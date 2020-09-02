package net.vortexdata.autolog.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import net.vortexdata.autolog.MainPageFragment;
import net.vortexdata.autolog.SettingsPage;

public class SliderAdapter extends FragmentStatePagerAdapter {

    int tabs = 2;
    public static SettingsPage settings;
    public static MainPageFragment mainPage;

    public SliderAdapter(FragmentManager fm) {
        super(fm);
        settings = new SettingsPage();
        mainPage = new MainPageFragment();

    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 1: return settings;
            case 0: return mainPage;
            default: return mainPage;
        }
    }

    @Override
    public int getCount() {
        return tabs;
    }

}
