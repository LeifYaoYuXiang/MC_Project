package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class WordAdapter extends ArrayAdapter<Word> {
    private int resourceId;

    public WordAdapter(Context context, int resource,List<Word> objects) {
        super(context, resource, objects);
        this.resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Word word=getItem(position);
        View view =  LayoutInflater.from(getContext()).inflate(resourceId, parent,false);

        TextView textView = view.findViewById(R.id.EnglishExpression);
        textView.setText(word.getEnglish());
        TextView textView1=view.findViewById(R.id.ChineseExpression);
        textView1.setText(word.getChinese());

        return view;
    }
}
