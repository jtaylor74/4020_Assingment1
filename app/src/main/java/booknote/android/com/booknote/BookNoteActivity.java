package booknote.android.com.booknote;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.jar.Attributes;

/**
 * Created by JordanTaylor on 1/26/16.
 */
public class BookNoteActivity extends ListActivity {

    private ArrayAdapter<Bookmark> adapter;
    private final String FILE_BOOKMARK = "Bookmarks.csv";

    String url, title, note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<Bookmark> bookmarks = readData();

        adapter = new ArrayAdapter<Bookmark>(this, android.R.layout.simple_list_item_1, bookmarks);
        setListAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        // intent action: 1 - create bookmark, 2 - edit bookmark, 3 - delete bookmark

        switch(id) {
            case R.id.action_add:
                Intent intent = new Intent(BookNoteActivity.this, ManageActivity.class);
                intent.putExtra("action", 1);
                BookNoteActivity.this.startActivity(intent);
                return true;
            case R.id.action_edit:

                return true;
            case R.id.action_delete:

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onListItemClick (ListView l, View v, int position, long id) {

    }

    private ArrayList<Bookmark> readData() {
        ArrayList<Bookmark> bookmarks = new ArrayList<>();

        try {
            FileInputStream fis = openFileInput(FILE_BOOKMARK);
            Scanner scanner = new Scanner(fis);
            while (scanner.hasNext()) {
                String url = scanner.next();
                String title = scanner.next();
                String note = scanner.next();
                Bookmark bookmark = new Bookmark(url, title, note);
                bookmarks.add(bookmark);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return bookmarks;
    }

    public void writeData() {
        try {
            FileOutputStream fos = openFileOutput(FILE_BOOKMARK, Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);
            PrintWriter pw = new PrintWriter(bw);

            // format: url, title, note
            for (int i = 0; i < adapter.getCount(); i++) {
                Bookmark bookmark = adapter.getItem(i);
                pw.println(bookmark.getUrl() + ", " + bookmark.getTitle() + ", " + bookmark.getNote());
            }

            pw.close();
        } catch (FileNotFoundException e) {
            Log.e("WRITE ERROR :: ", "Cannot save data: " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(this, getString(R.string.error_write), Toast.LENGTH_SHORT).show();
        }
    }

    private void checkEmpty() {
        if (url.equals("")){
            url = "EMPTY";
        }
        if (title.equals("")) {
            title = "EMPTY";
        }
        if (note.equals("")) {
            note = "EMPTY";
        }
    }
}
