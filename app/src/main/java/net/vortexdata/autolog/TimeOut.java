package net.vortexdata.autolog;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class TimeOut extends AppCompatActivity {

    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_out);

        b = findViewById(R.id.openBrowser);

        b.setOnClickListener(v -> {

            try {
                Uri uri = Uri.parse("googlechrome://navigate?url=" + Cfg.downloadURL);
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            } catch (ActivityNotFoundException e) {
                // Chrome is probably not installed
            }

        });
    }

    public void noBack() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.errormsg, null);

        builder.setView(view);

        TextView name = view.findViewById(R.id.name);
        Button btn = view.findViewById(R.id.accept);
        TextView t = view.findViewById(R.id.error);

        name.setText("Hey!");
        t.setText("You can't go back, because beta ended. Please follow instructions on this page.");

        AlertDialog dialog = builder.create();

        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.getWindow().setDimAmount((float) 0.9);

        btn.setOnClickListener(v ->  {
            dialog.dismiss();
        });



        dialog.show();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        noBack();
    }

}