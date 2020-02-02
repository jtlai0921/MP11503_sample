package jp.co.se.android.recipe.chapter01;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Ch0106 extends Activity {
    private Handler mHandler = new Handler();
    private int value = 0;
    private ProgressBar mProgress;
    private TextView mTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0106_main);

        mProgress = (ProgressBar) findViewById(R.id.progressbar);
        mTextView = (TextView) findViewById(R.id.textview);

        // 進度的值持續增加（演示範例用）
        mHandler.post(addProgress);
    }

    @Override
    protected void onDestroy() {
        // 捨棄執行中的Handler
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    /**
     * 進度的值每10ms增加
     */
    private Runnable addProgress = new Runnable() {
        @Override
        public void run() {
            value++;
            if (value > 100) {
                value = 0;
            }
            mProgress.setProgress(value);
            mTextView.setText(value + "%");
            mHandler.postDelayed(addProgress, 10);
        }
    };
}
