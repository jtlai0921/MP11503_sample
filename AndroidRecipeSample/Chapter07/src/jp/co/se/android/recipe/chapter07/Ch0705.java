package jp.co.se.android.recipe.chapter07;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;

public class Ch0705 extends Activity {
    private MusicIntentReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0704_main);

        // 監視耳機的連接狀態
        myReceiver = new MusicIntentReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(myReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 解除監聽耳機的連接狀態
        unregisterReceiver(myReceiver);
    }

    private class MusicIntentReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
                int state = intent.getIntExtra("state", -1);
                switch (state) {
                case 0:
                    Toast.makeText(Ch0705.this, "耳機拔出了",
                            Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(Ch0705.this, "耳機插入了",
                            Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
    }
}
