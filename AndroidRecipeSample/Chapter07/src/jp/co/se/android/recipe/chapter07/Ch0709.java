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
 * ‰¹Œ¹‚Í@http://maoudamashii.jokersounds.com/music_rule.html@‚©‚ç’¸‚«‚Ü‚µ‚½
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

        // MediaPlayerªºªì©l¤Æ
        mMediaPlayer = new MediaPlayer();
        // ³]©w¬°¯à±µ¦¬Mediaªº¼½©ñ·Ç³Æ§¹¦¨³qª¾ªººÊÅ¥¾¹
        mMediaPlayer.setOnPreparedListener(this);
        // ³]©w¬°¯à±µ¦¬Mediaªº¼½©ñ§¹²¦³qª¾ªººÊÅ¥¾¹
        mMediaPlayer.setOnCompletionListener(this);

        // «Ø¥ß«ü©w´CÅéÀÉ®×ªº¸ô®|
        String fileName = "android.resource://" + getPackageName() + "/"
                + R.raw.bgm_healing02;
        try {
            // ±N´CÅéÀÉ®×³]©w¨ìMediaPlayer
            mMediaPlayer.setDataSource(this, Uri.parse(fileName));
            // ±N´CÅéÀÉ®×«D¦P¨BÅª¨ú
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

        // «Ø¥ßEaulizer
        mEqualizer = new Equalizer(0, mMediaPlayer.getAudioSessionId());

        LinearLayout layoutBandlevels = (LinearLayout) findViewById(R.id.layoutBandlevels);
        LayoutInflater layoutInflater = getLayoutInflater();

        // ¥u²£¥ÍEaulizer band¼Æ¶qªºSeekBar
        short bands = mEqualizer.getNumberOfBands();
        for (int i = 0; i < bands; i++) {
            View layout = layoutInflater.inflate(R.layout.ch0709_seekbar,
                    layoutBandlevels, false);
            layoutBandlevels.addView(layout,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mSeekBars.add(layout);
            SeekBar seekbar = (SeekBar) layout.findViewById(R.id.seekBar);
            // ¬°¤F¯à°÷¦bºÊÅ¥¾¹¤¤§PÂ_¬O­ş­ÓbandªºSeekBar¡A¥ı¦b¤À­¶¤¤³]©w¨ä­È
            seekbar.setTag(i);
            // Ä²¸ISeekBar¨ú±oSeekBarªº­È¨Ã³]©w¦bEqualizer
            seekbar.setOnTouchListener(new OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        SeekBar seekbar = (SeekBar) v;
                        short maxEQLevel = mEqualizer.getBandLevelRange()[1];
                        // ¨ú±o¹w¥ı³]©wªºSeekBar½s¸¹
                        int index = (Integer) seekbar.getTag();
                        // ¨ú±oÅÜ§ó«áªºSeekBar­È¡A¨Ã¥H³Ì¤p­È¨Ó­×¥¿
                        short band = (short) (seekbar.getProgress() - maxEQLevel);
                        View layout = mSeekBars.get(index);
                        // ¥HÅÜ§ó«áªºSeekBar­È¬°°ò·Ç¨ÓÅã¥Ü¡A¨Ã§ó·sµ¥¤Æ¾¹ªºband­È
                        TextView textFreq = (TextView) layout
                                .findViewById(R.id.textFreq);
                        textFreq.setText(String.format("%6d", band));
                        // ¦bµ¥¤Æ¾¹³]©wband­È
                        mEqualizer.setBandLevel((short) index, band);
                    }
                    return false;
                }
            });
        }

        // §ó·sEqualizerªºÅã¥Ü
        updateEqlizerValue();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // ­YMediaPlayer¦b¼½©ñ¤¤«h°±¤î
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        // ÄÀ©ñMediaPlayer
        mMediaPlayer.release();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonPlayPause) {
            if (mMediaPlayer.isPlaying()) {
                // ­YMediaPlayer¦b¼½©ñ«h°±¤î
                mMediaPlayer.pause();
                setButtonText(mMediaPlayer);
            } else {
                // ­YMediaPlayer¤£¬O¦b¼½©ñ¤¤«h¼½©ñ
                mMediaPlayer.start();
                setButtonText(mMediaPlayer);
            }
        } else if (v.getId() == R.id.checkEqulizer) {
            CheckBox checkbox = (CheckBox) v;
            // ±Nµ¥¤Æ¾¹ªº³]©w¥Í®Ä©ÎµL®Ä
            mEqualizer.setEnabled(checkbox.isChecked());
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        // ¦]¥¼MediaPlayer¤wÅÜ§ó¬°¥i¼½©ñª¬ºA¡A±N¼½©ñ¶s³]©w¥Í®Ä
        mButtonPlayPause.setEnabled(true);
        setButtonText(mp);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // ¦]¬°MediaPlayer¤w¼½©ñ§¹²¦¡AÅÜ§ó«ö¶sª¬ºA
        setButtonText(mp);
    }

    private void setButtonText(MediaPlayer mp) {
        if (mp.isPlaying()) {
            // ­Y¦b¼½©ñ¤¤«hÅã¥Ü°±¤î
            mButtonPlayPause.setText(getString(R.string.text_stop));
        } else {
            // ­Y¦b°±¤î¤¤«hÅã¥Ü¼½©ñ
            mButtonPlayPause.setText(getString(R.string.text_play));
        }
    }

    private void updateEqlizerValue() {
        // ¨ú±oµ¥¤Æ¾¹ªºband¼Æ
        short bands = mEqualizer.getNumberOfBands();
        // µ¥¤Æ¾¹bandªº³Ì¤p­È
        short minEQLevel = mEqualizer.getBandLevelRange()[0];
        // µ¥¤Æ¾¹bandªº³Ì¤j­È
        short maxEQLevel = mEqualizer.getBandLevelRange()[1];
        // ¥u³]©wµ¥¤Æ¾¹band¼Æ¥ØªºSeekBar»PTextViewªº¹w³]­È
        for (int i = 0; i < bands; i++) {
            if (mSeekBars.size() > i) {
                View layout = mSeekBars.get(i);
                SeekBar seekbar = (SeekBar) layout.findViewById(R.id.seekBar);
                TextView textFreq = (TextView) layout
                        .findViewById(R.id.textFreq);
                TextView textFreqMax = (TextView) layout
                        .findViewById(R.id.textFreqMax);
                seekbar.setMax(maxEQLevel + Math.abs(minEQLevel));
                // ¨ú±o¥Ø«eªºµ¥¤Æ¾¹ªºband­È
                short band = mEqualizer.getBandLevel((short) i);
                // ¨ú±oµ¥¤Æ¾¹ÀW²vªş±aªº­È
                int freq = mEqualizer.getCenterFreq((short) i) / 1000;
                // ¦bSeekBar³]©wband­È
                seekbar.setProgress(band + Math.abs(minEQLevel));
                textFreq.setText(String.format("%6d", band));
                textFreqMax.setText(String.format("%6dHz", freq));
            }
        }
    }
}
