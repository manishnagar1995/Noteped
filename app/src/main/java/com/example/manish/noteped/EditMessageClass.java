package com.example.manish.noteped;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import static android.util.TypedValue.COMPLEX_UNIT_SP;

public class EditMessageClass extends AppCompatActivity implements OnClickListener {

    MultiAutoCompleteTextView mainview;
    String copydata;
    Button btn_bold, btn_italic, btn_normal, btn_size, btn_copydata;
    boolean bold, italic;

    int textSize;

    private String messageString;
    private boolean doubleBackToExit;
    private int position;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_layout);
        Intent intent = getIntent();
        messageString = intent.getStringExtra(Intent_Constants.INTENT_MESSAGE_DATA);
        position = intent.getIntExtra(Intent_Constants.INTENT_ITEM_POSITION, -1);


        btn_bold = findViewById(R.id.boldtext);
        btn_italic = findViewById(R.id.italictext);
        btn_normal = findViewById(R.id.normaltext);
        btn_size = findViewById(R.id.fontsize);
        btn_copydata = findViewById(R.id.copytext);

        String[] a = intent.getStringExtra(Intent_Constants.INTENT_ITEM_TEXT_PROPERTIES).split(" ");
        if (a[0].equalsIgnoreCase("true")) {
            bold = true;
            btn_bold.setTypeface(btn_bold.getTypeface(), Typeface.BOLD);
        }
        if (a[0].equalsIgnoreCase("true")) {
            italic = true;
            btn_italic.setTypeface(btn_italic.getTypeface(), Typeface.ITALIC);
        }
        textSize = Integer.parseInt(a[2]);

        btn_size.setText("Size : " + textSize + "sp");


        EditText editText = findViewById(R.id.messagebox);
        editText.setText(String.valueOf(messageString));
        editText.setSelection(messageString.length());
        editText.setTextSize(COMPLEX_UNIT_SP, textSize);
        if (bold && italic) editText.setTypeface(editText.getTypeface(), Typeface.BOLD_ITALIC);
        else if (bold)
            editText.setTypeface(editText.getTypeface(), Typeface.BOLD);
        else if (italic)
            editText.setTypeface(editText.getTypeface(), Typeface.ITALIC);


        mainview = findViewById(R.id.messagebox);

        btn_bold.setOnClickListener(this);
        btn_italic.setOnClickListener(this);
        btn_normal.setOnClickListener(this);
        btn_size.setOnClickListener(this);
        btn_copydata.setOnClickListener(this);

        registerForContextMenu(btn_size);
    }

    public void onClick(View view) {
        if (view == btn_bold) {
            if (bold == false && italic == true) {
                Typeface setfontstyle = Typeface.defaultFromStyle(Typeface.BOLD_ITALIC);
                mainview.setTypeface(setfontstyle);
                bold = true;
                btn_bold.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            } else if (bold == false && italic == false) {
                Typeface setfontstyle = Typeface.defaultFromStyle(Typeface.BOLD);
                mainview.setTypeface(setfontstyle);
                bold = true;
                btn_bold.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            } else if (bold == true && italic == false) {
                Typeface setfontstyle = Typeface.defaultFromStyle(Typeface.NORMAL);
                bold = false;
                btn_bold.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                mainview.setTypeface(setfontstyle);
            } else if (bold == true && italic == true) {
                Typeface setfontstyle = Typeface.defaultFromStyle(Typeface.ITALIC);
                mainview.setTypeface(setfontstyle);
                bold = false;
                btn_bold.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            }
        } else if (view == btn_italic) {
            if (italic == false && bold == false) {
                Typeface setfontstyle = Typeface.defaultFromStyle(Typeface.ITALIC);
                mainview.setTypeface(setfontstyle);
                italic = true;
                btn_italic.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
            } else if (italic == false && bold == true) {
                Typeface setfontstyle = Typeface.defaultFromStyle(Typeface.BOLD_ITALIC);
                mainview.setTypeface(setfontstyle);
                italic = true;
                btn_italic.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
            } else if (italic == true && bold == false) {
                Typeface setfontstyle = Typeface.defaultFromStyle(Typeface.NORMAL);
                mainview.setTypeface(setfontstyle);
                italic = false;
                btn_italic.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            } else if (italic == true && bold == true) {
                Typeface setfontstyle = Typeface.defaultFromStyle(Typeface.BOLD);
                mainview.setTypeface(setfontstyle);
                italic = false;
                btn_italic.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            }
        } else if (view == btn_normal) {
            Typeface setfontstyle = Typeface.defaultFromStyle(Typeface.NORMAL);
            mainview.setTypeface(setfontstyle);
            bold = false;
            italic = false;
        } else if (view == btn_size) {
            view.showContextMenu();
        } else if (view == btn_copydata) {
            copydata = mainview.getText().toString();
            copydata = mainview.getText().toString();
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("lable", copydata);
            clipboard.setPrimaryClip(clip);
        }
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select Font Size");
        for (int i = 5; i < 60; i++) {
            menu.add(0, v.getId(), 0, i + "");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        for (int i = 5; i < 60; i++) {
            if (item.getTitle().equals(i + "")) {
                mainview.setTextSize(COMPLEX_UNIT_SP, i);
                btn_size.setText("Size : " + i + "sp");
                textSize = i;
                break;
            }
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.task_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.save_settings) {
            String changedMessageText = ((EditText) findViewById(R.id.messagebox)).getText().toString();
            if (changedMessageText.isEmpty()) {

            } else {
                setResultSucessfully(changedMessageText);
            }
            return true;
        } else if (id == R.id.delete_settings) {
            setResultDelete();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void setResultSucessfully(String messageText) {
        Intent intent = new Intent();
        intent.putExtra(Intent_Constants.INTENT_CHANGED_MESSAGE, messageText);
        intent.putExtra(Intent_Constants.INTENT_ITEM_POSITION, position);
        intent.putExtra(Intent_Constants.INTENT_ITEM_BOLD, bold);
        intent.putExtra(Intent_Constants.INTENT_ITEM_ITALIC, italic);
        intent.putExtra(Intent_Constants.INTENT_ITEM_TEXTSIZE, textSize);
        setResult(Intent_Constants.INTENT_REQUEST_CODE_TWO, intent);
        finish();
    }

    void setResultDelete() {
        Intent intent = new Intent();
        intent.putExtra(Intent_Constants.INTENT_ITEM_POSITION, position);
        setResult(Intent_Constants.INTENT_DELETE_CODE, intent);
        finish();
    }

    void setResultErrorCode() {
        setResult(Intent_Constants.INTENT_ERROR_CODE, new Intent());
        finish();
    }

    @Override
    public void onBackPressed() {
        String changedMessageText = ((EditText) findViewById(R.id.messagebox)).getText().toString();
        if (changedMessageText.isEmpty() && doubleBackToExit) {
            setResultErrorCode();
        } else if (!changedMessageText.isEmpty()) {
            setResultSucessfully(changedMessageText);
        } else {
            this.doubleBackToExit = true;
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExit = false;
            }
        }, 2000);
    }
}
