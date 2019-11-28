package net.vortexdata.autolog;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import org.json.JSONObject;

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
    SwipeRefreshLayout refresh;
    public static NewsAdapter na;
    public static NewsFragment newsFrag;

    public NewsFragment() {
        newsFrag = this;
        //getNews();
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
        refresh = v.findViewById(R.id.refresh);

        Thread t = new Thread(() -> {
           na = new NewsAdapter(newsFrag);
           newsList.setAdapter(na);
           newsList.setDivider(new ColorDrawable(Color.TRANSPARENT));
           newsList.setDividerHeight(20);
           newsList.setClickable(true);
           newsList.setOnItemClickListener((parent, view, position, id) -> BasicMethods.readNews(newsFrag, position));
        });

        t.start();
        getNews();


        if (Cfg.fancyBackground) {
            BasicMethods.setFancyBackground(bg, getContext());
        }

        back.setOnClickListener(view -> {
            home.main.vp.setCurrentItem(1, true);
        });

        refresh.setOnRefreshListener(() -> {
            getNews();
            na.notifyDataSetChanged();
            refresh.setRefreshing(false);
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

        Thread getNews = new Thread(() -> {
            try {
                NewsFeed.clear();
                if(NewsFeed.size() > 1) return;

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
                    byteArray = new byte[1024];
                }

                String response = responseBuffer.toString();
                connection.disconnect();

                try {
                    JSONArray arr = new JSONArray(response);
                    for(int i = 0; i < arr.length(); i++) {
                        parseMessage(arr, i, false);
                    }
                    if(Cfg.dev) {
                        loadNews();
                    }
                    if(NewsFeed.size() >= 1) {
                        try{
                            getActivity().runOnUiThread(() -> na.notifyDataSetChanged());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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

    public static void saveNews(Context c) {
        try {
            JSONArray arr = new JSONArray();

            for(News n : NewsFeed) {
                JSONObject o = new JSONObject();

                o.put("id", n.getId());
                o.put("headline",n.getHeadline());
                o.put("text", n.getText());
                o.put("date", n.getDate());
                o.put("creator", n.getCreator());
                o.put("category", n.getCategory());
                o.put("read", n.isRead());
                arr.put(o);
            }

            SharedPreferences.Editor editor = c.getSharedPreferences("news", 0).edit();
            editor.putString("news", arr.toString());
            editor.apply();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadNews() {
        boolean cont = true;
        SharedPreferences prefs = getActivity().getSharedPreferences("news", 0);
        JSONArray arr = null;
        try {
            arr = new JSONArray(prefs.getString("news", ""));
        } catch (Exception e) {
            //e.printStackTrace(); will be thrown if no there are no saves
            cont = false;
        }

        JSONArray finalArr = arr;
        Thread t = new Thread(() -> {
            try {

                for(int i = 0; i < finalArr.length(); i++) {
                    parseMessage(finalArr, i, true);
                }
                getActivity().runOnUiThread(() -> na.notifyDataSetChanged());

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        if(cont) {
            t.start();
        }
    }

    private void parseMessage(JSONArray arr, int i, boolean saved) {
        Thread parse = new Thread(() -> {
            try {
                JSONObject o = arr.getJSONObject(i);
                News n = new News(o.getInt("id"), o.getString("headline"), o.getString("text"),  o.getString("date"),  o.getString("creator"), o.getString("category"));
                if(NewsFeed.contains(n)) {
                    if(Cfg.dev) {
                        System.out.println("contains!! " + n.getHeadline());
                    }
                    if(saved) {
                        int index = NewsFeed.indexOf(n);
                        News feed = NewsFeed.get(index);
                        feed.setRead(true);
                        NewsFeed.set(index, feed);
                    }
                } else {
                    if(Cfg.dev) {
                        System.out.println("adding.. " + n.getHeadline());
                    }
                    NewsFeed.add(n);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            getActivity().runOnUiThread(() -> na.notifyDataSetChanged());
        });
        parse.start();

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
