package net.vortexdata.autolog;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import net.vortexdata.autolog.configs.Cfg;

public class SettingsPage extends Fragment {

    TextView settingsHeader;
    TextView back;
    TextView copyright;
    TextView vtext;
    Switch rgb;
    Switch rgb2;
    Switch autoConn;
    TextView version;
    CardView hr1;
    CardView hr2;
    ConstraintLayout bg;

    private int clicked = 0;

    private OnFragmentInteractionListener mListener;

    public SettingsPage() {

    }


    public static SettingsPage newInstance(String param1, String param2) {
        SettingsPage fragment = new SettingsPage();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings_page, container, false);

        settingsHeader = v.findViewById(R.id.settingsHeader);
        back = v.findViewById(R.id.back);
        copyright = v.findViewById(R.id.copyright);
        rgb = v.findViewById(R.id.rgbMode);
        rgb2 = v.findViewById(R.id.rgbMode2);
        version = v.findViewById(R.id.version);
        vtext = v.findViewById(R.id.vtext);
        autoConn = v.findViewById(R.id.connectToWifi);
        hr1 = v.findViewById(R.id.hr1);
        hr2 = v.findViewById(R.id.hr2);
        bg = v.findViewById(R.id.bg_settings);

        if(Cfg.fancyBackground) {
            Settings.setFancyBackground(bg, getContext());
            rgb.setChecked(true);
            rgb2.setVisibility(View.VISIBLE);
        }

        version.setText(Cfg.version);

        back.setOnClickListener(view -> {

        });

        if (Cfg.fancyBackground) {
            Settings.setFancyBackground(bg, getContext());
            rgb.setChecked(true);
        }

        rgb2.setChecked(Cfg.fancyBGinQConn);

        rgb.setOnClickListener(view -> {
            if (rgb.isChecked()) {
                Cfg.fancyBackground = true;
                Settings.setFancyBackground(bg, getContext());
                rgb2.setVisibility(View.VISIBLE);
                saveApkData();
            }

            if (!rgb.isChecked()) {
                Cfg.fancyBackground = false;
                Settings.removeFancyBackground(bg, getContext());
                rgb2.setVisibility(View.GONE);
                Cfg.fancyBGinQConn = false;
                saveApkData();
            }
        });

        rgb2.setOnClickListener(view -> {
            if (rgb2.isChecked()) {
                Cfg.fancyBGinQConn = true;
                saveApkData();
            } else {
                Cfg.fancyBGinQConn = false;
                saveApkData();
            }
        });

        autoConn.setChecked(Cfg.autoConnect);

        autoConn.setOnClickListener(view -> {
            if (autoConn.isChecked()) {
                Cfg.autoConnect = true;
                saveApkData();
            }
            if (!autoConn.isChecked()) {
                Cfg.autoConnect = false;
                saveApkData();
            }
        });

        copyright.setOnClickListener(view -> {
            clicked++;
            if (clicked > 4) {
                Cfg.easteregg = true;
                saveApkData();
                clicked = 0;
                if (Cfg.easteregg) {
                    // place for easteregg
                }
            }


        });

        return v;
    }

    private void saveApkData() {
        SharedPreferences.Editor editor = getContext().getSharedPreferences("apkData", 0).edit();
        editor.putBoolean("easteregg", Cfg.easteregg);
        editor.putBoolean("fancyBackground", Cfg.fancyBackground);
        editor.putBoolean("Expired", Cfg.expired);
        editor.putBoolean("sentUsage", Cfg.sentUsage);
        editor.putBoolean("QConnBg", Cfg.fancyBGinQConn);
        editor.putBoolean("connectToWifi", Cfg.autoConnect);
        editor.apply();
    }

    private void loadApkData() {
        SharedPreferences prefs = getContext().getSharedPreferences("apkData", 0);
        Cfg.easteregg = prefs.getBoolean("easteregg", false);
        Cfg.fancyBackground = prefs.getBoolean("fancyBackground", false);
        Cfg.expired = prefs.getBoolean("Expired", false);
        Cfg.sentUsage = prefs.getBoolean("sentUsage", false);
        Cfg.fancyBGinQConn = prefs.getBoolean("QConnBg", false);
        Cfg.autoConnect = prefs.getBoolean("connectToWifi", true);
    }

    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
