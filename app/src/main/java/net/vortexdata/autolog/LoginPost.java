package net.vortexdata.autolog;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

import net.vortexdata.autolog.configs.Cfg;
import net.vortexdata.autolog.configs.Msg;

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
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

public class LoginPost {

    private Thread qsend;

    public void send(final String username, final String password, final MainPageFragment m) {
        Thread thread = new Thread(() ->  {
                try {
                    trustEveryone();
                    bindtoNetwork(m.getContext());

                    String data = URLEncoder.encode("auth_user", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
                    data += "&" + URLEncoder.encode("auth_pass", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                    data += "&" + URLEncoder.encode("accept", "UTF-8") + "=" + URLEncoder.encode("Anmelden", "UTF-8");


                    URL url = new URL(Cfg.logURL);
                    URLConnection conn = url.openConnection();
                    conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko");
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

                    if(Cfg.dev) {
                        System.out.println("R: " + response);
                    }

                    if(response.equalsIgnoreCase("Anmeldung erfolgreich") || response.contains("erfolgreich") || response.contains("connected")) {
                        m.ok("Successfully logged in!");
                    } else {
                        //m.ok("Wrong password or username\nResponse Code: "+code);
                        m.ok("Wrong password or username!");
                    }

                    wr.close();
                    rd.close();


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
        });
        thread.start();

    }

    public void quickSend(final String username, final String password, Qconn q) {

        qsend = new Thread(() -> {
                try {

                    trustEveryone();
                    bindtoNetwork(q.getApplicationContext());

                    String data = URLEncoder.encode("auth_user", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
                    data += "&" + URLEncoder.encode("auth_pass", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                    data += "&" + URLEncoder.encode("accept", "UTF-8") + "=" + URLEncoder.encode("Anmelden", "UTF-8");

                    URL url = new URL(Cfg.logURL);
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


                    if(Cfg.dev) {
                        System.out.println("R: " + response);
                    }

                    q.response = response;
                    if(response.equalsIgnoreCase("Anmeldung erfolgreich") || response.contains("erfolgreich") || response.contains("connected")) {
                        q.status = "Successfully logged in!"; // DO NOT CHANGE (NOT DISPLAYED)
                        q.statePositive = true;
                    } else {
                        //m.ok("Wrong password or username\nResponse Code: "+code);
                        q.status = "Wrong password or username!"; // DO NOT CHANGE (NOT DISPLAYED)
                        q.statePositive = false;
                    }

                    wr.close();
                    rd.close();


                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    q.statePositive = false;
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                    q.statePositive = false;
                } catch (IOException e) {
                    e.printStackTrace();
                    q.statePositive = false;
                    q.status = Msg.qConnErr;
                } finally {
                    q.done = true;
                    q.setText();
                    qsend.interrupt();
                    qsend = null;
                }
        });
        qsend.start();

    }

    private static void trustEveryone() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{new X509TrustManager(){
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) {}
                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) {}
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }}}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(
                    context.getSocketFactory());
        } catch (Exception e) { // should never happen
            e.printStackTrace();
        }
    }

    public static void bindtoNetwork(Context a) {
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
