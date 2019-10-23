package com.example.project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HistoryTestOpenHelper extends SQLiteOpenHelper {
    public static final String CREATE_RECORD = "create table Historytest (" +
            "id integer primary key autoincrement, " +
            "remember integer, " +
            "forget integer, " +
            "date long)" ;
    private Context context;

    public HistoryTestOpenHelper(Context context,String name,SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_RECORD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
