package com.lambdaapps.applock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ScreenReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            Log.i("applock", "Screen went OFF");
            stopLockService(context);
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            Log.i("applock", "Screen went ON");
            startLockService(context);
        }
    }

    private void startLockService(Context context) {
        Intent i = new Intent(context, LockService.class);
        context.startService(i);
    }

    private void stopLockService(Context context) {
        Intent i = new Intent(context, LockService.class);
        context.stopService(i);
    }
}
