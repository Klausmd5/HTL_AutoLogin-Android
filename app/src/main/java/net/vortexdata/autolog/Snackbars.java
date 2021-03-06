package net.vortexdata.autolog;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class Snackbars {

    public static void Snackbar(View view, String txt, String color) {

        if(view == null) return;

        Snackbar snackbar;
        snackbar = Snackbar.make(view, txt, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(Color.parseColor(color));
        snackbar.show();

        return;
    }

    public static void SnackbarLong(View view, String txt, String color) {

        Snackbar snackbar;
        snackbar = Snackbar.make(view, txt, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(Color.parseColor(color));
        snackbar.show();

        return;
    }

    public static void LongTextSnackbar(View view, String txt, String BackgroundColor, int lines) {

        Snackbar snackbar;
        snackbar = Snackbar.make(view, txt, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        TextView t = (TextView) snackBarView.findViewById(R.id.snackbar_text) ;
        t.setMaxLines(lines);
        snackBarView.setBackgroundColor(Color.parseColor(BackgroundColor));
        snackbar.show();

        return;
    }

}
