package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import com.blankj.swipepanel.SwipePanel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.PicassoProvider;
import com.vstechlab.easyfonts.EasyFonts;

import java.io.IOException;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Leif(Yuxiang Yao)
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView=findViewById(R.id.swipeInstrcution);
        textView.setText("Want to know more functions?\n" +
                "SWIPE from LEFT to RIGHT!");

        TextView quoteTextView=findViewById(R.id.quotes);
        String[] quotes={
                "You can, you should, and if you're brave enough to start, you will.",
                "Everything you want to be, you already are. You're simply on the path to discovering it.",
                "The death of a dream is the day that you stop believing in the work it takes to get there.",
                "One who knows all the answers has not been asked all the questions.",
                "Powerful avalanches begin with small shifts.",
                "If you don't like something, change it. If you can't change it, change your attitude. Don't complain.",
                "A wet person does not fear the rain.",
                "Talent is a pursued interest. Anything that you're willing to practice, you can do.",
                "Don't let someone who gave up on their dreams talk you out of going after yours.It is not uncommon for people to spend their whole life waiting to start living.",
                "The past has no power over the present moment.",
                "Stop being afraid of what could go wrong and start being positive about what could go right.",
                "When we get too caught up in the busyness of the world, we lose connection with one another—and ourselves.",
                "Look for the good in every person and every situation. You'll almost always find it.",
                "Waste no more time arguing about what a good person should be. Be one.",
                "Winning starts with beginning.",
                "What makes you different or weird—that's your strength.",
                "Forgiveness is a gift you give yourself.",
                "Have patience. All things are difficult before they become easy.",
                "The thing that is really hard, and really amazing, is giving up on being perfect and beginning the work of becoming yourself.",
                "I've learned that you shouldn't go through life with a catcher's mitt on both hands; you need to be able to throw something back.",
                "Happiness is when what you think, what you say, and what you do are in harmony.",
                "Never dull your shine for somebody else."
        };

        Random random=new Random();
        quoteTextView.setTypeface(EasyFonts.caviarDreams(this));
        quoteTextView.setText(quotes[random.nextInt(quotes.length)]);


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
                        showInternetErrorConnection(MainActivity.this);
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

    private void showInternetErrorConnection(final Context context){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Error");
                builder.setMessage("There are some problems in Internet Connection. Please check it and query the word again!");
                builder.show();
            }
        });

    }
}
