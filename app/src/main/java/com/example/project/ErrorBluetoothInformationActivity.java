package com.example.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * @author Leif(Yuxiang Yao)
 */
public class ErrorBluetoothInformationActivity extends AppCompatActivity {
    private int themeID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("themePre", MODE_PRIVATE);
        themeID = sharedPreferences.getInt("themeID", -1);
        setTheme(themeID);

        setContentView(R.layout.activity_error_bluetooth_information);
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(ErrorBluetoothInformationActivity.this,FunctionListActivity.class);
        startActivity(intent);
    }
}
