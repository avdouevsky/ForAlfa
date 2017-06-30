package com.mshvdvskgmail.foralfa.activities;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import com.mshvdvskgmail.foralfa.adapters.NewsListAdapter;
import com.mshvdvskgmail.foralfa.MyApplication;
import com.mshvdvskgmail.foralfa.R;
import com.mshvdvskgmail.foralfa.service.NotificationService;
import com.prof.rssparser.Article;
import com.prof.rssparser.Parser;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String urlString;
    private Parser parser;
    private RecyclerView rvNewsList;
    private NewsListAdapter nlAdaper;
    private JobScheduler jobScheduler;
    private SwipeRefreshLayout sTRL;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* building the service to check for updates */

        ComponentName mServiceComponent = new ComponentName(this, NotificationService.class);
        JobInfo task = new JobInfo.Builder(0, mServiceComponent).setPeriodic(60000)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY).build();
        jobScheduler = (JobScheduler) this.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(task);

        rvNewsList = (RecyclerView) findViewById(R.id.activity_main_recycler);
        rvNewsList.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        rvNewsList.setLayoutManager(lm);

        /* the service's built */

        urlString = "https://lenta.ru/rss/news";
        parser = new Parser();
        parser.execute(urlString);
        parser.onFinish(new Parser.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(ArrayList<Article> list) {
                nlAdaper = new NewsListAdapter(getApplicationContext(), list);
                rvNewsList.setAdapter(nlAdaper);

                MyApplication app = (MyApplication)getApplication();
                app.setArticleList(list);
            }

            @Override
            public void onError() {}
        });

        sTRL = (SwipeRefreshLayout) findViewById(R.id.swipe_to_refresh);
        sTRL.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nlAdaper!=null&&!nlAdaper.getList().get(0).equals(((MyApplication)getApplication()).getArticleList().get(0))){
            nlAdaper.updateData(((MyApplication)getApplication()).getArticleList());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        jobScheduler.cancelAll();
    }

    private void refreshItems(){
        Parser updateParser = new Parser();
        updateParser.execute(urlString);
        updateParser.onFinish(new Parser.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(ArrayList<Article> list) {
                List<Article> savedList = ((MyApplication)getApplication()).getArticleList();

                if (!savedList.get(0).equals(list.get(0))){
                    ((MyApplication)getApplication()).setArticleList(list);
                    nlAdaper.updateData(list);
                    Log.d("lol", "different");
                }
            }
            @Override
            public void onError() {}
        });
        sTRL.setRefreshing(false);
    }

}
