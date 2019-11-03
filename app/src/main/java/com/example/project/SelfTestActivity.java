package com.example.project;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    private Button rememberLater;
    private Button forgetReally;

    private TextView pronunciationTextView;
    private TextView wordTextView;

    private RelativeLayout testLayout;
    private RelativeLayout answerLayout;

    private boolean hasWords=true;



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
        rememberLater=findViewById(R.id.i_remember_this_word);
        forgetReally=findViewById(R.id.jump_to_next_word);

        pronunciationTextView=findViewById(R.id.self_test_pronunciation);
        wordTextView=findViewById(R.id.self_test_english);

        testLayout=findViewById(R.id.choose_know_unknown);
        answerLayout=findViewById(R.id.self_test_translation);

        knowThisWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpToNextWord();
                totalNumberWordsRemember++;
            }
        });

        unKnownThisWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showExplanations();
                testLayout.setVisibility(View.GONE);
                answerLayout.setVisibility(View.VISIBLE);
            }
        });

        rememberLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpToNextWord();
                answerLayout.setVisibility(View.GONE);
                testLayout.setVisibility(View.VISIBLE);
                totalNumberWordsRemember++;

            }
        });

        forgetReally.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpToNextWord();
                answerLayout.setVisibility(View.GONE);
                testLayout.setVisibility(View.VISIBLE);
                totalNumberWordsForget++;
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

    private void jumpToNextWord(){
        index++;
        if(index<testWordsList.size()){
            Word temp=testWordsList.get(index);
            String tempPronunciation=temp.getPronunciation();
            String tempEnglish=temp.getEnglish();
            pronunciationTextView.setText(tempPronunciation);
            wordTextView.setText(tempEnglish);
        }else{
            Toast.makeText(SelfTestActivity.this, "Reach the end", Toast.LENGTH_SHORT).show();

        }
    }

    private void showExplanations(){
        Word temp=testWordsList.get(index);
        String English=temp.getEnglish();
        String Chinese=temp.getChinese();
        String ChineseDetail=temp.getChineseDetail();
        wordTextView.setText(English+"\n"+Chinese+"\n"+ChineseDetail);
    }



    @Override
    public void onBackPressed() {
       super.onBackPressed();
        Calendar calendar=Calendar.getInstance();


    }

    @Override
    protected void onPause() {
        Toast.makeText(this, totalNumberWordsForget+"---"+totalNumberWordsRemember, Toast.LENGTH_SHORT).show();
        super.onPause();
    }


}
