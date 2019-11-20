package com.example.project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
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
                Toast.makeText(SettingsActivity.this, position+"", Toast.LENGTH_SHORT).show();
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
}
