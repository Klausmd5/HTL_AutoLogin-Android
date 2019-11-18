package net.vortexdata.autolog;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;

import net.vortexdata.autolog.configs.Cfg;

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

    public static void setFancyBackgrounds(ConstraintLayout[] c, Context context) {
        for(ConstraintLayout bg : c) {
            setFancyBackground(bg, context);
        }
    }

    public static void setFancyBackground(ConstraintLayout bg, Context c) {
        bg.setBackground(ContextCompat.getDrawable(c, R.drawable.anim_background));
        AnimationDrawable an =(AnimationDrawable) bg.getBackground();
        an.setEnterFadeDuration(5000);
        an.setExitFadeDuration(2000);
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

}
