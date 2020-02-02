package jp.co.se.android.recipe.chapter07;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

public class Ch0710 extends Activity {
    private MediaPlayer mPlayer;
    private Ch0710View mVisualizerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0710_main);

        ToggleButton togglePlay = (ToggleButton) findViewById(R.id.togglePlay);
        togglePlay.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
                if (isChecked) {
                    mPlayer.start();
                } else {
                    mPlayer.pause();
                }
            }
        });

        mPlayer = MediaPlayer.create(this, R.raw.bgm_healing02);
        mPlayer.setLooping(true);

        mVisualizerView = (Ch0710View) findViewById(R.id.visualizerView);
        mVisualizerView.attach(mPlayer);
    }

    @Override
    protected void onDestroy() {
        if (mPlayer != null) {
            mVisualizerView.deatach();
            mPlayer.release();
            mPlayer = null;
        }
        super.onDestroy();
    }
}
