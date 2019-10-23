package com.example.project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.TextView;


import com.blankj.swipepanel.SwipePanel;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView=findViewById(R.id.swipeInstrcution);
        textView.setText("Want to know more functions?\n" +
                "SWIPE from LEFT to RIGHT!");

        final SwipePanel swipePanel=findViewById(R.id.swipePanel);
        swipePanel.setOnFullSwipeListener(new SwipePanel.OnFullSwipeListener() {
            @Override
            public void onFullSwipe(int direction) {
                Intent intent=new Intent(MainActivity.this,FunctionListActivity.class);
                swipePanel.close(direction);
                startActivity(intent);
            }
        });
        final SearchView searchView=findViewById(R.id.searchWord);
        searchView.setSubmitButtonEnabled(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String s) {
                String url = "http://fanyi.youdao.com/openapi.do?keyfrom=lewe518&key=70654389&type=data&doctype=json&version=1.1&q="+s;
                OkHttpClient okHttpClient = new OkHttpClient();
                final Request request = new Request.Builder()
                        .url(url)
                        .get()
                        .build();
                Call call=okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("TAG","Failure");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String networkResponse = response.body().string();
                        Intent intent=new Intent(MainActivity.this,TranslateResultActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putString("translation",networkResponse);
                        bundle.putString("English",s);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });


    }
}
