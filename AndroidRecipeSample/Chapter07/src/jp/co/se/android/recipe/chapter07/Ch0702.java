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
 * �����́@http://maoudamashii.jokersounds.com/music_rule.html�@���璸���܂���
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

        // �C�鼽�񾹪���l��
        mMediaPlayer = new MediaPlayer();
        // �]�w�౵������ǳƧ����q������ť��
        mMediaPlayer.setOnPreparedListener(this);
        // �]�w�౵�����񧹦��q������ť��
        mMediaPlayer.setOnCompletionListener(this);

        // �إ߫��w�C���ɪ����|
        String fileName = "android.resource://" + getPackageName() + "/"
                + R.raw.bgm_healing02;
        try {
            // �N�C���ɳ]�w�bMediaPlayer
            mMediaPlayer.setDataSource(this, Uri.parse(fileName));
            // �N�C���ɰ��D�P�BŪ��
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
        // �Y�C�鼽�񾹬O�b���񤤫h����
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        // ����C�鼽��
        mMediaPlayer.release();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonPlayPause) {
            if (mMediaPlayer.isPlaying()) {
                // �Y�C�鼽�񾹬O�b���񤤫h����
                mMediaPlayer.pause();
                setButtonText(mMediaPlayer);
            } else {
                // �Y�C�鼽�񾹤��O�b���񤤫h����
                mMediaPlayer.start();
                setButtonText(mMediaPlayer);
            }
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        // �]���C�鼽�񾹤w�����i���񪬺A�A�ϼ���s�ͮ�
        mButtonPlayPause.setEnabled(true);
        setButtonText(mp);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // �]�C�鼽�񾹪�����w�����A�ܧ���s���A
        setButtonText(mp);
    }

    private void setButtonText(MediaPlayer mp) {
        if (mp.isPlaying()) {
            // �Y�b���񤤫h����
            mButtonPlayPause.setText(getString(R.string.text_stop));
        } else {
            // �Y�b����h����
            mButtonPlayPause.setText(getString(R.string.text_play));
        }
    }
}
