package net.vortexdata.autolog;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import net.vortexdata.autolog.adapter.NewsAdapter;
import net.vortexdata.autolog.configs.Cfg;
import net.vortexdata.autolog.objects.News;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

public class NewsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    public static ArrayList<News> NewsFeed = new ArrayList<News>() {};

    public ConstraintLayout bg;
    ListView newsList;
    TextView header;
    TextView back;

    NewsAdapter na;

    public NewsFragment() {
        getNews();
    }

    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
        getNews();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_news, container, false);

        bg = v.findViewById(R.id.bg_news);
        newsList = v.findViewById(R.id.newsList);
        back = v.findViewById(R.id.back_news);
        header = v.findViewById(R.id.header_news);

        na = new NewsAdapter(this);
        newsList.setAdapter(na);
        newsList.setDividerHeight(15);
        newsList.setClickable(true);
        newsList.setOnItemClickListener((parent, view, position, id) -> {
            BasicMethods.readNews(this, position);
        });

        if (Cfg.fancyBackground) {
            BasicMethods.setFancyBackground(bg, getContext());
        }

        back.setOnClickListener(view -> {
            home.main.vp.setCurrentItem(1, true);
        });

        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void getNews() {

        if(Cfg.dev) {
            System.out.println("Fetching news..");
        }

        NewsFeed.clear();
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
                        NewsFeed.add(new News(arr.getJSONObject(i).getString("headline"), arr.getJSONObject(i).getString("text"),  arr.getJSONObject(i).getString("date"),  arr.getJSONObject(i).getString("creator")));
                        if(NewsFeed.size() > 1) {
                            try{
                                na.notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }



                connection.disconnect();

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
}
