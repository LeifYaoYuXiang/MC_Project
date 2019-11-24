package com.example.project;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.ArrayList;;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class WidgetProvider extends AppWidgetProvider {

    private final String ACTION_UPDATE_ALL = "UPDATE_ALL";
    private static Set idsSet = new HashSet();
    public static int index;
    private List<Word> wordList=new ArrayList<>();

    public WidgetProvider() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        final String action = intent.getAction();

        if (ACTION_UPDATE_ALL.equals(action)) {
            updateAllAppWidgets(context, AppWidgetManager.getInstance(context), idsSet);
        } else if (intent.hasCategory(Intent.CATEGORY_ALTERNATIVE)) {
            index++;
            updateAllAppWidgets(context, AppWidgetManager.getInstance(context), idsSet);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int appWidgetId : appWidgetIds) {
            idsSet.add(Integer.valueOf(appWidgetId));
        }
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            idsSet.remove(Integer.valueOf(appWidgetId));
        }
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        Intent intent = new Intent(context, WidgetService.class);
        context.startService(intent);
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        Intent intent = new Intent(context, WidgetService.class);
        context.stopService(intent);
        super.onDisabled(context);
    }

    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);
    }

    private void updateAllAppWidgets(Context context, AppWidgetManager appWidgetManager, Set set) {
        int appID;
        Iterator it = set.iterator();
        while (it.hasNext()) {
            appID = ((Integer) it.next()).intValue();
            RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget);

            updateWordList(context);
            remoteView.setTextViewText(R.id.widget_English,wordList.get(index).getEnglish());
            remoteView.setTextViewText(R.id.widget_Chinese,wordList.get(index).getChinese());
            appWidgetManager.updateAppWidget(appID, remoteView);
            index++;
            if(index>=wordList.size()){
                index=0;
            }
        }
    }


    private void updateWordList(Context context){
        this.wordList=new ArrayList<>();
        WordListOpenHelper wordListOpenHelper=new WordListOpenHelper(context,"Word.db",null,1);
        SQLiteDatabase sqLiteDatabase=wordListOpenHelper.getWritableDatabase();
        Cursor cursor=sqLiteDatabase.query("Word",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do {
                String English=cursor.getString(cursor.getColumnIndex("English"));
                String Chinese=cursor.getString(cursor.getColumnIndex("ChineseBasic"));
                int id=cursor.getInt(cursor.getColumnIndex("id"));
                Word word=new Word(id,English,Chinese);
                wordList.add(word);
            }while(cursor.moveToNext());
        }
        cursor.close();
    }



}
