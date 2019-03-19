package net.vortexdata.autolog.updater;

import android.content.Context;
import android.content.Intent;

import net.vortexdata.autolog.Cfg;
import net.vortexdata.autolog.TimeOut;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

public class checkWeb {

    public checkWeb(Context c) {
        System.out.println("Reading Webite..");

        try {
            URL urlLoc = new URL(Cfg.checkWeb);
            trustEveryone();
        HttpsURLConnection conexion = (HttpsURLConnection) urlLoc.openConnection();
        conexion.setConnectTimeout(4000);
        conexion.setReadTimeout(1000);
        conexion.connect();

        // downlod the file
        InputStream input = new BufferedInputStream(urlLoc
                .openStream());

        StringBuffer responseBuffer = new StringBuffer();
        byte[] byteArray = new byte[1024];
        while (input.read(byteArray) != -1) {
            String res = new String(byteArray, "UTF-8");
            responseBuffer.append(res);
            byteArray = null;
            byteArray = new byte[1024];
        }

        String[] response = responseBuffer.toString().trim().split(";");
            //System.out.println("RESPONSE " + response);

            if(response.length > 1) {
                if (response[0].contains("outdated") && response[1].contains(Cfg.version)) {
                    Cfg.expired = true;
                    Intent i = new Intent(c, TimeOut.class);
                    c.startActivity(i);
                }
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void trustEveryone() {
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

}
