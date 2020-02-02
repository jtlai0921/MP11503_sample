package jp.co.se.android.recipe.chapter07;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;

/***
 * 
 * ‰¹Œ¹‚Í@http://maoudamashii.jokersounds.com/music_rule.html@‚©‚ç’¸‚«‚Ü‚µ‚½
 * 
 * @author yokmama
 * 
 */
@SuppressLint("NewApi")
public class Ch0704 extends Activity implements OnPreparedListener {
    private MediaPlayer mMediaPlayer1;
    private MediaPlayer mMediaPlayer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0704_main);

        // ±N­n¼½©ñ²Ä1¦±¥ØªºÁn­µ¸ê°TªºMediaPlayerªì©l¤Æ
        mMediaPlayer1 = new MediaPlayer();
        mMediaPlayer1.setOnPreparedListener(this);
        // ±N­n¼½©ñ²Ä2¦±¥ØªºÁn­µ¸ê°TªºMediaPlayerªì©l¤Æ
        mMediaPlayer2 = new MediaPlayer();
        mMediaPlayer2.setOnPreparedListener(this);

        try {
            // ³]©w²Ä1¦±¥ØªºÁn­µ¸ê°T
            mMediaPlayer1.setDataSource(
                    this,
                    Uri.parse("android.resource://" + getPackageName() + "/"
                            + R.raw.bgm_healing02));
            // Åª¨ú²Ä1¦±¥ØªºÁn­µ¸ê
            mMediaPlayer1.prepareAsync();
            // ³]©w²Ä2¦±¥ØªºÁn­µ¸ê°T
            mMediaPlayer2.setDataSource(
                    this,
                    Uri.parse("android.resource://" + getPackageName() + "/"
                            + R.raw.bgm_healing03));
            // Åª¨ú²Ä2¦±¥ØªºÁn­µ¸ê°T
            mMediaPlayer2.prepareAsync();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // ´CÅé¼½©ñ¾¹¬O¼½©ñ¤¤«h°±¤î
        if (mMediaPlayer1.isPlaying()) {
            mMediaPlayer1.stop();
        }
        // ´CÅé¼½©ñ¾¹¬O¼½©ñ¤¤«h°±¤î
        if (mMediaPlayer2.isPlaying()) {
            mMediaPlayer2.stop();
        }

        // ÄÀ©ñ´CÅé¼½©ñ¾¹
        mMediaPlayer1.release();
        mMediaPlayer2.release();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (mp == mMediaPlayer1) {
            // ¤w§¹¦¨²Ä1¦±¥Øªº¼½©ñ·Ç³Æ¡A¶}©l¼½©ñ
            mMediaPlayer1.start();
        } else if (mp == mMediaPlayer2) {
            // ¤w§¹¦¨²Ä2¦±¥Øªº¼½©ñ·Ç³Æ¡A³]©w¤U¤@­ºªº¼½©ñ¦±¥Ø
            mMediaPlayer1.setNextMediaPlayer(mMediaPlayer2);
        }
    }
}
