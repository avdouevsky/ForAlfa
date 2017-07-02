package com.mshvdvskgmail.foralfa.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.mshvdvskgmail.foralfa.MyApplication;
import com.mshvdvskgmail.foralfa.R;
import com.mshvdvskgmail.foralfa.activities.MainActivity;
import com.mshvdvskgmail.foralfa.activities.WebActivity;
import com.prof.rssparser.Article;
import com.prof.rssparser.Parser;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by mshvdvsk on 29/06/2017.
 */

public class NotificationService extends Service{

    private Context context;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        prefs = getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        editor = prefs.edit();

        Log.d("service", "onCreate()");
        Log.d("service", "checkIfAppIsRunning returns "+checkIfAppIsRunning());
        Log.d("service", "getApplication() "+getApplication());
        checkForUpdates();
//        if(checkIfAppIsRunning()){
//            requestCheck();
//        } else {
//            Log.d("service", "stoppingMyself");
//            JobScheduler jobScheduler = (JobScheduler) this.getSystemService(Context.JOB_SCHEDULER_SERVICE);
//            jobScheduler.cancel(0);
//            stopSelf();
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("service", "onDestroy()");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.d("service", "onTaskRemoved()");
        stopSelf();
    }

    private void checkForUpdates(){
        Log.d("service", "checkForUpdates");
        Parser parser = new Parser();
        parser.execute(getResources().getString(R.string.rss_url));
        parser.onFinish(new Parser.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(ArrayList<Article> list) {
                Log.d(this.toString()+" service", "onTaskCompleted");
                checkForNewArticles(list);
            }
            @Override
            public void onError() {}
        });
    }

    private void checkForNewArticles(List<Article> list){
        if (!getRecentLinkInSharedPref().equals(list.get(0).getLink())){
            Log.d("service", "found a new one");
            int notificationNumber = getRecentIdInSharedPref();
            buildNotification(list.get(0), notificationNumber);
            saveRecentIdInSharedPref(++notificationNumber);
            saveRecentLinkInSharedPref(list.get(0).getLink());
            ((MyApplication)getApplication()).setArticleList(list);
        }
    }

    private boolean checkIfAppIsRunning(){
        if (((MyApplication)getApplication()).getArticleList()==null){
            return false;
        } else return true;
    }

    private void saveRecentLinkInSharedPref(String string){
        editor.putString("recent_link", string);
        editor.commit();
    }

    private String getRecentLinkInSharedPref(){
        return prefs.getString("recent_link", "");
    }

    private void saveRecentIdInSharedPref(int id){
        editor.putInt("notificationNumber", id);
        editor.commit();
    }

    private int getRecentIdInSharedPref(){
        return prefs.getInt("notificationNumber", 0);
    }

    private void buildNotification(Article article, int notificationNumber){
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra("link", article.getLink());
        PendingIntent contentIntent =
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification notification = builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_notification).setAutoCancel(false)
                .setContentTitle("forAlfa")
                .setContentText(article.getTitle()).build();
        notificationManager.notify(notificationNumber, notification);
    }

}
