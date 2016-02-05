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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.jar.Attributes;

/**
 * Created by JordanTaylor on 1/26/16.
 */
public class BookNoteActivity extends ListActivity {

    private ArrayAdapter<Bookmark> adapter;
    private final String FILE_BOOKMARK = "Bookmarks.csv";

    String url, title, note;
    int pos;

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

        Intent intent = new Intent(BookNoteActivity.this, ManageActivity.class);
        switch(id) {
            case R.id.action_add:
                intent.putExtra("action", "create");
                BookNoteActivity.this.startActivityForResult(intent, 100);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.i("RESULTS CODE :: ", resultCode + "");
        switch(resultCode) {
            case 100:
                adapter.add(new Bookmark(intent.getExtras().get("url").toString(),
                        intent.getExtras().get("title").toString(),
                        intent.getExtras().get("note").toString()));

                //writeData(intent.getExtras().get("url").toString(), intent.getExtras().get("title").toString(), intent.getExtras().get("note").toString());
                break;
            case 200:
                adapter.remove(adapter.getItem(pos ));
                adapter.notifyDataSetChanged();
                adapter.add(new Bookmark(intent.getExtras().get("url").toString(),
                        intent.getExtras().get("title").toString(),
                        intent.getExtras().get("note").toString()));
                adapter.notifyDataSetChanged();

                break;
        }
    }


    @Override
    protected void onListItemClick (ListView l, View v, int position, long id) {
        Intent intent = new Intent(BookNoteActivity.this, ManageActivity.class);
        intent.putExtra("action", "update");
        intent.putExtra("url", adapter.getItem(position).getUrl());
        intent.putExtra("title", adapter.getItem(position).getTitle());
        intent.putExtra("note", adapter.getItem(position).getNote());

        pos = position;

        BookNoteActivity.this.startActivityForResult(intent, 200);
    }

    private ArrayList<Bookmark> readData() {
        ArrayList<Bookmark> bookmarks = new ArrayList<>();

        try {
            FileInputStream fis = openFileInput(FILE_BOOKMARK);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line;
            while((line = br.readLine()) != null) {
                String[] temp = line.split(",");

                String url = temp[0];
                String title = temp[1];
                String note = temp[2];
                Bookmark bookmark = new Bookmark(url, title, note);
                bookmarks.add(bookmark);
            }


        } catch (FileNotFoundException e) {
            //
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bookmarks;
    }

    public void writeData(String url, String title, String note) {
        try {
            FileOutputStream fos = openFileOutput(FILE_BOOKMARK, Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);
            PrintWriter pw = new PrintWriter(bw);

            checkEmpty();
            pw.println(url + "," + title + "," + note);

            /*
            // format: url, title, note
            for (int i = 0; i < adapter.getCount(); i++) {
                Bookmark bookmark = adapter.getItem(i);
                pw.println(bookmark.getUrl() + "," + bookmark.getTitle() + "," + bookmark.getNote());
            }
            */

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
