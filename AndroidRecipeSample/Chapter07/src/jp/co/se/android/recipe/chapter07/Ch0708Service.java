package jp.co.se.android.recipe.chapter07;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

@SuppressLint("NewApi")
public class Ch0708Service extends Service {
    public static final String TAG = "Chapter07";
    private MediaPlayer mMediaPlayer;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if ("play".equals(action)) {
                if (mMediaPlayer == null || !mMediaPlayer.isPlaying()) {
                    play();
                }
            } else if ("pause".equals(action)) {
                if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                    pause();
                }
            } else if ("stop".equals(action)) {
                if (mMediaPlayer != null) {
                    stop();
                }
            } else if ("next".equals(action)) {
                // 未實作
            } else if ("back".equals(action)) {
                // 未實作
            } else if ("playpause".equals(action)) {
                if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                    pause();
                } else {
                    play();
                }
            }
        }

        return START_STICKY;
    }

    private void play() {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            String fileName = "android.resource://" + getPackageName() + "/"
                    + R.raw.bgm_healing02;
            try {
                mMediaPlayer.setDataSource(this, Uri.parse(fileName));
                mMediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        mMediaPlayer.start();

        // 註冊Notification
        startForeground(1, generateNotification());
    }

    private void pause() {
        mMediaPlayer.pause();
    }

    private void stop() {
        mMediaPlayer.stop();
        mMediaPlayer = null;

        // 解除Notification
        stopForeground(true);
    }

    private Notification generateNotification() {
        // 建立觸碰通知範圍時的PendingIntent
        Intent actionIntent = new Intent(getApplicationContext(), Ch0708.class);
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(),
                0, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // 建立獨立版面配置的RemoteView
        RemoteViews mNotificationView = new RemoteViews(getPackageName(),
                R.layout.ch0708_statusbar);

        // 建立Notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getApplicationContext());
        builder.setSmallIcon(R.drawable.ic_stat_media);
        // 將獨立的版面配置設定在Notificaiton
        builder.setContent(mNotificationView);
        // 設為true就會一直顯示在通知範圍
        builder.setOngoing(true);
        // 在通知範圍設定預設顯示時的訊息
        builder.setTicker("播放Sample Title");
        builder.setContentIntent(pi);

        // 對狀態列版面配置中所設定的圖片圖示設定圖示
        mNotificationView.setImageViewResource(R.id.imageicon,
                R.drawable.ic_launcher);

        // 對狀態列版面配置中所設定的標題名稱設定標題
        mNotificationView.setTextViewText(R.id.textTitle, "Sample Title");
        // 對狀態列版面配置中所設定的創作者名稱設定創作者
        mNotificationView.setTextViewText(R.id.textArtist, "Sample Artist");

        // 設定當[圖片圖示]鈕按下時所呼叫的Intent
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, Ch0708.class), Intent.FLAG_ACTIVITY_NEW_TASK);
        mNotificationView
                .setOnClickPendingIntent(R.id.imageicon, contentIntent);

        // 設定當[播放][暫停]鈕按下時所呼叫的Intent
        mNotificationView.setOnClickPendingIntent(R.id.btnPlay,
                createPendingIntent("playpause"));

        // 設定當[下一步]鈕按下時所呼叫的Intent
        mNotificationView.setOnClickPendingIntent(R.id.btnNext,
                createPendingIntent("next"));

        return builder.build();
    }

    private PendingIntent createPendingIntent(String action) {
        // 為了儲存對應Action的Service的Intent，建立PendingIntent
        Intent service = new Intent(this, Ch0708Service.class);
        service.setAction(action);

        return PendingIntent.getService(this, 0, service, 0);
    }

}
