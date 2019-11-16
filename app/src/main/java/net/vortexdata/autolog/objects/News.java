package net.vortexdata.autolog.objects;

import java.text.SimpleDateFormat;
import java.util.Date;

public class News {

    private String headline;
    private String text;
    private Date date; // is parsed to string and will not be edited.
    private String creator;

    public News(String headline, String text, String date, String creator) {
        this.headline = headline;
        this.text = text;
        this.creator = creator;
        try {
            this.date = new SimpleDateFormat("dd.MM.yyyy").parse(date);
        } catch (Exception e) {
            this.date = new Date();
        }
    }

    public String getHeadline() {
        return headline;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date.toString();
    }

    public String getCreator() {
        return creator;
    }
}
