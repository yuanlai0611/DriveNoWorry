package com.example.yuanyuanlai.drivenoworry.BackgroundService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.yuanyuanlai.drivenoworry.Utils.PollingUtils;

public class PollingReceiver extends BroadcastReceiver {

    public static String TAG = "PollingReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "调用了onReceive");
        PollingUtils.startExactAgain(context, 20, PollingReceiver.class, PollingUtils.ACTION);
        Intent i = PollingService.newIntent(context);
        context.startService(i);

    }

}
