package jp.co.se.android.recipe.chapter13;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class Ch1310GcmBroadcastReceiver extends WakefulBroadcastReceiver {
    private String TAG = "Ch1311GcmBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Ch1311GcmBroadcastReceiver.onReceive:" + intent.getAction());
        ComponentName comp = new ComponentName(context.getPackageName(),
                Ch1310GcmIntentService.class.getName());
        // 啟動Ch1311GcmIntentService，在服務處理中保存WakeLock
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }

}
