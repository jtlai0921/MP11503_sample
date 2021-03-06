package jp.co.se.android.recipe.chapter07;

import java.io.IOException;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

public class Ch0703 extends Activity {
    private static final String TAG = "Ch0703";
    // 再生
    private MediaRecorder mMediaRecorder;
    private MediaPlayer mMediaPlayer;
    private String mFilePath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0703_main);

        ToggleButton recSwitch = (ToggleButton) findViewById(R.id.toggleRecord);
        recSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
                if (isChecked) {
                    // 録音の開始
                    startRecord();
                } else {
                    // ｰｱ､�ｿ�ｭｵ
                    if (mMediaRecorder != null) {
                        mMediaRecorder.stop();
                        mMediaRecorder.reset();
                    }
                }
            }
        });

        // ｵ�･Uｼｽｩ�ｶsｪｺｨﾆ･�
        findViewById(R.id.buttonPlay).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // ｼｽｩ�ｩﾒｿ�､Uｪｺﾁnｭｵ
                startPlay();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // MediaPlayerｪｺﾄﾀｩ�ｳBｲz
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }

        // MediaRecorderｪｺﾄﾀｩ�ｳBｲz
        if (mMediaRecorder != null) {
            if (mMediaRecorder != null) {
                mMediaRecorder.release();
                mMediaRecorder = null;
            }
        }
    }

    /**
    * ｱNMediaRecorderｪ�ｩl､ﾆｨﾃｶ}ｩlｿ�ｭｵ
     */
    private void startRecord() {
        // ｭYｬOｦbｼｽｩ�､､ｫhｰｱ､�ｼｽｩ�
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
        }

        // MediaRecorderｪｺｪ�ｩl､ﾆ
        if (mMediaRecorder == null) {
            mMediaRecorder = new MediaRecorder();
        }
        // ｱNｿ鬢Jｷｽｳ]ｩwｬｰｳﾁｧJｭｷ
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        // ｱNｫOｦsｮ讎｡ｳ]ｬｰ3gp
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        // ｱNAudioｽsｽXｾｹｳ]ｩwｬｰｪ�ｩlｪｬｺA
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

        // ｦb･~ｳ｡ﾀxｦsｾｹ(microSDｵ･ｵ･)ｨﾏ･ﾎ｡uhoge.3gp｡vｪｺﾀﾉｦWｨﾓﾀxｦs
        String fileName = "hoge.3gp";
        mFilePath = Environment.getExternalStorageDirectory() + "/" + fileName;
        mMediaRecorder.setOutputFile(mFilePath);

        // ｧｹｦｨｿ�ｭｵｷﾇｳﾆｫ皎Yｶ}ｩlｿ�ｭｵ
        try {
            mMediaRecorder.prepare();
            mMediaRecorder.start();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.toString(), e);
        }
    }

    /**
     * ｪ�ｩl､ﾆMediaPlayerｨﾃｼｽｩ�ﾁnｭｵ
     */
    private void startPlay() {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
        } else {
            mMediaPlayer.reset();
        }
        try {
            mMediaPlayer.setDataSource(mFilePath);
            // ｷ�ﾁnｭｵｪｺｩI･sｷﾇｳﾆｧｹｦｨｮﾉｩﾒｭnｩI･sｪｺｺﾊﾅ･ｾｹ
            mMediaPlayer.setOnPreparedListener(new OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // ｼｽｩ�ﾁnｭｵ
                    mp.start();
                }
            });
            mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    Toast.makeText(Ch0703.this,
                            R.string.text_complete_recordplay,
                            Toast.LENGTH_SHORT).show();
                }
            });
            mMediaPlayer.prepareAsync();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            Log.e(TAG, e.toString(), e);
        } catch (SecurityException e) {
            e.printStackTrace();
            Log.e(TAG, e.toString(), e);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            Log.e(TAG, e.toString(), e);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, e.toString(), e);
        }
    }
}
