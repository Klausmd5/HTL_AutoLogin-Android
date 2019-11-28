package net.vortexdata.autolog.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.vortexdata.autolog.BasicMethods;
import net.vortexdata.autolog.NewsFragment;
import net.vortexdata.autolog.R;
import net.vortexdata.autolog.configs.Cfg;
import net.vortexdata.autolog.home;
import net.vortexdata.autolog.objects.News;

public class NewsAdapter extends BaseAdapter {

    private NewsFragment n;

    public NewsAdapter(NewsFragment n) {
        this.n = n;
    }

    @Override
    public int getCount() {
        return n.NewsFeed.size();
    }

    @Override
    public Object getItem(int i) {
        return NewsFragment.NewsFeed.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater layoutInflater = (LayoutInflater) n.getContext().getSystemService(home.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.newslayout, null);

        TextView head = view.findViewById(R.id.headline);
        TextView text = view.findViewById(R.id.text);
        ImageView icon = view.findViewById(R.id.icon);
        TextView date = view.findViewById(R.id.date);
        TextView newIcon = view.findViewById(R.id.newIcon);

        News news = n.NewsFeed.get(i);
        // Layout
        BasicMethods.setNewsIcons(news, icon, view);
        head.setText(news.getHeadline());
        if(!Cfg.dev) {
            newIcon.setVisibility(View.INVISIBLE);
        }
        if(news.isRead()) {
            newIcon.setVisibility(View.INVISIBLE);
        }
        //text.setText(news.getText().length() > 51 ? news.getText().substring(0, 51) + Html.fromHtml( ".... <br/><strong>read more(click me)</strong>") : news.getText());
        date.setText(news.getDate());

        return view;

    }
}
