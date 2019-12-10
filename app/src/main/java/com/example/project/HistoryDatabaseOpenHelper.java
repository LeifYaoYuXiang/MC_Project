package com.example.project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Leif(Yuxiang Yao)
 */
public class HistoryDatabaseOpenHelper extends SQLiteOpenHelper {
    public static final String CREATE_RECORD = "create table Record (" +
            "id integer primary key autoincrement, " +
            "correct integer, " +
            "incorrect integer, " +
            "date long )";
    private Context context;

    public HistoryDatabaseOpenHelper(Context context,String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_RECORD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
