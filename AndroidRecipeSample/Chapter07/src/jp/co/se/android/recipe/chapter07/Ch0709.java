package jp.co.se.android.recipe.chapter07;

import java.io.IOException;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.audiofx.Equalizer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

/***
 * 
 * �����́@http://maoudamashii.jokersounds.com/music_rule.html�@���璸���܂���
 * 
 * @author yokmama
 * 
 */
@SuppressLint("NewApi")
public class Ch0709 extends Activity implements OnClickListener,
        OnPreparedListener, OnCompletionListener {
    public static final String TAG = "Chapter07";
    private Button mButtonPlayPause;
    private MediaPlayer mMediaPlayer;
    private Equalizer mEqualizer;
    private ArrayList<View> mSeekBars = new ArrayList<View>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0709_main);

        mButtonPlayPause = (Button) findViewById(R.id.buttonPlayPause);
        mButtonPlayPause.setOnClickListener(this);
        mButtonPlayPause.setEnabled(false);

        // MediaPlayer����l��
        mMediaPlayer = new MediaPlayer();
        // �]�w���౵��Media������ǳƧ����q������ť��
        mMediaPlayer.setOnPreparedListener(this);
        // �]�w���౵��Media�����񧹲��q������ť��
        mMediaPlayer.setOnCompletionListener(this);

        // �إ߫��w�C���ɮת����|
        String fileName = "android.resource://" + getPackageName() + "/"
                + R.raw.bgm_healing02;
        try {
            // �N�C���ɮ׳]�w��MediaPlayer
            mMediaPlayer.setDataSource(this, Uri.parse(fileName));
            // �N�C���ɮ׫D�P�BŪ��
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

        findViewById(R.id.checkEqulizer).setOnClickListener(this);

        // �إ�Eaulizer
        mEqualizer = new Equalizer(0, mMediaPlayer.getAudioSessionId());

        LinearLayout layoutBandlevels = (LinearLayout) findViewById(R.id.layoutBandlevels);
        LayoutInflater layoutInflater = getLayoutInflater();

        // �u����Eaulizer band�ƶq��SeekBar
        short bands = mEqualizer.getNumberOfBands();
        for (int i = 0; i < bands; i++) {
            View layout = layoutInflater.inflate(R.layout.ch0709_seekbar,
                    layoutBandlevels, false);
            layoutBandlevels.addView(layout,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mSeekBars.add(layout);
            SeekBar seekbar = (SeekBar) layout.findViewById(R.id.seekBar);
            // ���F����b��ť�����P�_�O����band��SeekBar�A���b�������]�w���
            seekbar.setTag(i);
            // Ĳ�ISeekBar���oSeekBar���Ȩó]�w�bEqualizer
            seekbar.setOnTouchListener(new OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        SeekBar seekbar = (SeekBar) v;
                        short maxEQLevel = mEqualizer.getBandLevelRange()[1];
                        // ���o�w���]�w��SeekBar�s��
                        int index = (Integer) seekbar.getTag();
                        // ���o�ܧ�᪺SeekBar�ȡA�åH�̤p�Ȩӭץ�
                        short band = (short) (seekbar.getProgress() - maxEQLevel);
                        View layout = mSeekBars.get(index);
                        // �H�ܧ�᪺SeekBar�Ȭ���Ǩ���ܡA�ç�s���ƾ���band��
                        TextView textFreq = (TextView) layout
                                .findViewById(R.id.textFreq);
                        textFreq.setText(String.format("%6d", band));
                        // �b���ƾ��]�wband��
                        mEqualizer.setBandLevel((short) index, band);
                    }
                    return false;
                }
            });
        }

        // ��sEqualizer�����
        updateEqlizerValue();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // �YMediaPlayer�b���񤤫h����
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        // ����MediaPlayer
        mMediaPlayer.release();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonPlayPause) {
            if (mMediaPlayer.isPlaying()) {
                // �YMediaPlayer�b����h����
                mMediaPlayer.pause();
                setButtonText(mMediaPlayer);
            } else {
                // �YMediaPlayer���O�b���񤤫h����
                mMediaPlayer.start();
                setButtonText(mMediaPlayer);
            }
        } else if (v.getId() == R.id.checkEqulizer) {
            CheckBox checkbox = (CheckBox) v;
            // �N���ƾ����]�w�ͮĩεL��
            mEqualizer.setEnabled(checkbox.isChecked());
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        // �]��MediaPlayer�w�ܧ󬰥i���񪬺A�A�N����s�]�w�ͮ�
        mButtonPlayPause.setEnabled(true);
        setButtonText(mp);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // �]��MediaPlayer�w���񧹲��A�ܧ���s���A
        setButtonText(mp);
    }

    private void setButtonText(MediaPlayer mp) {
        if (mp.isPlaying()) {
            // �Y�b���񤤫h��ܰ���
            mButtonPlayPause.setText(getString(R.string.text_stop));
        } else {
            // �Y�b����h��ܼ���
            mButtonPlayPause.setText(getString(R.string.text_play));
        }
    }

    private void updateEqlizerValue() {
        // ���o���ƾ���band��
        short bands = mEqualizer.getNumberOfBands();
        // ���ƾ�band���̤p��
        short minEQLevel = mEqualizer.getBandLevelRange()[0];
        // ���ƾ�band���̤j��
        short maxEQLevel = mEqualizer.getBandLevelRange()[1];
        // �u�]�w���ƾ�band�ƥت�SeekBar�PTextView���w�]��
        for (int i = 0; i < bands; i++) {
            if (mSeekBars.size() > i) {
                View layout = mSeekBars.get(i);
                SeekBar seekbar = (SeekBar) layout.findViewById(R.id.seekBar);
                TextView textFreq = (TextView) layout
                        .findViewById(R.id.textFreq);
                TextView textFreqMax = (TextView) layout
                        .findViewById(R.id.textFreqMax);
                seekbar.setMax(maxEQLevel + Math.abs(minEQLevel));
                // ���o�ثe�����ƾ���band��
                short band = mEqualizer.getBandLevel((short) i);
                // ���o���ƾ��W�v���a����
                int freq = mEqualizer.getCenterFreq((short) i) / 1000;
                // �bSeekBar�]�wband��
                seekbar.setProgress(band + Math.abs(minEQLevel));
                textFreq.setText(String.format("%6d", band));
                textFreqMax.setText(String.format("%6dHz", freq));
            }
        }
    }
}
