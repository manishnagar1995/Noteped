package com.example.manish.noteped;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private View lastFocusView;
    private int lastFocusPosition;

    private ListView mListView;
    private ArrayList<String> arrayList;
    private ArrayList<String> arrayTextPro;
    private CustomAdapter arrayAdapter;
    private String messageString;
    private int position;
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arrayList = new ArrayList<String>();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mListView = findViewById(R.id.listView);
        arrayList = new ArrayList<>();
        arrayTextPro = new ArrayList<>();

        arrayAdapter = new CustomAdapter(this, arrayList);
        try {
            Scanner scanner = new Scanner(openFileInput("TodoTask.txt"));
            while (scanner.hasNext()) {
                messageString = decodeData(scanner.nextLine());
                arrayAdapter.add(messageString);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mListView.setAdapter(arrayAdapter);

        mEmptyStateTextView = findViewById(R.id.empty_view);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (lastFocusView != null) removeFocusSetting(view, position);
                setFocusSetting(view, position);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lastFocusView != null) removeFocusSetting(lastFocusView, lastFocusPosition);
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, EditFieldClass.class);
                startActivityForResult(intent, Intent_Constants.INTENT_REQUEST_CODE);
            }
        });

        if (arrayList.size() <= 0) mEmptyStateTextView.setText("No TODO List");
        else mEmptyStateTextView.setVisibility(View.INVISIBLE);
    }

    private void removeFocusSetting(View view, int position) {
        TextView t = lastFocusView.findViewById(R.id.textView);
        t.setMaxLines(2); //As in the android sourcecode
        t.setEllipsize(TextUtils.TruncateAt.END);
        t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        t.setTypeface(null, Typeface.NORMAL);
        ImageView i = lastFocusView.findViewById(R.id.delete);
        ImageView i2 = lastFocusView.findViewById(R.id.edit);
        i.setVisibility(View.GONE);
        i2.setVisibility(View.GONE);
        lastFocusView.setBackgroundResource(R.drawable.shape);
    }

    private void setFocusSetting(View view, int position) {

        ImageView edit = view.findViewById(R.id.delete);
        ImageView delete = view.findViewById(R.id.edit);
        edit.setVisibility(View.VISIBLE);
        delete.setVisibility(View.VISIBLE);
        view.setBackgroundResource(R.drawable.shape2);

        TextView t = view.findViewById(R.id.textView);
        String[] a = arrayTextPro.get(position).split(" ");
        if (a[0].equalsIgnoreCase("true") && a[1].equalsIgnoreCase("true"))
            t.setTypeface(t.getTypeface(), Typeface.BOLD_ITALIC);
        else if (a[0].equalsIgnoreCase("true"))
            t.setTypeface(t.getTypeface(), Typeface.BOLD);
        else if (a[0].equalsIgnoreCase("true"))
            t.setTypeface(t.getTypeface(), Typeface.ITALIC);
        t.setMaxLines(Integer.MAX_VALUE); //As in the android sourcecode
        t.setEllipsize(null);
        t.setTextSize(TypedValue.COMPLEX_UNIT_SP, Integer.parseInt(a[2]));

        lastFocusView = view;
        lastFocusPosition = position;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Intent_Constants.INTENT_ERROR_CODE) {
        } else if (resultCode == Intent_Constants.INTENT_DELETE_CODE) {
            if (data.hasExtra(Intent_Constants.INTENT_ITEM_POSITION)) {
                position = data.getIntExtra(Intent_Constants.INTENT_ITEM_POSITION, -1);
                arrayList.remove(position);
                arrayTextPro.remove(position);
                if (arrayList.size() == 0) {
                    mEmptyStateTextView.setText("No TODO List");
                    mEmptyStateTextView.setVisibility(View.VISIBLE);
                } else removeFocusSetting(lastFocusView, lastFocusPosition);
            }
            arrayAdapter.notifyDataSetChanged();
        } else {
            if (requestCode == Intent_Constants.INTENT_REQUEST_CODE) {
                messageString = data.getStringExtra(Intent_Constants.INTENT_MESSAGE_FIELD);
                boolean bold = data.getBooleanExtra(Intent_Constants.INTENT_ITEM_BOLD, false);
                boolean italic = data.getBooleanExtra(Intent_Constants.INTENT_ITEM_ITALIC, false);
                int textSize = data.getIntExtra(Intent_Constants.INTENT_ITEM_TEXTSIZE, -1);
                arrayList.add(mListView.getFirstVisiblePosition(), messageString);
                arrayTextPro.add(0, bold + " " + italic + " " + textSize);
                arrayAdapter.notifyDataSetChanged();

            } else if (requestCode == Intent_Constants.INTENT_REQUEST_CODE_TWO) {
                messageString = data.getStringExtra(Intent_Constants.INTENT_CHANGED_MESSAGE);
                position = data.getIntExtra(Intent_Constants.INTENT_ITEM_POSITION, -1);
                boolean bold = data.getBooleanExtra(Intent_Constants.INTENT_ITEM_BOLD, false);
                boolean italic = data.getBooleanExtra(Intent_Constants.INTENT_ITEM_ITALIC, false);
                int textSize = data.getIntExtra(Intent_Constants.INTENT_ITEM_TEXTSIZE, -1);
                arrayList.remove(position);
                arrayList.add(position, messageString);
                arrayTextPro.remove(position);
                arrayTextPro.add(position, bold + " " + italic + " " + textSize);
                removeFocusSetting(lastFocusView, lastFocusPosition);
                arrayAdapter.notifyDataSetChanged();
            }
        }
    }

    public View getViewByPosition(int pos) {
        final int firstListItemPosition = mListView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + mListView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return mListView.getAdapter().getView(pos, null, mListView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return mListView.getChildAt(childIndex);
        }
    }

    String encodeData(String s, int i) {
        String[] a = arrayTextPro.get(i).split(" ");
        DecimalFormat formatter = new DecimalFormat("00");
        String textS = formatter.format(Integer.parseInt(a[2]));

        return (s.replaceAll("[\\n]", "$nॉलॉलॉलn$")) + a[0].charAt(0) + " " + a[1].charAt(0) + " " + textS;
    }

    String decodeData(String s) {
        String[] a = s.substring(s.length() - 6).split(" ");
        if (a[0].equalsIgnoreCase("t")) a[0] = "true";
        else if (a[0].equalsIgnoreCase("f")) a[0] = "false";
        if (a[1].equalsIgnoreCase("f")) a[1] = "false";
        else if (a[1].equalsIgnoreCase("t")) a[1] = "true";

        arrayTextPro.add(a[0] + " " + a[1] + " " + a[2]);
        s = s.substring(0, s.length() - 6);
        return s.replaceAll("nॉलॉलॉलn", "\n");
    }

    void savedData() {
        try {
            PrintWriter printWriter = new PrintWriter(openFileOutput("TodoTask.txt", Context.MODE_PRIVATE));
            for (int i = 0; i < arrayList.size(); i++) {
                String data = encodeData(arrayList.get(i).toString(), i);
                printWriter.println(data);
            }
            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        savedData();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        savedData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        savedData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (arrayList.size() <= 0) mEmptyStateTextView.setText("No TODO List");
        else mEmptyStateTextView.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (arrayList.size() <= 0) mEmptyStateTextView.setText("No TODO List");
        else mEmptyStateTextView.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_delete_all) {
            arrayList.clear();
            arrayAdapter.notifyDataSetChanged();
            mEmptyStateTextView.setText("No TODO List");
            mEmptyStateTextView.setVisibility(View.VISIBLE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void doEdit(View view) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, EditMessageClass.class);
        intent.putExtra(Intent_Constants.INTENT_MESSAGE_DATA, arrayList.get(lastFocusPosition).toString());
        intent.putExtra(Intent_Constants.INTENT_ITEM_POSITION, lastFocusPosition);
        intent.putExtra(Intent_Constants.INTENT_ITEM_TEXT_PROPERTIES, arrayTextPro.get(lastFocusPosition));
        startActivityForResult(intent, Intent_Constants.INTENT_REQUEST_CODE_TWO);
    }

    public void doDelete(View view) {
        arrayAdapter.remove(arrayList.get(lastFocusPosition));
        arrayTextPro.remove(lastFocusPosition);
        if (arrayList.size() == 0) {
            mEmptyStateTextView.setText("No TODO List");
            mEmptyStateTextView.setVisibility(View.VISIBLE);
        } else removeFocusSetting(lastFocusView, lastFocusPosition);
    }
}
