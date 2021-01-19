package net.vortexdata.autolog.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import net.vortexdata.autolog.MainPageFragment;
import net.vortexdata.autolog.NewsFragment;
import net.vortexdata.autolog.SettingsPage;
import net.vortexdata.autolog.objects.News;

public class SliderAdapter extends FragmentStatePagerAdapter {

    int tabs = 3;
    public static SettingsPage settings;
    public static MainPageFragment mainPage;
    public static NewsFragment news;

    public SliderAdapter(FragmentManager fm) {
        super(fm);
        settings = new SettingsPage();
        mainPage = new MainPageFragment();
        news = new NewsFragment();
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 2: return settings;
            case 1: return mainPage;
            case 0: return news;
            default: return mainPage;
        }
    }

    @Override
    public int getCount() {
        return tabs;
    }

}
