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
        // ³qª¾¹q¦Àª¬ºAªº¼s¼½±µ¦¬ªÌ
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

            // ³Ñ¾l¹q¶q
            TextView tvBatteryLevel = (TextView) mActivity
                    .findViewById(R.id.BatteryLevel);
            int level = intent.getIntExtra("level", 0);
            int scale = intent.getIntExtra("scale", 0);
            tvBatteryLevel.setText(level + " / " + scale + "(%)");

            // ¥R¹qª¬ºA
            TextView tvBatteryCharge = (TextView) mActivity
                    .findViewById(R.id.BatteryCharge);
            switch (intent.getIntExtra("status", 0)) {
            case BatteryManager.BATTERY_STATUS_CHARGING:
                // ¥R¹q¤¤
                tvBatteryCharge.setText(context
                        .getString(R.string.label_detect_charging));
                break;
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                // ©ñ¹q¤¤
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                tvBatteryCharge.setText(context
                        .getString(R.string.label_detect_discharging));
                break;
            case BatteryManager.BATTERY_STATUS_FULL:
                // ¹¡©M
                tvBatteryCharge.setText(context
                        .getString(R.string.label_detect_fullbattery));
                break;
            case BatteryManager.BATTERY_STATUS_UNKNOWN:
                // ¤£©ú
                Log.d("BatteryChange",
                        context.getString(R.string.label_detect_status_unknown));
                break;
            }

            // «~½è
            TextView tvBatteryQuality = (TextView) mActivity
                    .findViewById(R.id.BatteryQuality);
            switch (intent.getIntExtra("health", 0)) {
            case BatteryManager.BATTERY_HEALTH_GOOD:
                // ¨}¦n
                tvBatteryQuality.setText(context
                        .getString(R.string.label_detect_good));
                break;
            case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                // ¹L¼ö
                tvBatteryQuality.setText(context
                        .getString(R.string.label_detect_overheart));
                break;
            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                // ¹L¹¡©M
                tvBatteryQuality.setText(context
                        .getString(R.string.label_detect_overvoltage));
                break;
            case BatteryManager.BATTERY_HEALTH_DEAD:
                // ¬G»Ù
                tvBatteryQuality.setText(context
                        .getString(R.string.label_detect_dead));
                break;
            case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                // ¯S©w¿ù»~“Á’èŽ¸”s
                tvBatteryQuality.setText(context
                        .getString(R.string.label_detect_unspecifiedfailure));
                break;
            case BatteryManager.BATTERY_HEALTH_COLD:
                // ¹L§C·Å’á‰·
                tvBatteryQuality.setText(context
                        .getString(R.string.label_detect_cold));
                break;
            case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                // ¤£©ú
                tvBatteryQuality.setText(context
                        .getString(R.string.label_detect_unknown));
                break;
            }

            // ·Å«×&¹qÀ£
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

            // ¥R¹q¤èªk
            TextView tvBatteryConnect = (TextView) mActivity
                    .findViewById(R.id.BatteryConnect);
            switch (intent.getIntExtra("plugged", 0)) {
            case BatteryManager.BATTERY_PLUGGED_AC:
                // ³s±µAC
                tvBatteryConnect.setText(context
                        .getString(R.string.label_detect_ac));
                break;
            case BatteryManager.BATTERY_PLUGGED_USB:
                // ³s±µUSB
                tvBatteryConnect.setText(context
                        .getString(R.string.label_detect_usb));
                break;
            case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                // µL½u³s±µ
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
