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
 * 音源は　http://maoudamashii.jokersounds.com/music_rule.html　から頂きました
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

        // ｴCﾅ鮠ｽｩ�ｾｹｪｺｪ�ｩl､ﾆ
        mMediaPlayer = new MediaPlayer();
        // ｳ]ｩwｯ牾ｵｦｬｼｽｩ�ｷﾇｳﾆｧｹｦｨｳqｪｾｪｺｺﾊﾅ･ｾｹ
        mMediaPlayer.setOnPreparedListener(this);
        // ｳ]ｩwｯ牾ｵｦｬｼｽｩ�ｧｹｦｨｳqｪｾｪｺｺﾊﾅ･ｾｹ
        mMediaPlayer.setOnCompletionListener(this);

        // ｫﾘ･ﾟｫ�ｩwｴCﾅ鯊ﾉｪｺｸ�ｮ|
        String fileName = "android.resource://" + getPackageName() + "/"
                + R.raw.bgm_healing02;
        try {
            // ｱNｴCﾅ鯊ﾉｳ]ｩwｦbMediaPlayer
            mMediaPlayer.setDataSource(this, Uri.parse(fileName));
            // ｱNｴCﾅ鯊ﾉｰｵｫDｦPｨBﾅｪｨ�
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
        // ｭYｴCﾅ鮠ｽｩ�ｾｹｬOｦbｼｽｩ�､､ｫhｰｱ､�
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        // ﾄﾀｩ�ｴCﾅ鮠ｽｩ�ｾｹ
        mMediaPlayer.release();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonPlayPause) {
            if (mMediaPlayer.isPlaying()) {
                // ｭYｴCﾅ鮠ｽｩ�ｾｹｬOｦbｼｽｩ�､､ｫhｰｱ､�
                mMediaPlayer.pause();
                setButtonText(mMediaPlayer);
            } else {
                // ｭYｴCﾅ鮠ｽｩ�ｾｹ､｣ｬOｦbｼｽｩ�､､ｫhｼｽｩ�
                mMediaPlayer.start();
                setButtonText(mMediaPlayer);
            }
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        // ｦ]ｬｰｴCﾅ鮠ｽｩ�ｾｹ､wｦｨｬｰ･iｼｽｩ�ｪｬｺA｡Aｨﾏｼｽｩ�ｶs･ﾍｮﾄ
        mButtonPlayPause.setEnabled(true);
        setButtonText(mp);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // ｦ]ｴCﾅ鮠ｽｩ�ｾｹｪｺｼｽｩ�､wｵｲｧ�｡Aﾅﾜｧ�ｫ�ｶsｪｬｺA
        setButtonText(mp);
    }

    private void setButtonText(MediaPlayer mp) {
        if (mp.isPlaying()) {
            // ｭYｦbｼｽｩ�､､ｫhｰｱ､�
            mButtonPlayPause.setText(getString(R.string.text_stop));
        } else {
            // ｭYｦbｰｱ､�､､ｫhｼｽｩ�
            mButtonPlayPause.setText(getString(R.string.text_play));
        }
    }
}
