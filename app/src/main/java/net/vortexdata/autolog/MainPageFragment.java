package net.vortexdata.autolog;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import net.vortexdata.autolog.configs.Cfg;
import net.vortexdata.autolog.configs.Msg;

public class MainPageFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    EditText inUsername;
    EditText inPassword;
    Button saveButton;
    ImageView settings;
    ImageView news;
    TextView header;
    ConstraintLayout bg;
    View v;

    Thread ok;

    public MainPageFragment() {

    }


    public static MainPageFragment newInstance() {
        MainPageFragment fragment = new MainPageFragment();
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
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.fragment_main_page, container, false);


        inUsername = (EditText) v.findViewById(R.id.inUsername);
        inPassword = v.findViewById(R.id.inPassword);
        saveButton = v.findViewById(R.id.savebutton);
        settings = v.findViewById(R.id.config);
        news = v.findViewById(R.id.news);
        header = v.findViewById(R.id.header);
        bg = v.findViewById(R.id.bg_home);

        loadData();

        if (Cfg.fancyBackground) {
            Settings.setFancyBackground(bg, getContext());
        }

        settings.setOnClickListener(view -> {
            FragmentTransaction tr = getFragmentManager().beginTransaction();
            tr.replace(R.id.bg_home, new SettingsPage());
            tr.commit();
        });

        saveButton.setOnClickListener(view -> {

            if (inUsername.getText().length() < 4 || inPassword.getText().length() < 4) {

                Snackbars.Snackbar(view, Msg.noUsername, Msg.err_color);
                return;
            }

            SharedPreferences.Editor editor = getContext().getSharedPreferences("userData", getContext().MODE_PRIVATE).edit();
            editor.putString("user", inUsername.getText().toString());
            editor.putString("pw", inPassword.getText().toString());
            editor.apply();

            Snackbars.Snackbar(view, Msg.loginData, Msg.successColor);

            Snackbars.Snackbar(view, Msg.processing, Msg.GreyColor);
            LoginPost l = new LoginPost();
            l.send(inUsername.getText().toString(), inPassword.getText().toString(), this);
        });


        return v;
    }

    private void loadData() {
        SharedPreferences prefs = getContext().getSharedPreferences("userData", getContext().MODE_PRIVATE);
        inUsername.setText(prefs.getString("user", ""));
        inPassword.setText(prefs.getString("pw", ""));

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    public void error(final String error) {
        Snackbars.Snackbar(v, Msg.err, Msg.err_color);
    }

    public void ok(final String message) {
        Snackbars.Snackbar(v, message, Msg.successColor);

        ok = new Thread(() -> {
            try {
                ok.sleep(1000);
                BasicMethods.openTab(getContext());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        ok.start();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
