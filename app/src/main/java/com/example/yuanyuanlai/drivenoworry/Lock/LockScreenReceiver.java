package com.example.yuanyuanlai.drivenoworry.Lock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class LockScreenReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        if (Intent.ACTION_SCREEN_OFF.equals(action)) {
            Intent mLockIntent = new Intent(context, LockScreenActivity.class);
            mLockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            context.startActivity(mLockIntent);
        }

    }

}
