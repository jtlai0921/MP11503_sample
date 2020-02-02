package jp.co.se.android.recipe.chapter13;

import jp.co.se.android.recipe.chapter13.R;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

public class Ch1309 extends Activity {
    private ScreenReceiver mScreenReciever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1309_main);
        final TextView tvDescription = (TextView) findViewById(R.id.description);
        final Switch unlockSwitch = (Switch) findViewById(R.id.screenSwitch);

        // 開關的ON／OFF
        unlockSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
                if (isChecked) {
                    // 註冊監聽螢幕開啟的廣播接收者
                    IntentFilter intentFilter = new IntentFilter();
                    intentFilter.addAction(Intent.ACTION_SCREEN_ON);
                    intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
                    mScreenReciever = new ScreenReceiver();
                    registerReceiver(mScreenReciever, intentFilter);
                    tvDescription
                            .setText(getString(R.string.label_detect_screen));
                    tvDescription.setVisibility(View.VISIBLE);
                } else {
                    // 銷毀監聽螢幕開啟的廣播接收者
                    if (mScreenReciever != null) {
                        unregisterReceiver(mScreenReciever);
                        mScreenReciever = null;
                    }
                    tvDescription.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (mScreenReciever != null) {
            unregisterReceiver(mScreenReciever);
            mScreenReciever = null;
        }
        super.onDestroy();
    }

    /**
     * 監聽螢幕開啟的廣播接收者
     */
    public static class ScreenReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Vibrator vib = (Vibrator) context
                    .getSystemService(VIBRATOR_SERVICE);
            // 螢幕ON
            if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                // 發出短振動
                vib.vibrate(300);
            // 螢幕OFF
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                // 發出長振動
                vib.vibrate(1000);
            }
        }
    }
}
