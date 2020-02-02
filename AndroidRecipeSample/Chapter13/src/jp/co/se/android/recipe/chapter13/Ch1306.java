package jp.co.se.android.recipe.chapter13;

import jp.co.se.android.recipe.chapter13.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class Ch1306 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1306_main);
        final TextView tvDescription = (TextView) findViewById(R.id.description);
        final Switch callSwitch = (Switch) findViewById(R.id.callSwitch);

        // 開關的ON/OFF
        callSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
                if (isChecked) {
                    // 設定監聽電話情況的監聽器
                    TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    tm.listen(mPhoneStateListener,
                            PhoneStateListener.LISTEN_CALL_STATE);

                    tvDescription
                            .setText(getString(R.string.label_detect_callphone));
                    tvDescription.setVisibility(View.VISIBLE);
                } else {
                    tvDescription.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 接收電話狀態的監聽器
     */
    PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
        public void onCallStateChanged(int state, String number) {
            if (state == TelephonyManager.CALL_STATE_RINGING) {
                // 監聽來電
                Toast.makeText(Ch1306.this,
                        getString(R.string.label_detect_calling, number),
                        Toast.LENGTH_SHORT).show();
            } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                // 監聽通話
                Toast.makeText(Ch1306.this,
                        getString(R.string.label_detect_talking, number),
                        Toast.LENGTH_SHORT).show();
            }
        }
    };
}
