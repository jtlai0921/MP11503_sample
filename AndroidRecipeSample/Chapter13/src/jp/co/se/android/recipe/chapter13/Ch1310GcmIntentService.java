package jp.co.se.android.recipe.chapter13;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class Ch1310GcmIntentService extends IntentService {

    private static final String TAG = "Ch1311GcmIntentService";

    public Ch1310GcmIntentService() {
        super("Ch1311GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        // 取得GoogleCloudMessaging實例
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        if (gcm != null) {
            // 顯示GCM接收資料
            StringBuilder log = new StringBuilder();
            log.append("MessageType:").append(gcm.getMessageType(intent));
            if (!extras.isEmpty()) {
                log.append("\n").append("Extras:").append(extras.toString());
            }
            Log.d(TAG, log.toString());
        }
        Ch1310GcmBroadcastReceiver.completeWakefulIntent(intent);
    }
}
