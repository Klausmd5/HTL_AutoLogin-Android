package net.vortexdata.autolog;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import net.vortexdata.autolog.configs.Cfg;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

public class BasicMethods {

    public static void openTab(Context c) {
        try {
            Uri uri = Uri.parse("googlechrome://navigate?url="+ Cfg.logURL);
            Intent i = new Intent(Intent.ACTION_VIEW, uri);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            c.startActivity(i);
        } catch (ActivityNotFoundException e) {
            // Chrome is probably not installed
        }
    }

    public static void updateInput() {
        MainPageFragment.inUsername.setEnabled(!Cfg.lockCredentials);
        MainPageFragment.inUsername.setFocusable(!Cfg.lockCredentials);
        MainPageFragment.inUsername.setFocusableInTouchMode(!Cfg.lockCredentials);

        MainPageFragment.inPassword.setEnabled(!Cfg.lockCredentials);
        MainPageFragment.inPassword.setFocusable(!Cfg.lockCredentials);
        MainPageFragment.inPassword.setFocusableInTouchMode(!Cfg.lockCredentials);

        if(Cfg.lockCredentials) {
            MainPageFragment.saveButton.setText("Login");
        } else {
            MainPageFragment.saveButton.setText("Save & Login");
        }
    }

    public static void setFancyBackgrounds(ConstraintLayout[] c, Context context) {
        AnimationDrawable an = (AnimationDrawable) context.getDrawable(R.drawable.anim_background);
        an.setEnterFadeDuration(5000);
        an.setExitFadeDuration(2000);
        for(ConstraintLayout bg : c) {
            //setFancyBackground(bg, context);
            setFancyBackground2(bg, an);
        }
    }

    public static void setFancyBackground(ConstraintLayout bg, Context c) {
        bg.setBackground(ContextCompat.getDrawable(c, R.drawable.anim_background));
        AnimationDrawable an =(AnimationDrawable) bg.getBackground();
        an.setEnterFadeDuration(5000);
        an.setExitFadeDuration(2000);
        an.start();
    }

    public static void setFancyBackground2(ConstraintLayout bg, AnimationDrawable an) {
        bg.setBackground(an);
        an.start();
    }

    public static void removeFancyBackgrounds(ConstraintLayout[] c, Context context) {
        for(ConstraintLayout bg : c) {
            removeFancyBackground(bg, context);
        }
    }

    public static void removeFancyBackground(ConstraintLayout bg, Context c) {
        bg.setBackground(ContextCompat.getDrawable(c, R.color.backgroundBlack));
    }

    public static void saveApkData(Context c) {
        SharedPreferences.Editor editor = c.getSharedPreferences("apkData", 0).edit();
        editor.putBoolean("dev", Cfg.dev);
        editor.putBoolean("easteregg", Cfg.easteregg);
        editor.putBoolean("fancyBackground", Cfg.fancyBackground);
        editor.putBoolean("QConnBg", Cfg.fancyBGinQConn);
        editor.putBoolean("connectToWifi", Cfg.autoConnect);
        editor.putBoolean("lockCredentials", Cfg.lockCredentials);
        editor.putBoolean("newDesign", Cfg.newDesign);
        editor.apply();
    }

    public static void loadApkData(Context c) {
        SharedPreferences prefs = c.getSharedPreferences("apkData", 0);
        Cfg.dev = prefs.getBoolean("dev", Cfg.dev);
        Cfg.easteregg = prefs.getBoolean("easteregg", Cfg.easteregg);
        Cfg.fancyBackground = prefs.getBoolean("fancyBackground", Cfg.fancyBackground);
        Cfg.fancyBGinQConn = prefs.getBoolean("QConnBg", Cfg.fancyBGinQConn);
        Cfg.autoConnect = prefs.getBoolean("connectToWifi", Cfg.autoConnect);
        Cfg.lockCredentials = prefs.getBoolean("lockCredentials", Cfg.lockCredentials);
        Cfg.newDesign = prefs.getBoolean("newDesign", Cfg.newDesign);
    }

}
