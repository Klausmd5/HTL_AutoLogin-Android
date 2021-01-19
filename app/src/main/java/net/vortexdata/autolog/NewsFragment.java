package net.vortexdata.autolog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import net.vortexdata.autolog.adapter.NewsAdapter;
import net.vortexdata.autolog.configs.Cfg;
import net.vortexdata.autolog.objects.News;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import javax.net.ssl.HttpsURLConnection;

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

        na = new NewsAdapter(newsFrag);
        newsList.setAdapter(na);
        newsList.setDivider(new ColorDrawable(Color.TRANSPARENT));
        newsList.setDividerHeight(20);
        newsList.setClickable(true);
        newsList.setOnItemClickListener((parent, view, position, id) -> BasicMethods.readNews(newsFrag, position));
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
                BasicMethods.trustEveryone();
                HttpURLConnection connection = (HttpURLConnection) urlLoc.openConnection();
                //HttpsURLConnection connection = (HttpsURLConnection) urlLoc.openConnection();
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

                    if(NewsFeed.size() >= 1) {
                        try{
                            Collections.sort(NewsFeed);
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

    public static void getPostExact(News n, TextView by, TextView text) {

        if(Cfg.dev) {
            System.out.println("Fetching post exact.. from: "+Cfg.newsFeed+"/"+n.getDisplayID());
        }

        Thread getPost = new Thread(() -> {
            try {
                URL urlLoc = new URL(Cfg.newsFeed+"/"+n.getDisplayID());
                BasicMethods.trustEveryone();
                HttpURLConnection connection = (HttpURLConnection) urlLoc.openConnection();
                //HttpsURLConnection connection = (HttpsURLConnection) urlLoc.openConnection();
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

                    JSONObject o = new JSONObject(response);
                   //n = new News(o.getInt("id"), o.getString("displayID"), o.getString("title"));
                    n.setAuthor(o.getJSONObject("author").getString("username"));
                    //n.setText();
                    JSONArray paragraphs = o.getJSONArray("paragraphs");
                    n.setText(paragraphs.getJSONObject(0).getString("message"));

                    by.setText(n.getAuthor());
                    text.setText(n.getText());

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        getPost.start();
    }

    private void parseMessage(JSONArray arr, int i, boolean saved) {
        Thread parse = new Thread(() -> {
            try {
                JSONObject o = arr.getJSONObject(i);
                News n = new News(o.getInt("id"), o.getString("displayID"), o.getString("title"));
                if(NewsFeed.contains(n)) {
                    if(Cfg.dev) {
                        System.out.println("contains!! " + n.getTitle());
                    }
                    if(saved) {
                        int index = NewsFeed.indexOf(n);
                        News feed = NewsFeed.get(index);
                        feed.setRead(true);
                        NewsFeed.set(index, feed);
                    }
                } else {
                    if(Cfg.dev) {
                        System.out.println("adding.. " + n.getTitle());
                        System.out.println("adding.. " + n.getTitle());
                    }
                    NewsFeed.add(n);
                    Collections.sort(NewsFeed);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            getActivity().runOnUiThread(() -> na.notifyDataSetChanged());
        });
        parse.start();

    }


}
