package com.example.project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;


import com.blankj.swipepanel.SwipePanel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.PicassoProvider;

import java.io.IOException;
import java.util.Random;

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

        ImageView imageView=findViewById(R.id.beautiful_image);
        String[] urls={
                "https://cdn.pixabay.com/photo/2019/10/17/14/52/white-4557097_1280.jpg",
                "https://cdn.pixabay.com/photo/2019/09/14/12/41/iceland-4475932_1280.jpg",
                "https://cdn.pixabay.com/photo/2019/10/17/09/18/robot-in-space-4556429__340.png",
                "https://cdn.pixabay.com/photo/2019/11/08/16/06/landscape-4611763__340.jpg",
                "https://cdn.pixabay.com/photo/2019/11/05/11/58/oldtimer-4603347__340.jpg",
                "https://cdn.pixabay.com/photo/2014/09/14/18/21/clover-445255__340.jpg",
                "https://cdn.pixabay.com/photo/2015/09/10/07/26/lighthouse-934175__340.jpg",
                "https://cdn.pixabay.com/photo/2017/08/30/02/28/castle-2695680__340.jpg",
                "https://cdn.pixabay.com/photo/2013/11/02/09/02/killarney-204401__340.jpg",
                "https://cdn.pixabay.com/photo/2016/11/22/06/05/girl-1848454__340.jpg",
                "https://cdn.pixabay.com/photo/2018/08/31/08/35/toys-3644073__340.png",
                "https://cdn.pixabay.com/photo/2017/01/11/08/31/icon-1971128__340.png",
                "https://cdn.pixabay.com/photo/2019/03/30/20/27/camera-4091991__340.png",
                "https://cdn.pixabay.com/photo/2019/11/09/19/39/ocean-4614307__340.jpg",
                "https://cdn.pixabay.com/photo/2019/11/11/21/33/rose-4619562__340.jpg",
                "https://cdn.pixabay.com/photo/2019/11/07/13/05/waffle-4608843__340.jpg",
                "https://cdn.pixabay.com/photo/2019/11/07/21/02/dachshund-4609905__340.jpg",
                "https://cdn.pixabay.com/photo/2019/11/03/20/11/portrait-4599559__340.jpg"
        };

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
        String url=urls[random.nextInt(urls.length)];

        Picasso.Builder builder=new Picasso.Builder(this);
        Picasso picasso=builder.build();
        picasso.load(url).placeholder(R.drawable.loading).into(imageView);

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
