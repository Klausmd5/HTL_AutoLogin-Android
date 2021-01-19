package net.vortexdata.autolog;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import net.vortexdata.autolog.configs.Cfg;
import net.vortexdata.autolog.configs.Msg;

public class MainPageFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public static EditText inUsername;
    public static EditText inPassword;
    public static Button saveButton;
    ImageView settings;
    ImageView news;
    TextView header;
    public ConstraintLayout bg;
    View v;

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
        v = inflater.inflate(R.layout.fragment_main_page, container, false);

        inUsername = v.findViewById(R.id.inUsername);
        inPassword = v.findViewById(R.id.inPassword);
        saveButton = v.findViewById(R.id.savebutton);
        settings = v.findViewById(R.id.config);
        news = v.findViewById(R.id.news);
        header = v.findViewById(R.id.header);
        bg = v.findViewById(R.id.bg_home);

        loadData();

        BasicMethods.updateInput();

        if (Cfg.fancyBackground) {
            BasicMethods.setFancyBackground(bg, getContext());
        }

        settings.setOnClickListener(view -> {
            home.main.vp.setCurrentItem(2, true);
        });

        if(Cfg.lockCredentials) {
            saveButton.setText("Log in");
        }

        saveButton.setOnClickListener(view -> {

            if (inUsername.getText().length() < 4 || inPassword.getText().length() < 4) {
                Snackbars.Snackbar(view, Msg.noUsername, Msg.err_color);
                return;
            }

            if(inUsername.getText().toString().equals("VortexDev")) {
                Cfg.dev = true;
                loadData();
                Snackbars.Snackbar(view, Msg.debug, Msg.successColor);
                BasicMethods.saveApkData(getContext());
                return;
            }

            if(inUsername.getText().toString().equals("NoDev")) {
                Cfg.dev = false;
                loadData();
                Snackbars.Snackbar(view, Msg.leftDebug, Msg.successColor);
                BasicMethods.saveApkData(getContext());
                return;
            }


            SharedPreferences.Editor editor = getContext().getSharedPreferences("userData", getContext().MODE_PRIVATE).edit();
            editor.putString("user", inUsername.getText().toString());
            editor.putString("pw", inPassword.getText().toString());
            editor.apply();

            //Snackbars.Snackbar(view, Msg.loginData, Msg.successColor);

            Snackbars.Snackbar(view, Msg.processing, Msg.GreyColor);
            LoginPost l = new LoginPost();
            l.send(inUsername.getText().toString(), inPassword.getText().toString(), this);
        });

        news.setOnClickListener(view -> {
            home.main.vp.setCurrentItem(0, true);
        });


        return v;
    }

    private void loadData() {
        SharedPreferences prefs = getContext().getSharedPreferences("userData", getContext().MODE_PRIVATE);
        inUsername.setText(prefs.getString("user", "Gastschueler"));
        inPassword.setText(prefs.getString("pw", "htlgkr"));

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
        if(message.equals("Wrong")) {
            Snackbars.Snackbar(v, message, Msg.err_color);
        } else {
            Snackbars.Snackbar(v, message, Msg.successColor);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
