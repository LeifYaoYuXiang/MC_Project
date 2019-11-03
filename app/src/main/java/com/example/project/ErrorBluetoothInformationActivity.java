package com.example.project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ErrorBluetoothInformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_bluetooth_information);
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(ErrorBluetoothInformationActivity.this,FunctionListActivity.class);
        startActivity(intent);
    }
}
