package com.mshvdvskgmail.foralfa.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, startId, startId);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        requestCheck();
    }

    private void requestCheck(){
        String urlString = "https://lenta.ru/rss/news";
        Parser parser = new Parser();
        parser.execute(urlString);
        parser.onFinish(new Parser.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(ArrayList<Article> list) {

                List<Article> savedList = ((MyApplication)getApplication()).getArticleList();

                if (!savedList.get(0).getLink().equals(list.get(0).getLink())){

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
                    notificationManager.notify(notificationNumber, notification);

                    SharedPreferences.Editor editor = prefs.edit();
                    notificationNumber++;
                    editor.putInt("notificationNumber", notificationNumber);
                    editor.commit();
                    ((MyApplication)getApplication()).setArticleList(list);
                }
            }
            @Override
            public void onError() {}
        });
    }
}
