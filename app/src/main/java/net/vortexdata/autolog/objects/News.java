package net.vortexdata.autolog.objects;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class News {

    private String headline;
    private String text;
    private String date;
    private String creator;
    private String category;

    public News(String headline, String text, String date, String creator, String category) {
        this.headline = headline;
        this.text = text;
        this.creator = creator;
        this.category = category;
        try {
            SimpleDateFormat base = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            Date baseDate = base.parse(date);
            SimpleDateFormat form = new SimpleDateFormat("HH:mm dd.MM.yy");
            this.date = form.format(baseDate);
        } catch (Exception e) {
            e.printStackTrace();
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
}
