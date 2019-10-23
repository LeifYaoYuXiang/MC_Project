package com.example.project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WordListOpenHelper extends SQLiteOpenHelper {
    public static final String CREATE_WORD = "create table Word (" +
            "id integer primary key autoincrement, " +
            "English String, " +
            "Pronunciation String,"+
            "ChineseBasic String,"
            +"ChineseDetail String)";

    private Context context;
    public WordListOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_WORD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
