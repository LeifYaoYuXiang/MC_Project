package com.example.project;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Leif
 */
public class WordListActivity extends AppCompatActivity {
    private ListView TranslationsList;
    private WordListOpenHelper wordListOpenHelper;
    private List<Word> wordList=new ArrayList<>();
    private List<String> EnglishList=new ArrayList<>();
    private List<String> ChineseList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);

        TranslationsList=findViewById(R.id.translations_pairs);
        this.wordListOpenHelper=new WordListOpenHelper(this,"Word.db",null,1);
        SearchView searchView=findViewById(R.id.searchStoredWords);
        searchView.setSubmitButtonEnabled(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                SQLiteDatabase sqLiteDatabase=wordListOpenHelper.getWritableDatabase();
                wordList=new ArrayList<>();
                Cursor cursor=sqLiteDatabase.query("Word",null,null,null,null,null,null);
                if(cursor.moveToFirst()){
                    do {
                        String English=cursor.getString(cursor.getColumnIndex("English"));
                        String Chinese=cursor.getString(cursor.getColumnIndex("ChineseBasic"));
                        if(English.contains(s)||Chinese.contains(s)){
                            int id=cursor.getInt(cursor.getColumnIndex("id"));
                            Word word=new Word(id,English,Chinese);
                            wordList.add(word);
                        }
                    }while(cursor.moveToNext());
                }
                cursor.close();
                final WordAdapter noteAdapter=new WordAdapter(WordListActivity.this,R.layout.word_item,wordList);
                TranslationsList.setAdapter(noteAdapter);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                SQLiteDatabase sqLiteDatabase=wordListOpenHelper.getWritableDatabase();
                wordList=new ArrayList<>();
                Cursor cursor=sqLiteDatabase.query("Word",null,null,null,null,null,null);
                if(cursor.moveToFirst()){
                    do {
                        String English=cursor.getString(cursor.getColumnIndex("English"));
                        String Chinese=cursor.getString(cursor.getColumnIndex("ChineseBasic"));
                        if(English.contains(s)||Chinese.contains(s)){
                            int id=cursor.getInt(cursor.getColumnIndex("id"));
                            Word word=new Word(id,English,Chinese);
                            wordList.add(word);
                        }
                    }while(cursor.moveToNext());
                }
                cursor.close();
                final WordAdapter noteAdapter=new WordAdapter(WordListActivity.this,R.layout.word_item,wordList);
                TranslationsList.setAdapter(noteAdapter);
                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        this.wordList=new ArrayList<>();
        initDatabase();

        final WordAdapter noteAdapter=new WordAdapter(WordListActivity.this,R.layout.word_item,wordList);
        TranslationsList.setAdapter(noteAdapter);
        TranslationsList.setTextFilterEnabled(true);

        TranslationsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int i, long l) {
                Word temp=noteAdapter.getItem(i);
                int id=temp.getId();

                String English="";
                String ChineseBasic="";
                String ChineseDetail="";
                String Pronunciation="";
                SQLiteDatabase sqLiteDatabase = wordListOpenHelper.getWritableDatabase();
                String stringID = id+"";
                Cursor cursor = sqLiteDatabase.query("Word",new String[]{"ChineseBasic","ChineseDetail","Pronunciation","English"}, "id like ?",new String[]{stringID}, null, null,null);
                if (cursor.moveToFirst()) {
                    do {
                        ChineseBasic = cursor.getString(0);
                        ChineseDetail=cursor.getString(1);
                        Pronunciation=cursor.getString(2);
                        English=cursor.getString(3);
                    }
                    while(cursor.moveToNext());
                }
                cursor.close();
                AlertDialog.Builder builder=new AlertDialog.Builder(WordListActivity.this);
                builder.setTitle(English);
                builder.setMessage(Pronunciation+"\n"+ChineseBasic+"\n"+ChineseDetail+"\n");
                builder.show();

            }
        });

        TranslationsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view,final int i, long l) {
                AlertDialog.Builder aBuider=new AlertDialog.Builder(WordListActivity.this);
                aBuider.setTitle("Warning!");
                aBuider.setMessage("Attention! You are try to delete this word:");
                aBuider.setPositiveButton("Delete!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        Word temp=noteAdapter.getItem(i);

                        int id=temp.getId();

                        SQLiteDatabase sqLiteDatabase=wordListOpenHelper.getWritableDatabase();
                        sqLiteDatabase.delete("Word","id=?",new String[]{id+""});
                        Intent intent=new Intent(WordListActivity.this,WordListActivity.class);
                        startActivity(intent);

                    }
                });

                aBuider.setNegativeButton("Cancel Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                aBuider.show();
                return true;
            }
        });

    }


    private void initDatabase(){
        this.wordListOpenHelper=new WordListOpenHelper(this,"Word.db",null,1);
        SQLiteDatabase sqLiteDatabase=wordListOpenHelper.getWritableDatabase();
        Cursor cursor=sqLiteDatabase.query("Word",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do {
                int id=cursor.getInt(cursor.getColumnIndex("id"));
                String English=cursor.getString(cursor.getColumnIndex("English"));
                String Chinese=cursor.getString(cursor.getColumnIndex("ChineseBasic"));
                this.ChineseList.add(Chinese);
                this.EnglishList.add(English);
                Word word=new Word(id,English,Chinese);
                this.wordList.add(word);
            }while(cursor.moveToNext());
        }
        cursor.close();
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(WordListActivity.this,FunctionListActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.wordList=new ArrayList<>();
        initDatabase();
    }


}
