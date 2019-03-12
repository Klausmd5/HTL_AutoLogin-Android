package net.vortexdata.autolog;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;

public class Snackbars {

    public static void Snackbar(View view, String txt, String color) {

        Snackbar snackbar;
        snackbar = Snackbar.make(view, txt, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(Color.parseColor(color));
        snackbar.show();

        return;
    }

}
