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

        // �i�ת��ȫ���W�[�]�t�ܽd�ҥΡ^
        mHandler.post(addProgress);
    }

    @Override
    protected void onDestroy() {
        // �˱���椤��Handler
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    /**
     * �i�ת��ȨC10ms�W�[
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
