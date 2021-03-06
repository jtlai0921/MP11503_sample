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
 * 音源は　http://maoudamashii.jokersounds.com/music_rule.html　から頂きました
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

        // ｱNｭnｼｽｩ�ｲﾄ1ｦｱ･ﾘｪｺﾁnｭｵｸ�ｰTｪｺMediaPlayerｪ�ｩl､ﾆ
        mMediaPlayer1 = new MediaPlayer();
        mMediaPlayer1.setOnPreparedListener(this);
        // ｱNｭnｼｽｩ�ｲﾄ2ｦｱ･ﾘｪｺﾁnｭｵｸ�ｰTｪｺMediaPlayerｪ�ｩl､ﾆ
        mMediaPlayer2 = new MediaPlayer();
        mMediaPlayer2.setOnPreparedListener(this);

        try {
            // ｳ]ｩwｲﾄ1ｦｱ･ﾘｪｺﾁnｭｵｸ�ｰT
            mMediaPlayer1.setDataSource(
                    this,
                    Uri.parse("android.resource://" + getPackageName() + "/"
                            + R.raw.bgm_healing02));
            // ﾅｪｨ�ｲﾄ1ｦｱ･ﾘｪｺﾁnｭｵｸ�
            mMediaPlayer1.prepareAsync();
            // ｳ]ｩwｲﾄ2ｦｱ･ﾘｪｺﾁnｭｵｸ�ｰT
            mMediaPlayer2.setDataSource(
                    this,
                    Uri.parse("android.resource://" + getPackageName() + "/"
                            + R.raw.bgm_healing03));
            // ﾅｪｨ�ｲﾄ2ｦｱ･ﾘｪｺﾁnｭｵｸ�ｰT
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
        // ｴCﾅ鮠ｽｩ�ｾｹｬOｼｽｩ�､､ｫhｰｱ､�
        if (mMediaPlayer1.isPlaying()) {
            mMediaPlayer1.stop();
        }
        // ｴCﾅ鮠ｽｩ�ｾｹｬOｼｽｩ�､､ｫhｰｱ､�
        if (mMediaPlayer2.isPlaying()) {
            mMediaPlayer2.stop();
        }

        // ﾄﾀｩ�ｴCﾅ鮠ｽｩ�ｾｹ
        mMediaPlayer1.release();
        mMediaPlayer2.release();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (mp == mMediaPlayer1) {
            // ､wｧｹｦｨｲﾄ1ｦｱ･ﾘｪｺｼｽｩ�ｷﾇｳﾆ｡Aｶ}ｩlｼｽｩ�
            mMediaPlayer1.start();
        } else if (mp == mMediaPlayer2) {
            // ､wｧｹｦｨｲﾄ2ｦｱ･ﾘｪｺｼｽｩ�ｷﾇｳﾆ｡Aｳ]ｩw､U､@ｭｺｪｺｼｽｩ�ｦｱ･ﾘ
            mMediaPlayer1.setNextMediaPlayer(mMediaPlayer2);
        }
    }
}
