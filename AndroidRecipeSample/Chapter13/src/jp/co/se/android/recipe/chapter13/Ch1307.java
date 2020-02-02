package jp.co.se.android.recipe.chapter13;

import jp.co.se.android.recipe.chapter13.R;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Ch1307 extends Activity {
    private BatteryChangedReceiver mBatteryChangedReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1307_main);
        // �q���q�����A���s��������
        IntentFilter intentFilter = new IntentFilter(
                Intent.ACTION_BATTERY_CHANGED);
        mBatteryChangedReceiver = new BatteryChangedReceiver(this);
        registerReceiver(mBatteryChangedReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        if (mBatteryChangedReceiver != null) {
            unregisterReceiver(mBatteryChangedReceiver);
            mBatteryChangedReceiver = null;
        }
        super.onDestroy();
    }

    public static class BatteryChangedReceiver extends BroadcastReceiver {
        Activity mActivity;

        public BatteryChangedReceiver(Activity activity) {
            this.mActivity = activity;
        }

        @Override
        public void onReceive(Context context, Intent intent) {

            // �Ѿl�q�q
            TextView tvBatteryLevel = (TextView) mActivity
                    .findViewById(R.id.BatteryLevel);
            int level = intent.getIntExtra("level", 0);
            int scale = intent.getIntExtra("scale", 0);
            tvBatteryLevel.setText(level + " / " + scale + "(%)");

            // �R�q���A
            TextView tvBatteryCharge = (TextView) mActivity
                    .findViewById(R.id.BatteryCharge);
            switch (intent.getIntExtra("status", 0)) {
            case BatteryManager.BATTERY_STATUS_CHARGING:
                // �R�q��
                tvBatteryCharge.setText(context
                        .getString(R.string.label_detect_charging));
                break;
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                // ��q��
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                tvBatteryCharge.setText(context
                        .getString(R.string.label_detect_discharging));
                break;
            case BatteryManager.BATTERY_STATUS_FULL:
                // ���M
                tvBatteryCharge.setText(context
                        .getString(R.string.label_detect_fullbattery));
                break;
            case BatteryManager.BATTERY_STATUS_UNKNOWN:
                // ����
                Log.d("BatteryChange",
                        context.getString(R.string.label_detect_status_unknown));
                break;
            }

            // �~��
            TextView tvBatteryQuality = (TextView) mActivity
                    .findViewById(R.id.BatteryQuality);
            switch (intent.getIntExtra("health", 0)) {
            case BatteryManager.BATTERY_HEALTH_GOOD:
                // �}�n
                tvBatteryQuality.setText(context
                        .getString(R.string.label_detect_good));
                break;
            case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                // �L��
                tvBatteryQuality.setText(context
                        .getString(R.string.label_detect_overheart));
                break;
            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                // �L���M
                tvBatteryQuality.setText(context
                        .getString(R.string.label_detect_overvoltage));
                break;
            case BatteryManager.BATTERY_HEALTH_DEAD:
                // �G��
                tvBatteryQuality.setText(context
                        .getString(R.string.label_detect_dead));
                break;
            case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                // �S�w���~���莸�s
                tvBatteryQuality.setText(context
                        .getString(R.string.label_detect_unspecifiedfailure));
                break;
            case BatteryManager.BATTERY_HEALTH_COLD:
                // �L�C�Œቷ
                tvBatteryQuality.setText(context
                        .getString(R.string.label_detect_cold));
                break;
            case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                // ����
                tvBatteryQuality.setText(context
                        .getString(R.string.label_detect_unknown));
                break;
            }

            // �ū�&�q��
            TextView tvBatteryTemperature = (TextView) mActivity
                    .findViewById(R.id.BatteryTemperature);
            int temperatue = intent.getIntExtra("temperature", 0) / 10;
            float voltage = intent.getIntExtra("voltage", 0) / 1000;
            tvBatteryTemperature
                    .setText(context.getString(
                            R.string.label_detect_temperature, temperatue)
                            + " / "
                            + context.getString(R.string.label_detect_voltage,
                                    voltage));

            // �R�q��k
            TextView tvBatteryConnect = (TextView) mActivity
                    .findViewById(R.id.BatteryConnect);
            switch (intent.getIntExtra("plugged", 0)) {
            case BatteryManager.BATTERY_PLUGGED_AC:
                // �s��AC
                tvBatteryConnect.setText(context
                        .getString(R.string.label_detect_ac));
                break;
            case BatteryManager.BATTERY_PLUGGED_USB:
                // �s��USB
                tvBatteryConnect.setText(context
                        .getString(R.string.label_detect_usb));
                break;
            case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                // �L�u�s��
                tvBatteryConnect.setText(context
                        .getString(R.string.label_detect_wireless));
                break;
            default:
                tvBatteryConnect.setText(context
                        .getString(R.string.label_detect_disconnect));
                break;

            }
        }
    };
}
