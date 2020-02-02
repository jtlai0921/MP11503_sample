package jp.co.se.android.recipe.chapter07;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.RemoteControlClient;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

@SuppressLint("NewApi")
public class Ch0707Service extends Service {
    public static final String TAG = "Chapter07";
    private AudioManager mAudioManager;
    private MediaPlayer mMediaPlayer;
    private RemoteControlClient mRemoteControlClient;
    private ComponentName mComponentName;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
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
            registerMediaButtonEventReceiver();
        }

        mMediaPlayer.start();

        // 註冊RemoteControlClient
        registerRemoteControlClient();

        // 在播放設定螢幕解鎖的狀態
        mRemoteControlClient
                .setPlaybackState(RemoteControlClient.PLAYSTATE_PLAYING);
        // 在螢幕解鎖設定要顯示的音樂情報
        mRemoteControlClient
                .editMetadata(true)
                .putString(MediaMetadataRetriever.METADATA_KEY_ARTIST,
                        "Sample Artist")
                .putString(MediaMetadataRetriever.METADATA_KEY_ALBUM,
                        "Sample Album")
                .putString(MediaMetadataRetriever.METADATA_KEY_TITLE,
                        "Sample Music").apply();
    }

    private void pause() {
        mMediaPlayer.pause();

        // 將螢幕解鎖的狀態設定為暫停中
        mRemoteControlClient
                .setPlaybackState(RemoteControlClient.PLAYSTATE_PAUSED);
    }

    private void stop() {
        mMediaPlayer.stop();
        mMediaPlayer = null;
        unregisterMediaButtonEventReceiver();

        // 解除RemoteControlClient的註冊
        unregisterRemoteControlClient();
    }

    private void registerMediaButtonEventReceiver() {
        if (mComponentName == null) {
            // 註冊MediaButtonEventReceiver
            mComponentName = new ComponentName(this, Ch0707Receiver.class);
            // 將MediaButtonEventReceiver註冊在系統中
            mAudioManager.registerMediaButtonEventReceiver(mComponentName);
        }
    }

    private void unregisterMediaButtonEventReceiver() {
        if (mComponentName != null) {
            // 解除MediaButtonEventReceiver的註冊
            mAudioManager.unregisterMediaButtonEventReceiver(mComponentName);
            mComponentName = null;
        }

    }

    private void registerRemoteControlClient() {
        if (mRemoteControlClient == null) {
            // 建立PendingIntent，使其接收解鎖畫面所按下的按鈕事件
            Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
            mediaButtonIntent.setComponent(mComponentName);
            PendingIntent mediaPendingIntent = PendingIntent.getBroadcast(
                    getApplicationContext(), 0, mediaButtonIntent, 0);

            // 建立RemoteControlClient並設定PendingIntent
            mRemoteControlClient = new RemoteControlClient(mediaPendingIntent);
            mRemoteControlClient
                    .setTransportControlFlags(RemoteControlClient.FLAG_KEY_MEDIA_PLAY
                            | RemoteControlClient.FLAG_KEY_MEDIA_PAUSE
                            | RemoteControlClient.FLAG_KEY_MEDIA_NEXT
                            | RemoteControlClient.FLAG_KEY_MEDIA_STOP);

            // 註冊RemoteControlClient
            mAudioManager.registerRemoteControlClient(mRemoteControlClient);

            // 取得AudioFocus
            mAudioManager.requestAudioFocus(new OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int focusChange) {
                    Log.d(TAG, "focusChanged:" + focusChange);
                }
            }, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        }
    }

    private void unregisterRemoteControlClient() {
        if (mRemoteControlClient != null) {
            // 解除RemoteControlClient的註冊
            mAudioManager.unregisterRemoteControlClient(mRemoteControlClient);
            mRemoteControlClient = null;
        }

    }

}
