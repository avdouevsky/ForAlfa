package com.mshvdvskgmail.foralfa.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.mshvdvskgmail.foralfa.Adapters.NewsListAdapter;
import com.mshvdvskgmail.foralfa.R;
import com.prof.rssparser.Article;
import com.prof.rssparser.Parser;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    RecyclerView rvNewsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvNewsList = (RecyclerView) findViewById(R.id.activity_main_recycler);
        rvNewsList.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        rvNewsList.setLayoutManager(lm);

    }

    @Override
    protected void onResume() {
        super.onResume();

        String urlString = "https://news.rambler.ru/rss/head/";
        Parser parser = new Parser();
        parser.execute(urlString);
        parser.onFinish(new Parser.OnTaskCompleted() {

            @Override
            public void onTaskCompleted(ArrayList<Article> list) {
                //what to do when the parsing is done
                //the Array List contains all article's data. For example you can use it for your adapter.
                for (Article artcl : list){
                    Log.d("wow", artcl.toString());
                }
//                NewsListAdapter nlAdaper = new NewsListAdapter(list);
                rvNewsList.setAdapter(new NewsListAdapter(list));
            }

            @Override
            public void onError() {
                //what to do in case of error
            }
        });



    }
}
