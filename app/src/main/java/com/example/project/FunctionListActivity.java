package com.example.project;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FunctionListActivity extends AppCompatActivity {
    private Functions[] functions={
            new Functions("Personal Information", R.drawable.personal_information),
            new Functions("Word List",R.drawable.word_list),
            new Functions("Self Test",R.drawable.exam),
            new Functions("History",R.drawable.history),
            new Functions("PK Mode",R.drawable.pk),
            new Functions("Preferences",R.drawable.settings)
    };

    private List<Functions> functionsList=new ArrayList<>();
    private FunctionsAdapter functionsAdapter;
    public int yourChoice=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences=getSharedPreferences("themePre",MODE_PRIVATE);
        int themeID=sharedPreferences.getInt("themeID",-1);
        setTheme(themeID);
    }

    @Override
    protected void onResume() {
        SharedPreferences sharedPreferences=getSharedPreferences("themePre",MODE_PRIVATE);
        int themeID=sharedPreferences.getInt("themeID",-1);
        Log.d("TAG",themeID+"");
        setTheme(themeID);

        setContentView(R.layout.activity_function_list);
        initFunctions();
        final RecyclerView mainRecylcle=findViewById(R.id.function_recycle);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2);
        mainRecylcle.setLayoutManager(gridLayoutManager);
        functionsAdapter=new FunctionsAdapter(functionsList);
        mainRecylcle.setAdapter(functionsAdapter);

        functionsAdapter.setItemClickListener(new onRecycleViewClickListener() {
            @Override
            public void onItemClickListener(View view) {
                int position=mainRecylcle.getChildAdapterPosition(view);
                if(position==0){
                    SharedPreferences sharedPreferences=getSharedPreferences("PersonalInformation",MODE_PRIVATE);
                    String name=sharedPreferences.getString("userName","");
                    if("".equals(name)){
                        dialogPersonalInformation();
                    }else{
                        Intent intent=new Intent(FunctionListActivity.this,PersonalInformationActivity.class);
                        startActivity(intent);
                    }
                }
                if(position==1){
                    Intent intent=new Intent(FunctionListActivity.this,WordListActivity.class);
                    startActivity(intent);
                }
                if(position==2){
                    Intent intent=new Intent(FunctionListActivity.this,SelfTestActivity.class);
                    startActivity(intent);
                }
                if(position==3){
                    Intent intent=new Intent(FunctionListActivity.this,HistoryActivity.class);
                    startActivity(intent);
                }
                if(position==4){
                   showInformation();
                }
                if(position==5){
                    Intent intent=new Intent(FunctionListActivity.this, SettingsActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onItemLongClickListener(View view) {

            }
        });

        super.onResume();
    }

    private void initFunctions() {
        functionsList.clear();
        for(int i=0;i<functions.length;i++){
            functionsList.add(functions[i]);
        }
    }

    private void showChosen(){
        final String[] items = { "I want to ask","I want to answer"};
        final AlertDialog.Builder singleChoiceDialog = new AlertDialog.Builder(FunctionListActivity.this);
        singleChoiceDialog.setTitle("What do you want to do?");
        singleChoiceDialog.setSingleChoiceItems(items, -1,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        yourChoice = which;
                    }
                });
        singleChoiceDialog.setPositiveButton("Sure",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (yourChoice != -1) {
                            Intent intent=new Intent(FunctionListActivity.this,PKActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putInt("choose",yourChoice);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }else{

                        }
                    }
                });
        singleChoiceDialog.show();
    }

    private void showInformation(){
        final AlertDialog.Builder builder=new AlertDialog.Builder(FunctionListActivity.this);
        builder.setTitle("INFORM");
        builder.setMessage("Before you start to use PK Mode, please ensure that \n 1.You open Bluetooth\n 2.You connect to your partner and prepare to communicate");
        builder.setPositiveButton("YES, I ENSURE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showChosen();
            }
        });
        builder.setNegativeButton("NO, I NEED MORE TIME", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(FunctionListActivity.this,MainActivity.class);
        startActivity(intent);
    }


    private void dialogPersonalInformation(){
        AlertDialog.Builder builder=new AlertDialog.Builder(FunctionListActivity.this);
        builder.setTitle("You have not logged into your account");
        builder.setMessage("You may need to log into one existed account OR register for new one");
        builder.setPositiveButton("Log Into One Existed Account", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent=new Intent(FunctionListActivity.this,LogIntoActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Register One New Account", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent=new Intent(FunctionListActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        builder.show();
    }

}
