package jp.co.se.android.recipe.chapter01;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class Ch0136 extends Activity {
    private Handler mHandler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0136_main);

        final ImageView iv = (ImageView) findViewById(R.id.imageView);
        final ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar);

        // 模擬3秒的讀取狀態
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // 設為不顯示進度的狀態
                pb.setVisibility(View.GONE);
                // 設為顯示ImageView的狀態
                iv.setVisibility(View.VISIBLE);
                iv.setImageResource(R.drawable.ic_launcher);
            }
        }, 3000);
    }
}
