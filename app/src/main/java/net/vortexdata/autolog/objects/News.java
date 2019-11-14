package net.vortexdata.autolog.objects;

public class News {

    private String headline;
    private String text;

    public News(String headline, String text) {
        this.headline = headline;
        this.text = text;
    }

    public String getHeadline() {
        return headline;
    }

    public String getText() {
        return text;
    }
}
