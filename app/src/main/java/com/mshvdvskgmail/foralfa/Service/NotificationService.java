package com.mshvdvskgmail.foralfa.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.mshvdvskgmail.foralfa.MyApplication;
import com.mshvdvskgmail.foralfa.R;
import com.mshvdvskgmail.foralfa.activities.MainActivity;
import com.mshvdvskgmail.foralfa.activities.WebActivity;
import com.mshvdvskgmail.foralfa.adapters.NewsListAdapter;
import com.prof.rssparser.Article;
import com.prof.rssparser.Parser;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by mshvdvsk on 29/06/2017.
 */

public class NotificationService extends IntentService{

    private Context context;

    private int id = 0;
        /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public NotificationService(String name) {
        super(name);
        Log.d("lol", "public NotificationService");
    }

    public NotificationService() {
        super("NotificationService");
        Log.d("lol", "public NotificationService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Log.d("lol", "onCreate");
        requestCheck();
    }

    //    public NotificationService() {
//        super("NotificationService");
//    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("lol", "onHandleIntent");
        requestCheck();
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//        Notification notification = builder//.setContentIntent(contentIntent)
//                .setSmallIcon(R.drawable.ic_notification).setAutoCancel(true)
//                .setContentTitle("forAlfa")
//                .setContentText("I am working OMG!!!").build();
//        notificationManager.notify(id++, notification);
    }

    private void requestCheck(){
        Log.d("lol", "requestCheck");

        String urlString = "https://lenta.ru/rss/news";
        Parser parser = new Parser();
        parser.execute(urlString);
        parser.onFinish(new Parser.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(ArrayList<Article> list) {
                Log.d("lol", "onTaskCompleted");

                MyApplication app = (MyApplication)getApplication();
                List<Article> appList = app.getArticleList();

                Log.d("lol", appList.get(0).getLink());
                Log.d("lol", list.get(0).getLink());

                if (!appList.get(0).getLink().equals(list.get(0).getLink())){

                    SharedPreferences prefs = getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
                    int notificationNumber = prefs.getInt("notificationNumber", 0);

                    NotificationManager notificationManager =
                            (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);


                    Intent intent = new Intent(context, WebActivity.class);
                    intent.putExtra("link", list.get(0).getLink());
                    PendingIntent contentIntent =
                            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

                    Notification notification = builder.setContentIntent(contentIntent)
                            .setSmallIcon(R.drawable.ic_notification).setAutoCancel(true)
                            .setContentTitle("forAlfa")
                            .setContentText(list.get(0).getTitle()).build();


                    Log.d("lol", "notification id is "+notificationNumber);
                    notificationManager.notify(notificationNumber, notification);

                    SharedPreferences.Editor editor = prefs.edit();
                    notificationNumber++;
                    editor.putInt("notificationNumber", notificationNumber);
                    editor.commit();
                }

//                for (Article artcl : list){
//                    Log.d("wow", artcl.toString());
//                }
////                NewsListAdapter nlAdaper = new NewsListAdapter(list);
//                rvNewsList.setAdapter(new NewsListAdapter(list));
//
//                MyApplication app = (MyApplication)getApplication();
//                try{
//                    Log.d("lol", ""+app.getArticleList().equals(list));
//
//                } catch (Exception e){
//                    Log.d("lol exception", e.getMessage());
//                }
//
//                List<Article> list2 = app.getArticleList();
//
//                // TODO изменить логическую проверку, объекты никогда не равны
//
//                if (!list2.equals(list)){
//                    app.setArticleList(list);
//                    nlAdaper.updateData(list);
//                    Log.d("lol", "different");
//                } else {
//                    Log.d("lol", "simmilar");
//                }
//
//                MyApplication app2 = (MyApplication)getApplication();
            }

            @Override
            public void onError() {
                //what to do in case of error
            }
        });
    }

}
