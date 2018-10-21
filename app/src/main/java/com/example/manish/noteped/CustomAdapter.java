package com.example.manish.noteped;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<String> {

    public CustomAdapter(Context context, ArrayList<String> words) {
        super(context, 0, words);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }


        String currentWord = getItem(position);
        TextView t = listItemView.findViewById(R.id.textView);
        t.setText(currentWord);
        ImageView i = listItemView.findViewById(R.id.delete);
        ImageView i2 = listItemView.findViewById(R.id.edit);
        i.setVisibility(View.GONE);
        i2.setVisibility(View.GONE);
        listItemView.setBackgroundResource(R.drawable.shape);
        return listItemView;
    }
}