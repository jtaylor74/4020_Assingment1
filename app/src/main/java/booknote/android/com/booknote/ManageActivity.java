package booknote.android.com.booknote;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by JordanTaylor on 1/26/16.
 */
public class ManageActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
    }

        public static Bookmark createBookmark (String url, String title, String note){
            return new Bookmark(url, title, note);
        }

    public static void updateBookmark(Bookmark bm, String url, String title, String note) {

        bm.setUrl(url);
        bm.setmTitle(title);
        bm.setNote(url);
    }
}
