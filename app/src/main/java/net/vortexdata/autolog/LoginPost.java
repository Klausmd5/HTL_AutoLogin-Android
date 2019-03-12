package net.vortexdata.autolog;

import org.apache.http.client.ClientProtocolException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

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



                    String echo = "http://scooterlabs.com/echo";
                    String htl = "http://10.10.0.251:8002/index.php?zone=cp_htl";

                    String data = URLEncoder.encode("auth_user", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
                    data += "&" + URLEncoder.encode("auth_pass", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                    data += "&" + URLEncoder.encode("accept", "UTF-8") + "=" + URLEncoder.encode("Anmelden", "UTF-8");


                    URL url = new URL(htl);
                    URLConnection conn = url.openConnection();
                    conn.setRequestProperty("User-Agent", "AutoLogin by VarChar42 | "+R.string.version);
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
                    int code = ((HttpURLConnection) conn).getResponseCode();
                    if(code == 302) {
                        m.ok("Successfully logged in!");
                    } else {
                        m.ok("Wrong password or username\nResponse Code: "+code);
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
}
