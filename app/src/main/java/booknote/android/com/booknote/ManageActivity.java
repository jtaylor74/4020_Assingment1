package booknote.android.com.booknote;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by JordanTaylor on 1/26/16.
 */
public class ManageActivity extends Activity {
    private EditText etUrl, etTitle, etNote;
    private Button btnUrl;
    public String mAction, mUrl, mTitle, mNote;
    public Intent mIntent, i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        mIntent = getIntent();
        mAction = mIntent.getStringExtra("action");

        etUrl = (EditText) findViewById(R.id.etUrl);
        etTitle = (EditText) findViewById(R.id.etTitle);
        etNote = (EditText) findViewById(R.id.etNote);

        if (mAction.equals("update")) {
            etUrl.setText(mIntent.getStringExtra("url"));
            etTitle.setText(mIntent.getStringExtra("title"));
            etNote.setText(mIntent.getStringExtra("note"));
        }

        i = new Intent();

        btnUrl = (Button) findViewById(R.id.btn_url);
        btnUrl.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String url = etUrl.getText().toString();

                if (!url.contains("http://")) {
                    url = "http://" + url;
                }

                Intent browse = new Intent(Intent.ACTION_VIEW , Uri.parse(url));
                startActivity(browse);
            }

        });
    }

    @Override
    public void onBackPressed() {
        switch(mAction) {
            case "create":
                Log.i("ACTION :: ", "CREATE");
                mUrl = etUrl.getText().toString();
                mTitle = etTitle.getText().toString();
                mNote = etNote.getText().toString();

                createBookmark(mUrl, mTitle, mNote);

                i.putExtra("url", mUrl);
                i.putExtra("title", mTitle);
                i.putExtra("note", mNote);

                setResult(100, i);
                finish();
                break;
            case "update":
                Log.i("ACTION :: ", "UPDATE");

                mUrl = etUrl.getText().toString();
                mTitle = etTitle.getText().toString();
                mNote = etNote.getText().toString();

                //updateBookmark(mUrl, mTitle, mNote);

                i.putExtra("url", mUrl);
                i.putExtra("title", mTitle);
                i.putExtra("note", mNote);

                setResult(200, i);
                finish();
                break;

            case "delete":

                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            switch(mAction) {
                case "create":
                    Log.i("ACTION :: ", "CREATE");
                    mUrl = etUrl.getText().toString();
                    mTitle = etTitle.getText().toString();
                    mNote = etNote.getText().toString();

                    checkEmpty();

                    //createBookmark(mUrl, mTitle, mNote);

                    i.putExtra("url", mUrl);
                    i.putExtra("title", mTitle);
                    i.putExtra("note", mNote);

                    setResult(100, i);
                    finish();
                    break;
                case "update":
                    Log.i("ACTION :: ", "UPDATE");

                    mUrl = etUrl.getText().toString();
                    mTitle = etTitle.getText().toString();
                    mNote = etNote.getText().toString();

                    checkEmpty();

                    //updateBookmark(mUrl, mTitle, mNote);

                    i.putExtra("url", mUrl);
                    i.putExtra("title", mTitle);
                    i.putExtra("note", mNote);

                    setResult(200, i);
                    finish();
                    break;

                case "delete":

                    break;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void createBookmark (String url, String title, String note){
        mUrl = url;
        mTitle = title;
        mNote = note;
    }

    private void checkEmpty() {
        if (mUrl.equals("")){
            mUrl = "EMPTY";
        }
        if (mTitle.equals("")) {
            mTitle = "EMPTY";
        }
        if (mNote.equals("")) {
            mNote = "EMPTY";
        }
    }
}
