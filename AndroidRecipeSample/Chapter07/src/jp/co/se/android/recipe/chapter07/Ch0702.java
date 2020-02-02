package jp.co.se.android.recipe.chapter07;

import java.io.IOException;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/***
 * 
 * ‰¹Œ¹‚Í@http://maoudamashii.jokersounds.com/music_rule.html@‚©‚ç’¸‚«‚Ü‚µ‚½
 * 
 * @author yokmama
 * 
 */
public class Ch0702 extends Activity implements OnClickListener,
        OnPreparedListener, OnCompletionListener {
    private Button mButtonPlayPause;
    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0702_main);

        mButtonPlayPause = (Button) findViewById(R.id.buttonPlayPause);
        mButtonPlayPause.setOnClickListener(this);
        mButtonPlayPause.setEnabled(false);

        // ´CÅé¼½©ñ¾¹ªºªì©l¤Æ
        mMediaPlayer = new MediaPlayer();
        // ³]©w¯à±µ¦¬¼½©ñ·Ç³Æ§¹¦¨³qª¾ªººÊÅ¥¾¹
        mMediaPlayer.setOnPreparedListener(this);
        // ³]©w¯à±µ¦¬¼½©ñ§¹¦¨³qª¾ªººÊÅ¥¾¹
        mMediaPlayer.setOnCompletionListener(this);

        // «Ø¥ß«ü©w´CÅéÀÉªº¸ô®|
        String fileName = "android.resource://" + getPackageName() + "/"
                + R.raw.bgm_healing02;
        try {
            // ±N´CÅéÀÉ³]©w¦bMediaPlayer
            mMediaPlayer.setDataSource(this, Uri.parse(fileName));
            // ±N´CÅéÀÉ°µ«D¦P¨BÅª¨ú
            mMediaPlayer.prepareAsync();
            setButtonText(mMediaPlayer);

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
        // ­Y´CÅé¼½©ñ¾¹¬O¦b¼½©ñ¤¤«h°±¤î
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        // ÄÀ©ñ´CÅé¼½©ñ¾¹
        mMediaPlayer.release();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonPlayPause) {
            if (mMediaPlayer.isPlaying()) {
                // ­Y´CÅé¼½©ñ¾¹¬O¦b¼½©ñ¤¤«h°±¤î
                mMediaPlayer.pause();
                setButtonText(mMediaPlayer);
            } else {
                // ­Y´CÅé¼½©ñ¾¹¤£¬O¦b¼½©ñ¤¤«h¼½©ñ
                mMediaPlayer.start();
                setButtonText(mMediaPlayer);
            }
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        // ¦]¬°´CÅé¼½©ñ¾¹¤w¦¨¬°¥i¼½©ñª¬ºA¡A¨Ï¼½©ñ¶s¥Í®Ä
        mButtonPlayPause.setEnabled(true);
        setButtonText(mp);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // ¦]´CÅé¼½©ñ¾¹ªº¼½©ñ¤wµ²§ô¡AÅÜ§ó«ö¶sª¬ºA
        setButtonText(mp);
    }

    private void setButtonText(MediaPlayer mp) {
        if (mp.isPlaying()) {
            // ­Y¦b¼½©ñ¤¤«h°±¤î
            mButtonPlayPause.setText(getString(R.string.text_stop));
        } else {
            // ­Y¦b°±¤î¤¤«h¼½©ñ
            mButtonPlayPause.setText(getString(R.string.text_play));
        }
    }
}
