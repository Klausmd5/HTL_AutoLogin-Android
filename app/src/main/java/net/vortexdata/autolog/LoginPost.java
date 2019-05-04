package net.vortexdata.autolog;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import net.vortexdata.autolog.configs.Cfg;
import net.vortexdata.autolog.updater.checkWeb;

import org.apache.http.client.ClientProtocolException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

/**
 * Created by mwiesinger17 on 23.11.2018.
 */

public class LoginPost {


    public static void send(final String username, final String password, final MainActivity m) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String msg = new String();

                    trustEveryone();
                    bindtoNetwork(m);

                    String echo = "http://scooterlabs.com/echo";
                    String htl = Cfg.logURL;

                    String data = URLEncoder.encode("auth_user", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
                    data += "&" + URLEncoder.encode("auth_pass", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                    data += "&" + URLEncoder.encode("accept", "UTF-8") + "=" + URLEncoder.encode("Anmelden", "UTF-8");


                    URL url = new URL(htl);
                    URLConnection conn = url.openConnection();
                    conn.setRequestProperty("User-Agent", "AutoLogin by Vortexdata | "+Cfg.version);
                    conn.setConnectTimeout(5000);
                    conn.setDoOutput(true);

                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write(data);
                    wr.flush();

                    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;
                    String response = new String();
                    while ((line = rd.readLine()) != null) {
                        response += line+"\n";
                    }

                    if(response.equalsIgnoreCase("Anmeldung erfolgreich") || response.contains("erfolgreich")) {
                        m.ok("Successfully logged in!");
                    } else {
                        //m.ok("Wrong password or username\nResponse Code: "+code);
                        m.ok("Wrong password or username!");
                    }



                    wr.close();
                    rd.close();

                    /*
                    post.addHeader("Referer", "http://10.10.0.251:8002/?zone=cp_htl");
                    post.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:63.0) Gecko/20100101 Firefox/63.0");

                    List<BasicNameValuePair> urlParameters = new ArrayList<BasicNameValuePair>();
                    urlParameters.add(new BasicNameValuePair("auth_user", "mwiesinger17"));
                    urlParameters.add(new BasicNameValuePair("auth_pass", "password"));
                    urlParameters.add(new BasicNameValuePair("accept", "Anmelden"));



                    post.setEntity(new UrlEncodedFormEntity(urlParameters));
                    System.out.println("111");
                    HttpResponse response = client.execute(post);
                    msg += "Response Code : " + response.getStatusLine().getStatusCode()+"\n";

                    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                    StringBuffer result = new StringBuffer();
                    String line = "";
                    while ((line = rd.readLine()) != null) {
                        result.append(line);
                    }
                    msg += "---------"+result;
                    m.ok(msg);

                    */

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    m.error(e.getMessage());
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                    m.error(e.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                    m.error(e.getMessage());
                }


            }
        });
        thread.start();



    }

    private boolean checkWifiOnAndConnected(Context context) {
        WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        if (wifiMgr.isWifiEnabled()) { // Wi-Fi adapter is ON

            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();

            if( wifiInfo.getNetworkId() == -1 ){
                return false; // Not connected to an access point
            }
            return true; // Connected to an access point
        }
        else {
            return false; // Wi-Fi adapter is OFF
        }
    }

    public static void quickSend(final String username, final String password, QuickConn quickConn, Activity a) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String msg = new String();

                    trustEveryone();
                    checkWeb.checkLoginURL();
                    bindtoNetwork(a);

                    String echo = "http://scooterlabs.com/echo";
                    String htl = Cfg.logURL;

                    String data = URLEncoder.encode("auth_user", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
                    data += "&" + URLEncoder.encode("auth_pass", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                    data += "&" + URLEncoder.encode("accept", "UTF-8") + "=" + URLEncoder.encode("Anmelden", "UTF-8");


                    URL url = new URL(htl);
                    URLConnection conn = url.openConnection();
                    conn.setRequestProperty("User-Agent", "AutoLogin by Vortexdata | "+Cfg.version);
                    conn.setConnectTimeout(5000);
                    conn.setDoOutput(true);

                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write(data);
                    wr.flush();

                    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;
                    String response = new String();
                    while ((line = rd.readLine()) != null) {
                        response += line+"\n";
                    }

                    quickConn.response = response;
                    if(response.equalsIgnoreCase("Anmeldung erfolgreich") || response.contains("erfolgreich") || response.contains("connected")) {
                        quickConn.state = "Successfully logged in!"; // DO NOT CHANGE (NOT DISPLAYED)
                        quickConn.statePositive = true;
                        quickConn.done = true;
                    } else {
                        //m.ok("Wrong password or username\nResponse Code: "+code);
                        quickConn.state = "Wrong password or username!"; // DO NOT CHANGE (NOT DISPLAYED)
                        quickConn.statePositive = false;
                        quickConn.done = true;
                    }


                    wr.close();
                    rd.close();


                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                    quickConn.state = "Err";
                    quickConn.done = true;
                    quickConn.statePositive = false;
                }


            }
        });
        thread.start();

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

    public static void bindtoNetwork(Activity a) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            ConnectivityManager connectivityManager = (ConnectivityManager) a.getSystemService(Context.CONNECTIVITY_SERVICE);

            for (Network net : connectivityManager.getAllNetworks()) {

                NetworkInfo networkInfo = connectivityManager.getNetworkInfo(net);

                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    connectivityManager.bindProcessToNetwork(net);
                    break;
                }
            }
        }
    }

}