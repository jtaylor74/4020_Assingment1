package booknote.android.com.booknote;

/**
 * Created by JordanTaylor on 1/26/16.
 */
public class Bookmark {
    String mUrl;
    String mTitle;
    String mNote;

    Bookmark() {

    }

    Bookmark(String url, String title, String note) {
        this.mUrl = url;
        this.mTitle = title;
        this.mNote = note;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setmTitle(String title) {
        this.mTitle = title;
    }

    public String getNote() {
        return mNote;
    }

    public void setNote(String note) {
        this.mNote = note;
    }
}
