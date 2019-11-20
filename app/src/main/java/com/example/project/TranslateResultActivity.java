package com.example.project;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ldoublem.loadingviewlib.view.LVCircularJump;
import com.vstechlab.easyfonts.EasyFonts;

import org.json.JSONObject;

import java.util.ArrayList;


public class TranslateResultActivity extends AppCompatActivity {
    public SQLiteDatabase sqLiteDatabase;
    private String English;
    private TextView englishWord;
    private TextView basicTranslation;
    private TextView pronunciation;
    private TextView detailTranslation;
    private ImageView tick;
    private Button remember;
    private Button forget;
    private LVCircularJump lvCircularJump;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate_result);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        String res=bundle.getString("translation");
        English=bundle.getString("English");

        initUI();
        WordListOpenHelper wordListOpenHelper=new WordListOpenHelper(this,"Word.db",null,1);
        sqLiteDatabase=wordListOpenHelper.getWritableDatabase();

        final String[] translation=this.parseJSON(res);
        String test="[\""+English+"\"]";
        lvCircularJump.setViewColor(Color.parseColor("#FF5722"));
        lvCircularJump.startAnim();
        remember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TranslateResultActivity.this, "Congratulations!", Toast.LENGTH_SHORT).show();
            }
        });

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues contentValues=new ContentValues();
                contentValues.put("English",English);
                contentValues.put("Pronunciation",translation[2]);
                contentValues.put("ChineseBasic",translation[0]);
                contentValues.put("ChineseDetail",translation[1]);
                sqLiteDatabase.insert("Word",null,contentValues);
                Toast.makeText(TranslateResultActivity.this, "Store this word!", Toast.LENGTH_SHORT).show();
                tick.setImageResource(R.drawable.tick);
                forget.setClickable(false);
            }
        });

        if(test.equals(translation[0])){
            englishWord.setText(English);
            remember.setClickable(false);
            forget.setClickable(false);
            basicTranslation.setText("Your Spelling is Wrong!");
        }else{
            if(savedAlready()){
                tick.setImageResource(R.drawable.tick);
                forget.setClickable(false);
            }
            englishWord.setText(English);
            basicTranslation.setText(translation[0]);
            basicTranslation.setTypeface(EasyFonts.captureIt(this));
            pronunciation.setText(translation[2]);
            basicTranslation.setTypeface(EasyFonts.robotoRegular(this));
            detailTranslation.setText(translation[1]);
        }

    }

    private String[] parseJSON(String jString){
        String basicTranslation=null;
        String detailTranslation=null;
        String phonetic=null;

        try{
            JSONObject jsonObject=new JSONObject(jString);
            basicTranslation=jsonObject.getString("translation");
            JSONObject jsonObject1=jsonObject.getJSONObject("basic");
            phonetic=jsonObject1.getString("phonetic");
            detailTranslation=jsonObject1.getString("explains");
            if(!basicTranslation.equals(null)){
                CharSequence charSequence=basicTranslation.replace("[","");
                charSequence=((String) charSequence).replace("]","");
                charSequence=((String) charSequence).replace("\"","");
                basicTranslation=charSequence.toString();
            }
            if(!detailTranslation.equals(null)){
                CharSequence charSequence=detailTranslation.replace("[","");
                charSequence=((String) charSequence).replace("]","");
                charSequence=((String) charSequence).replace("\"","");
                detailTranslation=charSequence.toString();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return new String[]{basicTranslation,detailTranslation,phonetic};
    }



    private void initUI(){
        englishWord=findViewById(R.id.englishWord);
        basicTranslation=findViewById(R.id.basic_translation);
        pronunciation=findViewById(R.id.pronunciation);
        detailTranslation=findViewById(R.id.detail_translation);

        tick=findViewById(R.id.save_already);

        remember=findViewById(R.id.rememberIt);
        forget=findViewById(R.id.putIntoList);
        lvCircularJump=findViewById(R.id.animation_jump);

    }

    private boolean savedAlready(){
        WordListOpenHelper wordListOpenHelper=new WordListOpenHelper(this,"Word.db",null,1);
        sqLiteDatabase=wordListOpenHelper.getWritableDatabase();
        ArrayList wordList=new ArrayList<>();
        Cursor cursor=sqLiteDatabase.query("Word",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do {
                String tempEnglish=cursor.getString(cursor.getColumnIndex("English"));
                if(tempEnglish.equals(English)){
                    return true;
                }
            }while(cursor.moveToNext());
        }
        cursor.close();
        return false;
    }
}
