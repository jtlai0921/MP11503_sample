package jp.co.se.android.recipe.chapter01;

import android.app.Activity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class Ch0114 extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0114_main);

        final TextView tv = (TextView) findViewById(R.id.textview);
        final SeekBar sb = (SeekBar) findViewById(R.id.seekbar);
        tv.setText(String.valueOf(sb.getProgress()));
        sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tv.setText("©ì¦²µ²§ô");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                tv.setText("©ì¦²¶}©l");
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                    boolean fromUser) {
                tv.setText(String.valueOf(progress));
            }
        });
    }
}