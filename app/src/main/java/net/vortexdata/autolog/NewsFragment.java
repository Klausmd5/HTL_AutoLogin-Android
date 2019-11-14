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

public class NewsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    ConstraintLayout bg;
    ListView newsList;
    TextView header;
    TextView back;

    NewsAdapter na;

    public NewsFragment() {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_news, container, false);

        bg = v.findViewById(R.id.bg_news);
        newsList = v.findViewById(R.id.newsList);
        back = v.findViewById(R.id.back_news);
        header = v.findViewById(R.id.header_news);

        na = new NewsAdapter(home.main);
        newsList.setAdapter(na);

        if (Cfg.fancyBackground) {
            Settings.setFancyBackground(bg, getContext());
        }

        return v;
    }


    public void onButtonPressed(Uri uri) {

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
}
