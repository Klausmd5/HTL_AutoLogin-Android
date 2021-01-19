package net.vortexdata.autolog.objects;
import androidx.annotation.Nullable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class News implements Comparable<News> {

    private int id;
    private String displayID;
    private String title;
    private String text;
    private String category;
    private String author;
    private boolean read = false;

    public News(int id, String displayID, String title) {
        this.id = id;
        this.displayID = displayID;
        this.title = title;
        category = "global";
        author = "postcord";
    }

    public String getCategory() {
        return category;
    }

    public String getDisplayID() {
        return displayID;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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
            if(((News) obj).getId() == getId() && ((News) obj).getTitle().equals(getTitle()) && ((News) obj).getDisplayID().equals(getDisplayID())) {
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
