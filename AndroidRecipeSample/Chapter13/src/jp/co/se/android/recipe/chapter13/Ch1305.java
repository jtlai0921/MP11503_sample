package jp.co.se.android.recipe.chapter13;

import jp.co.se.android.recipe.chapter13.R;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class Ch1305 extends Activity {
    private UnlockReciever mUnlockReciever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1305_main);
        final TextView tvDescription = (TextView) findViewById(R.id.description);
        final Switch unlockSwitch = (Switch) findViewById(R.id.unlockSwitch);

        // 開關的ON/OFF
        unlockSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
                if (isChecked) {
                    // 註冊監聽解鎖的廣播接收者
                    IntentFilter intentFilter = new IntentFilter(
                            Intent.ACTION_USER_PRESENT);
                    mUnlockReciever = new UnlockReciever();
                    registerReceiver(mUnlockReciever, intentFilter);

                    tvDescription
                            .setText(getString(R.string.label_detect_unlock));
                    tvDescription.setVisibility(View.VISIBLE);
                } else {
                    // 銷毀監聽解鎖的廣播接收者
                    if (mUnlockReciever != null) {
                        unregisterReceiver(mUnlockReciever);
                        mUnlockReciever = null;
                    }
                    tvDescription.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (mUnlockReciever != null) {
            unregisterReceiver(mUnlockReciever);
            mUnlockReciever = null;
        }
        super.onDestroy();
    }

    /**
     * 要監聽裝置lock狀態的廣播接收者
     */
    public static class UnlockReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
                Toast.makeText(context,
                        context.getString(R.string.label_detect_unlocked),
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}
