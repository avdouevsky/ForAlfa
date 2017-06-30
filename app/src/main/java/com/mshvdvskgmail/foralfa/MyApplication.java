package com.mshvdvskgmail.foralfa;

import android.app.Application;
import android.content.Context;

import com.prof.rssparser.Article;

import java.util.List;

/**
 * Created by mshvdvsk on 29/06/2017.
 */

public class MyApplication extends Application {
    private List <Article> articleList;
    private static Context context;

    public List<Article> getArticleList() {
        return articleList;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getContext(){
        return context;
    }

    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
    }
}
