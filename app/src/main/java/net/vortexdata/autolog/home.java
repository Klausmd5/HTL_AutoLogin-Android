package net.vortexdata.autolog;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import net.vortexdata.autolog.adapter.SliderAdapter;
import net.vortexdata.autolog.configs.Cfg;
import net.vortexdata.autolog.configs.Msg;
import net.vortexdata.autolog.objects.News;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

public class home extends AppCompatActivity {

    public static home main;
    ConstraintLayout bg;

    public ViewPager vp;

    public SliderAdapter sl;
    public int currentPage;

    View v;


    public static ArrayList<News> NewsFeed = new ArrayList<News>() {};

    private boolean connecting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        vp = (ViewPager) findViewById(R.id.slideViewPager);
        sl = new SliderAdapter(getSupportFragmentManager());
        vp.setAdapter(sl);
        loadApkData();

        main = this;
        getNews();

    }

    private void saveApkData() {
        SharedPreferences.Editor editor = getSharedPreferences("apkData", 0).edit();
        editor.putBoolean("easteregg", Cfg.easteregg);
        editor.putBoolean("fancyBackground", Cfg.fancyBackground);
        editor.putBoolean("Expired", Cfg.expired);
        editor.putBoolean("sentUsage", Cfg.sentUsage);
        editor.putBoolean("QConnBg", Cfg.fancyBGinQConn);
        editor.putBoolean("connectToWifi", Cfg.autoConnect);
        editor.apply();
    }

    private void loadApkData() {
        SharedPreferences prefs = getSharedPreferences("apkData", 0);
        Cfg.easteregg = prefs.getBoolean("easteregg", false);
        Cfg.fancyBackground = prefs.getBoolean("fancyBackground", false);
        Cfg.expired = prefs.getBoolean("Expired", false);
        Cfg.sentUsage = prefs.getBoolean("sentUsage", false);
        Cfg.fancyBGinQConn = prefs.getBoolean("QConnBg", false);
        Cfg.autoConnect = prefs.getBoolean("connectToWifi", true);
    }


    public void error(final String error) {
        connecting = false;

        Snackbars.Snackbar(v, Msg.err, Msg.err_color);
    }

    public void ok(final String message) {
        connecting = false;

        Snackbars.Snackbar(v, message, Msg.successColor);
    }

    public void getNews() {

        if(NewsFeed.size() > 1) return;

        Thread getNews = new Thread(() -> {
            try {
                URL urlLoc = new URL(Cfg.newsFeed);
                trustEveryone();
                HttpsURLConnection connection = (HttpsURLConnection) urlLoc.openConnection();
                connection.setConnectTimeout(4000);
                connection.setReadTimeout(1000);
                connection.connect();


                InputStream input = new BufferedInputStream(urlLoc.openStream());

                StringBuffer responseBuffer = new StringBuffer();
                byte[] byteArray = new byte[1024];
                while (input.read(byteArray) != -1) {
                    String res = new String(byteArray, "UTF-8");
                    responseBuffer.append(res);
                    byteArray = null;
                    byteArray = new byte[1024];
                }

                String response = responseBuffer.toString();
                try {
                    JSONArray arr = new JSONArray(response);
                    for(int i = 0; i < arr.length(); i++) {
                        NewsFeed.add(new News(arr.getJSONObject(i).getString("headline"), arr.getJSONObject(i).getString("text")));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }




            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        getNews.start();

    }

    private static void trustEveryone() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier(){
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }});
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{new X509TrustManager(){
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {}
                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {}
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }}}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(
                    context.getSocketFactory());
        } catch (Exception e) { // should never happen
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if(currentPage == 1) {
            super.onBackPressed();
        } else {
            vp.setCurrentItem(1, true);
        }
    }
}
