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
                // ����@
            } else if ("back".equals(action)) {
                // ����@
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

        // ���UNotification
        startForeground(1, generateNotification());
    }

    private void pause() {
        mMediaPlayer.pause();
    }

    private void stop() {
        mMediaPlayer.stop();
        mMediaPlayer = null;

        // �Ѱ�Notification
        stopForeground(true);
    }

    private Notification generateNotification() {
        // �إ�Ĳ�I�q���d��ɪ�PendingIntent
        Intent actionIntent = new Intent(getApplicationContext(), Ch0708.class);
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(),
                0, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // �إ߿W�ߪ����t�m��RemoteView
        RemoteViews mNotificationView = new RemoteViews(getPackageName(),
                R.layout.ch0708_statusbar);

        // �إ�Notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getApplicationContext());
        builder.setSmallIcon(R.drawable.ic_stat_media);
        // �N�W�ߪ������t�m�]�w�bNotificaiton
        builder.setContent(mNotificationView);
        // �]��true�N�|�@����ܦb�q���d��
        builder.setOngoing(true);
        // �b�q���d��]�w�w�]��ܮɪ��T��
        builder.setTicker("����Sample Title");
        builder.setContentIntent(pi);

        // �窱�A�C�����t�m���ҳ]�w���Ϥ��ϥܳ]�w�ϥ�
        mNotificationView.setImageViewResource(R.id.imageicon,
                R.drawable.ic_launcher);

        // �窱�A�C�����t�m���ҳ]�w�����D�W�ٳ]�w���D
        mNotificationView.setTextViewText(R.id.textTitle, "Sample Title");
        // �窱�A�C�����t�m���ҳ]�w���Ч@�̦W�ٳ]�w�Ч@��
        mNotificationView.setTextViewText(R.id.textArtist, "Sample Artist");

        // �]�w��[�Ϥ��ϥ�]�s���U�ɩҩI�s��Intent
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, Ch0708.class), Intent.FLAG_ACTIVITY_NEW_TASK);
        mNotificationView
                .setOnClickPendingIntent(R.id.imageicon, contentIntent);

        // �]�w��[����][�Ȱ�]�s���U�ɩҩI�s��Intent
        mNotificationView.setOnClickPendingIntent(R.id.btnPlay,
                createPendingIntent("playpause"));

        // �]�w��[�U�@�B]�s���U�ɩҩI�s��Intent
        mNotificationView.setOnClickPendingIntent(R.id.btnNext,
                createPendingIntent("next"));

        return builder.build();
    }

    private PendingIntent createPendingIntent(String action) {
        // ���F�x�s����Action��Service��Intent�A�إ�PendingIntent
        Intent service = new Intent(this, Ch0708Service.class);
        service.setAction(action);

        return PendingIntent.getService(this, 0, service, 0);
    }

}
