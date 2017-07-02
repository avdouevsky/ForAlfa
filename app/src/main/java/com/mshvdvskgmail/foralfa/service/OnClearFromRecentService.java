package com.mshvdvskgmail.foralfa.service;

import android.app.Service;
import android.app.job.JobScheduler;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.util.Log;

/**
 * Created by mshvdvsk on 02/07/2017.
 */

public class OnClearFromRecentService extends Service{
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            Log.d("ClearFromRecentService", "Service Started");
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("ClearFromRecentService", "Service Destroyed");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d("ClearFromRecentService", "END");
        JobScheduler jobScheduler = (JobScheduler) this.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.cancel(0);
        stopSelf();
    }
}
