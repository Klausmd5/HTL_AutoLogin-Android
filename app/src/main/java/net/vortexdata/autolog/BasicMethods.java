package net.vortexdata.autolog;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.vortexdata.autolog.configs.Cfg;
import net.vortexdata.autolog.objects.News;

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
    }


    public static void readNews(Fragment f, int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(f.getContext());
        View view = f.getLayoutInflater().inflate(R.layout.read_news_layout, null);

        builder.setView(view);

        ImageView icon = view.findViewById(R.id.icon);
        TextView head = view.findViewById(R.id.headline);
        TextView text = view.findViewById(R.id.text);
        TextView date = view.findViewById(R.id.date);
        TextView done = view.findViewById(R.id.done);
        TextView by = view.findViewById(R.id.by);

        News n = NewsFragment.NewsFeed.get(i);

        head.setText(n.getHeadline());
        text.setText(n.getText());
        date.setText(n.getDate());
        by.setText(n.getCreator());
        setNewsIcons(n, icon, view);
        n.setRead(true);
        NewsFragment.na.notifyDataSetChanged();

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        done.setOnClickListener(view1 -> dialog.dismiss());

        dialog.show();
        if(Cfg.dev) {
            NewsFragment.saveNews(f.getContext());
        }
    }

    public static void setNewsIcons(News news, ImageView icon, View view) {
        switch (news.getCategory()) {
            case "app": icon.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_comment_black_24dp));
                break;
            case "sv": icon.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_public_black_24dp));
                break;
            case "info": icon.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_info_outline_black_24dp));
                break;
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
    }

}
