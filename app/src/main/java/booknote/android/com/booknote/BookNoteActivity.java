package booknote.android.com.booknote;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by JordanTaylor on 1/26/16.
 */
public class BookNoteActivity extends ListActivity {

    private ArrayAdapter<Bookmark> adapter;
    private final String FILE_BOOKMARK = "Bookmarks.csv";

    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<Bookmark> bookmarks = readData();

        adapter = new ArrayAdapter<Bookmark>(this, android.R.layout.simple_list_item_1, bookmarks);
        setListAdapter(adapter);

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Log.i("POS ::", arg2 + "");
                dialogDelete(adapter.getItem(arg2));
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

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
    public void onDestroy() {
        super.onDestroy();
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
                adapter.notifyDataSetChanged();

                writeData();

                break;
            case 200:
                adapter.remove(adapter.getItem(pos));
                adapter.notifyDataSetChanged();
                adapter.add(new Bookmark(intent.getExtras().get("url").toString(),
                        intent.getExtras().get("title").toString(),
                        intent.getExtras().get("note").toString()));
                adapter.notifyDataSetChanged();

                writeData();

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

    private void dialogDelete(final Bookmark bm) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_delete));
        builder.setMessage(getString(R.string.message_delete));
        builder.setPositiveButton("Delete",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        adapter.remove(bm);
                        writeData();
                    }
                });

        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
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

    private void writeData() {
        try {
            FileOutputStream fos = openFileOutput(FILE_BOOKMARK, Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);
            PrintWriter pw = new PrintWriter(bw);

            // format: url, title, note
            for (int i = 0; i < adapter.getCount(); i++) {
                Bookmark bookmark = adapter.getItem(i);
                pw.println(bookmark.getUrl() + "," + bookmark.getTitle() + "," + bookmark.getNote());
            }

            pw.close();
        } catch (FileNotFoundException e) {
            Log.e("WRITE ERROR :: ", "Cannot save data: " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(this, getString(R.string.error_write), Toast.LENGTH_SHORT).show();
        }
    }
}
