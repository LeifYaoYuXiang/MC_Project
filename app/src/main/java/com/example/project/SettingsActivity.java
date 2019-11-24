package com.example.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.ldoublem.loadingviewlib.LVChromeLogo;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {
    private Preference[] preferences={
            new Preference("Juicy Orange(Default)",R.drawable.orange),
            new Preference("summer sea",R.drawable.sea),
            new Preference("Cute Rose",R.drawable.rose),
            new Preference("Silent Movie",R.drawable.movie),

    };
    private List<Preference> preferenceList=new ArrayList<>();
    private PreferenceAdapter preferenceAdapter;
    public int yourChoice;

    private int mThemeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences=getSharedPreferences("themePre",MODE_PRIVATE);
        int themeID=sharedPreferences.getInt("themeID",-1);
        setTheme(themeID);

        setContentView(R.layout.activity_settings);
        LVChromeLogo chromeLogo=findViewById(R.id.animation_chrome);

        chromeLogo.startAnim();
        initPreference();

        final RecyclerView preferenceRecycle=findViewById(R.id.preference_recycle);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2);
        preferenceRecycle.setLayoutManager(gridLayoutManager);
        preferenceAdapter=new PreferenceAdapter(preferenceList);
        preferenceRecycle.setAdapter(preferenceAdapter);
        preferenceAdapter.setItemClickListener(new onRecycleViewClickListener() {
            @Override
            public void onItemClickListener(View view) {
                int position=preferenceRecycle.getChildAdapterPosition(view);
                if(position==0){
                    onTheme(R.style.AppTheme);
                    SharedPreferences.Editor editor=getSharedPreferences("themePre",MODE_PRIVATE).edit();
                    editor.putInt("themeID",mThemeId);
                    editor.apply();
                }
                else if(position==1){
                    onTheme(R.style.BlueTheme);
                    SharedPreferences.Editor editor=getSharedPreferences("themePre",MODE_PRIVATE).edit();
                    editor.putInt("themeID",mThemeId);
                    editor.apply();
                }
                else if(position==2){
                    onTheme(R.style.PinkTheme);
                    SharedPreferences.Editor editor=getSharedPreferences("themePre",MODE_PRIVATE).edit();
                    editor.putInt("themeID",mThemeId);
                    editor.apply();
                }
                else if(position==3){
                    onTheme(R.style.GrayTheme);
                    SharedPreferences.Editor editor=getSharedPreferences("themePre",MODE_PRIVATE).edit();
                    editor.putInt("themeID",mThemeId);
                    editor.apply();
                }
            }

            @Override
            public void onItemLongClickListener(View view) {

            }
        });

    }


    private void initPreference(){
        this.preferenceList.clear();
        for(int i=0;i<preferences.length;i++){
            preferenceList.add(preferences[i]);
        }
    }


    private void onTheme(int iThemeId){
        mThemeId = iThemeId;
        this.recreate();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("theme", mThemeId);
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(SettingsActivity.this,MainActivity.class);
        startActivity(intent);
    }

}
