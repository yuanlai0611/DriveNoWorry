package com.example.yuanyuanlai.drivenoworry.Lock;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class LockService extends Service {

    private LockScreenReceiver lockScreenReceiver;

    @Override
    public void onCreate() {

        lockScreenReceiver = new LockScreenReceiver();
        IntentFilter mScreenOffFilte = new IntentFilter();
        mScreenOffFilte.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(lockScreenReceiver,mScreenOffFilte);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(lockScreenReceiver);
    }
}
