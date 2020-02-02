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
 * �����́@http://maoudamashii.jokersounds.com/music_rule.html�@���璸���܂���
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

        // �N�n�����1���ت��n����T��MediaPlayer��l��
        mMediaPlayer1 = new MediaPlayer();
        mMediaPlayer1.setOnPreparedListener(this);
        // �N�n�����2���ت��n����T��MediaPlayer��l��
        mMediaPlayer2 = new MediaPlayer();
        mMediaPlayer2.setOnPreparedListener(this);

        try {
            // �]�w��1���ت��n����T
            mMediaPlayer1.setDataSource(
                    this,
                    Uri.parse("android.resource://" + getPackageName() + "/"
                            + R.raw.bgm_healing02));
            // Ū����1���ت��n����
            mMediaPlayer1.prepareAsync();
            // �]�w��2���ت��n����T
            mMediaPlayer2.setDataSource(
                    this,
                    Uri.parse("android.resource://" + getPackageName() + "/"
                            + R.raw.bgm_healing03));
            // Ū����2���ت��n����T
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
        // �C�鼽�񾹬O���񤤫h����
        if (mMediaPlayer1.isPlaying()) {
            mMediaPlayer1.stop();
        }
        // �C�鼽�񾹬O���񤤫h����
        if (mMediaPlayer2.isPlaying()) {
            mMediaPlayer2.stop();
        }

        // ����C�鼽��
        mMediaPlayer1.release();
        mMediaPlayer2.release();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (mp == mMediaPlayer1) {
            // �w������1���ت�����ǳơA�}�l����
            mMediaPlayer1.start();
        } else if (mp == mMediaPlayer2) {
            // �w������2���ت�����ǳơA�]�w�U�@�������񦱥�
            mMediaPlayer1.setNextMediaPlayer(mMediaPlayer2);
        }
    }
}
