package jp.co.se.android.recipe.chapter07;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;

@SuppressLint("NewApi")
public class Ch0706Service extends Service {
    public static final String TAG = "Chapter07";
    private AudioManager mAudioManager;
    private MediaPlayer mMediaPlayer;
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
    }

    private void pause() {
        mMediaPlayer.pause();
    }

    private void stop() {
        mMediaPlayer.stop();
        mMediaPlayer = null;
        unregisterMediaButtonEventReceiver();
    }

    private void registerMediaButtonEventReceiver() {
        if (mComponentName == null) {
            // 註冊MediaButtonEventReceiver
            mComponentName = new ComponentName(this, Ch0706Receiver.class);
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

}
