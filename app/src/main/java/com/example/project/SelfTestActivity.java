package com.example.project;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ldoublem.loadingviewlib.view.LVPlayBall;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class SelfTestActivity extends AppCompatActivity {
    private List<Word> testWordsList=new ArrayList<Word>();
    private WordListOpenHelper wordListOpenHelper;
    public int index=0;
    private int totalNumberWordsRemember=0;
    private int totalNumberWordsForget=0;

    private Button knowThisWord;
    private Button unKnownThisWord;

    private TextView pronunciationTextView;
    private TextView wordTextView;
    private TextView definitionTextView;
    private LVPlayBall lvPlayBall;

    private RelativeLayout testLayout;
    private RelativeLayout answerLayout;

    private boolean hasWords=true;
    private boolean showDefinition =false;
    int themeID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wordListOpenHelper=new WordListOpenHelper(this,"Word.db",null,1);

        initWordList();
        if(testWordsList.size()==0){
            hasWords=false;
        }else{
            Collections.shuffle(testWordsList);
        }
        SharedPreferences preferences= getSharedPreferences("themePre", MODE_PRIVATE);
        themeID = preferences.getInt("themeID", -1);
        setTheme(themeID);

        setContentView(R.layout.activity_self_test);
        initUI();
        initTest();

    }

    private void initWordList(){
        String ChineseBasic;
        String ChineseDetail;
        String Pronunciation;
        String English;
        SQLiteDatabase sqLiteDatabase = wordListOpenHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query("Word",new String[]{"ChineseBasic","ChineseDetail","Pronunciation","English"}, null,null, null, null,null);
        if (cursor.moveToFirst()) {
            do {
                ChineseBasic = cursor.getString(0);
                ChineseDetail=cursor.getString(1);
                Pronunciation=cursor.getString(2);
                English=cursor.getString(3);
                Word temp=new Word(English,Pronunciation,ChineseBasic,ChineseDetail);
                testWordsList.add(temp);
            }
            while(cursor.moveToNext());
        }
        cursor.close();
    }

    private void initUI(){
        knowThisWord=findViewById(R.id.know_this_word);
        unKnownThisWord=findViewById(R.id.unknown_this_word);

        lvPlayBall=findViewById(R.id.animation_play_ball);
        definitionTextView=findViewById(R.id.definition);

        lvPlayBall.setViewColor(Color.parseColor("#00BCD4"));
        lvPlayBall.setBallColor(Color.parseColor("#FFA000"));
        lvPlayBall.startAnim();


        pronunciationTextView=findViewById(R.id.self_test_pronunciation);
        wordTextView=findViewById(R.id.self_test_english);

        if(themeID==R.style.AppTheme){

        }else if(themeID==R.style.PinkTheme){
            knowThisWord.setBackground(SelfTestActivity.this.getResources().getDrawable(R.drawable.button_pink));
            unKnownThisWord.setBackground(SelfTestActivity.this.getResources().getDrawable(R.drawable.button_pink));
        }else if(themeID==R.style.BlueTheme){
            knowThisWord.setBackground(SelfTestActivity.this.getResources().getDrawable(R.drawable.button_blue));
            unKnownThisWord.setBackground(SelfTestActivity.this.getResources().getDrawable(R.drawable.button_blue));
        }else if(themeID==R.style.GrayTheme){
            knowThisWord.setBackground(SelfTestActivity.this.getResources().getDrawable(R.drawable.button_grey));
            unKnownThisWord.setBackground(SelfTestActivity.this.getResources().getDrawable(R.drawable.button_grey));
        }

        knowThisWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!showDefinition){
                    showDefinition=false;
                    totalNumberWordsRemember++;
                    index++;
                    if(index<testWordsList.size()){
                        Word temp=testWordsList.get(index);
                        String tempPronunciation=temp.getPronunciation();
                        String tempEnglish=temp.getEnglish();
                        pronunciationTextView.setText(tempPronunciation);
                        wordTextView.setText(tempEnglish);
                    }else{
                        onBackPressed();
                    }
                }else{
                    knowThisWord.setText("I know this word");
                    unKnownThisWord.setText("I forget this word");
                    totalNumberWordsRemember++;
                    index++;
                    definitionTextView.setVisibility(View.GONE);
                    showDefinition=false;
                    if(index<testWordsList.size()){
                        Word temp=testWordsList.get(index);
                        String tempPronunciation=temp.getPronunciation();
                        String tempEnglish=temp.getEnglish();
                        pronunciationTextView.setText(tempPronunciation);
                        wordTextView.setText(tempEnglish);
                    }else{
                        onBackPressed();
                    }
                }
            }
        });

        unKnownThisWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!showDefinition){
                    if(index<testWordsList.size()){
                        knowThisWord.setText("Oh I remember it now");
                        unKnownThisWord.setText("I really forget");
                        showExplanations();
                        showDefinition=true;
                    }else{
                        onBackPressed();
                    }
                }else{
                    knowThisWord.setText("I know this word");
                    unKnownThisWord.setText("I forget this word");
                    totalNumberWordsForget++;
                    index++;
                    definitionTextView.setVisibility(View.GONE);
                    showDefinition=false;
                    if(index<testWordsList.size()){
                        Word temp=testWordsList.get(index);
                        String tempPronunciation=temp.getPronunciation();
                        String tempEnglish=temp.getEnglish();
                        pronunciationTextView.setText(tempPronunciation);
                        wordTextView.setText(tempEnglish);
                    }else{
                        onBackPressed();
                    }
                }
            }
        });

    }

    private void initTest(){
        if(hasWords){
            Word temp=testWordsList.get(0);
            String tempPronunciation=temp.getPronunciation();
            String tempEnglish=temp.getEnglish();
            pronunciationTextView.setText(tempPronunciation);
            wordTextView.setText(tempEnglish);
        }else{
            Toast.makeText(this, "There aren't any words", Toast.LENGTH_SHORT).show();
        }

    }


    private void showExplanations(){
        Word temp=testWordsList.get(index);
        String English=temp.getEnglish();
        String Chinese=temp.getChinese();
        String ChineseDetail=temp.getChineseDetail();
        definitionTextView.setText(Chinese+"\n"+ChineseDetail);
        definitionTextView.setVisibility(View.VISIBLE);
    }



    @Override
    public void onBackPressed() {
        if(totalNumberWordsForget==0&&totalNumberWordsRemember==0){
            super.onBackPressed();
        }else{
            HistoryDatabaseOpenHelper historyDatabaseOpenHelper=new HistoryDatabaseOpenHelper(this,"Records.db",null,1);
            SQLiteDatabase sqLiteDatabase=historyDatabaseOpenHelper.getWritableDatabase();
            ContentValues contentValues=new ContentValues();
            contentValues.put("correct",totalNumberWordsRemember);
            contentValues.put("incorrect",totalNumberWordsForget);
            contentValues.put("date",System.currentTimeMillis());
            sqLiteDatabase.insert("Record",null,contentValues);

            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("Feedback");
            builder.setMessage("In this Self Test, you answer "+(totalNumberWordsRemember+totalNumberWordsForget)+"words\n"+
                    "You remember "+totalNumberWordsRemember + " words\n"+
                    "You forget "+totalNumberWordsForget+ " words");
            builder.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent=new Intent(SelfTestActivity.this,FunctionListActivity.class);
                    startActivity(intent);
                }
            });
            builder.show();
        }
    }

}
