package com.example.project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class TranslateResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate_result);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        String res=bundle.getString("translation");

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(res);
            Toast.makeText(this, ""+jsonObject.toString(), Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //3.转为java对象


    }
}
