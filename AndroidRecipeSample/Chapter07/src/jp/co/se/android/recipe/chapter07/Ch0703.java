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
    // çƒê∂
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
                    // ò^âπÇÃäJén
                    startRecord();
                } else {
                    // ∞±§Óø˝≠µ
                    if (mMediaRecorder != null) {
                        mMediaRecorder.stop();
                        mMediaRecorder.reset();
                    }
                }
            }
        });

        // µ˘•UºΩ©Ò∂s™∫®∆•Û
        findViewById(R.id.buttonPlay).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // ºΩ©Ò©“ø˝§U™∫¡n≠µ
                startPlay();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // MediaPlayer™∫ƒ¿©Ò≥B≤z
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }

        // MediaRecorder™∫ƒ¿©Ò≥B≤z
        if (mMediaRecorder != null) {
            if (mMediaRecorder != null) {
                mMediaRecorder.release();
                mMediaRecorder = null;
            }
        }
    }

    /**
    * ±NMediaRecorder™Ï©l§∆®√∂}©lø˝≠µ
     */
    private void startRecord() {
        // ≠Y¨O¶bºΩ©Ò§§´h∞±§ÓºΩ©Ò
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
        }

        // MediaRecorder™∫™Ï©l§∆
        if (mMediaRecorder == null) {
            mMediaRecorder = new MediaRecorder();
        }
        // ±NøÈ§J∑Ω≥]©w¨∞≥¡ßJ≠∑
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        // ±N´O¶sÆÊ¶°≥]¨∞3gp
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        // ±NAudioΩsΩXæπ≥]©w¨∞™Ï©l™¨∫A
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

        // ¶b•~≥°¿x¶sæπ(microSDµ•µ•)®œ•Œ°uhoge.3gp°v™∫¿…¶W®”¿x¶s
        String fileName = "hoge.3gp";
        mFilePath = Environment.getExternalStorageDirectory() + "/" + fileName;
        mMediaRecorder.setOutputFile(mFilePath);

        // ßπ¶®ø˝≠µ∑«≥∆´·ßY∂}©lø˝≠µ
        try {
            mMediaRecorder.prepare();
            mMediaRecorder.start();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.toString(), e);
        }
    }

    /**
     * ™Ï©l§∆MediaPlayer®√ºΩ©Ò¡n≠µ
     */
    private void startPlay() {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
        } else {
            mMediaPlayer.reset();
        }
        try {
            mMediaPlayer.setDataSource(mFilePath);
            // ∑Ì¡n≠µ™∫©I•s∑«≥∆ßπ¶®Æ…©“≠n©I•s™∫∫ ≈•æπ
            mMediaPlayer.setOnPreparedListener(new OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // ºΩ©Ò¡n≠µ
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
