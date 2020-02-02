package jp.co.se.android.recipe.chapter12;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class Ch1201 extends Activity {
    private static final String TAG = "Chapter12";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1201_main);

        Log.d(TAG, "OnCreate");

        findViewById(R.id.buttonStart).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        startThread();

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroy");
    }

    private void startThread() {
        // 建立執行緒
        Thread thread = new Thread(new Worker());
        // 進行執行緒處理
        thread.start();
    }

    private static class Worker implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                // 每隔1秒輸出log
                Log.d(TAG, String.format("執行中%d", (i + 1)));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
