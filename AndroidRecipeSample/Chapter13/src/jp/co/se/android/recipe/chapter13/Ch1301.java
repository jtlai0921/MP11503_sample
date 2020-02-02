package jp.co.se.android.recipe.chapter13;

import jp.co.se.android.recipe.chapter13.R;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

public class Ch1301 extends Activity {
    /** Notification的識別用ID */
    int ID_NOTIFICATION_SAMPLE_ACTIVITY = 0x00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1301_main);

        Switch notificationSwitch = (Switch) findViewById(R.id.notificationSwitch);
        notificationSwitch
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                            boolean isChecked) {
                        // 配合開關的切換來變更狀態列的ON/OFF
                        showNotification(isChecked);
                    }
                });

    }

    /**
     * 顯示狀態列
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void showNotification(boolean isShow) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (isShow) {
            // 通知狀態列
            // 建立啟動瀏覽器的PendingIntent
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(getString(R.string.url_shoeisha)));
            PendingIntent contentIntent = PendingIntent.getActivity(this,
                    ID_NOTIFICATION_SAMPLE_ACTIVITY, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            // Notification的設定
            Notification.Builder nb = new Notification.Builder(this);
            // 通知事件的時間戳記
            nb.setWhen(System.currentTimeMillis());
            // 設定內容
            nb.setContentIntent(contentIntent);
            // 設定圖示
            nb.setSmallIcon(android.R.drawable.stat_notify_chat);
            // 通知時要顯示的字串
            nb.setTicker(getString(R.string.label_status_ticker));
            // 要顯示在狀態列的標題
            nb.setContentTitle(getString(R.string.label_launch_browser));
            // 聲音、振動、燈光
            nb.setDefaults(Notification.DEFAULT_SOUND
                    | Notification.DEFAULT_VIBRATE
                    | Notification.DEFAULT_LIGHTS);
            // 點擊後自動消除顯示
            nb.setAutoCancel(true);
            Notification notification = nb.build();

            // Notification通知
            notificationManager.notify(ID_NOTIFICATION_SAMPLE_ACTIVITY,
                    notification);
        } else {
            // 刪除狀態列
            // 取消Notification
            notificationManager.cancel(ID_NOTIFICATION_SAMPLE_ACTIVITY);
        }
    }
}
