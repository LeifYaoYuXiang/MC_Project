package com.example.project;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ldoublem.loadingviewlib.view.LVCircularJump;
import org.json.JSONObject;

public class TranslateResultActivity extends AppCompatActivity {
    public SQLiteDatabase sqLiteDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate_result);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        String res=bundle.getString("translation");
        final String English=bundle.getString("English");

        WordListOpenHelper wordListOpenHelper=new WordListOpenHelper(this,"Word.db",null,1);
        sqLiteDatabase=wordListOpenHelper.getWritableDatabase();

        final String[] translation=this.parseJSON(res);
        TextView englishWord=findViewById(R.id.englishWord);
        englishWord.setText(English);

        TextView basicTranslation=findViewById(R.id.basic_translation);
        basicTranslation.setText(translation[0]);

        final TextView pronunciation=findViewById(R.id.pronunciation);
        pronunciation.setText(translation[2]);

        TextView detailTranslation=findViewById(R.id.detail_translation);
        detailTranslation.setText(translation[1]);

        LVCircularJump lvCircularJump=findViewById(R.id.animation_jump);
        lvCircularJump.setViewColor(Color.parseColor("#FF5722"));
        lvCircularJump.startAnim();

        Button remember=findViewById(R.id.rememberIt);
        Button forget=findViewById(R.id.putIntoList);

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
            }
        });

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
}
