package com.example.project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
            new Functions("Settings",R.drawable.settings)
    };

    private List<Functions> functionsList=new ArrayList<>();
    private FunctionsAdapter functionsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                if(position==1){
                    Intent intent=new Intent(FunctionListActivity.this,WordListActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onItemLongClickListener(View view) {

            }
        });
    }

    private void initFunctions() {
        functionsList.clear();
        for(int i=0;i<functions.length;i++){
            functionsList.add(functions[i]);
        }
    }
}
