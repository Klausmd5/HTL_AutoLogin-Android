package net.vortexdata.autolog.objects;

import android.support.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class News implements Comparable<News> {

    private int id;
    private String headline;
    private String text;
    private String date;
    private String creator;
    private String category;
    private boolean read = false;

    public News(int id, String headline, String text, String date, String creator, String category) {
        this.id = id;
        this.headline = headline;
        this.text = text;
        this.creator = creator;
        this.category = category;
        try {
            SimpleDateFormat base = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            Date baseDate = base.parse(date);
            SimpleDateFormat form = new SimpleDateFormat("HH:mm dd.MM.yy");
            this.date = form.format(baseDate);
        } catch (ParseException e) {
            //e.printStackTrace();
            this.date = date;
        }
    }

    public String getHeadline() {
        return headline;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }

    public String getCreator() {
        return creator;
    }

    public String getCategory() {
        return category;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof News) {
            if(((News) obj).getId() == getId() && ((News) obj).getHeadline().equals(getHeadline()) && ((News) obj).getCreator().equals(getCreator())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int compareTo(News o) {

        if(o.getId() > this.getId()) {
            return 1;
        } else {
            return 0;
        }
    }
}
